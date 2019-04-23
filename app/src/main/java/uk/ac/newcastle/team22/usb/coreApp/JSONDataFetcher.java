package uk.ac.newcastle.team22.usb.coreApp;

import android.app.Activity;
import android.content.Context;
import android.net.SSLCertificateSocketFactory;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import uk.ac.newcastle.team22.usb.coreUSB.Floor;
import uk.ac.newcastle.team22.usb.coreUSB.Resource;
import uk.ac.newcastle.team22.usb.coreUSB.ResourceType;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class that downloads JSON data describing available computers in the Urban Sciences Building from NUIT.
 *
 * @author Daniel Vincent
 * @author Alexander MacLeod
 * @version 1.0
 */
public class JSONDataFetcher extends AsyncTask<Void, Void, Void> {

    /** The URL for accessing live computer availability. */
    private final String NUIT_ADDRESS = "https://csi.ncl.ac.uk/usb/?json=y";

    /** The required Wi-FI SSID to access NUIT computer availability. */
    private final String NEWCASTLE_WIFI_SSID = "\"newcastle-university\"";

    /** The response when the Wi-Fi SSID is not available. */
    private final String UNKNOWN_WIFI_SSID = "<unknown ssid>";

    /** The Wi-Fi manager. */
    private WifiManager wifiManager;

    /** The response reference. */
    private AsyncResponse reference;

    /**
     * @param activity The current activity.
     * @param reference UI activity fetching JSON update.
     */
    public JSONDataFetcher(Activity activity, AsyncResponse reference) {
        this.reference = reference;
        wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public Void doInBackground(Void... voids) {
        try {
            // Initial check for the correct WIFI SSID.
            WifiInfo wifiInfo;
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                if (!wifiInfo.getSSID().equals(NEWCASTLE_WIFI_SSID) && !wifiInfo.getSSID().equals(UNKNOWN_WIFI_SSID)) {
                    throw new java.net.UnknownHostException();
                }
            }

            // Request computer availability.
            URL url = new URL(NUIT_ADDRESS);
            HttpsURLConnection connection = (HttpsURLConnection ) url.openConnection();
            connection.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
            connection.setHostnameVerifier(getHostnameVerifier());
            connection.connect();

            JsonParser parser = new JsonParser();
            JsonObject base = parser.parse(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
            JsonArray locationSummary = base.get("usb_location_summary").getAsJsonArray();
            Map<Integer, Floor> buildingFloors = USBManager.shared.getBuilding().getFloors();

            for (JsonElement location : locationSummary) {
                JsonObject locationObject = location.getAsJsonObject();

                int floor = Integer.parseInt(locationObject.get("location_name").getAsString().substring(4,5));
                String number = locationObject.get("location_name").getAsString().substring(6,9);
                int availableComputers = Integer.parseInt(locationObject.get("location_free").getAsString());
                int totalComputers = Integer.parseInt(locationObject.get("location_total").getAsString());

                Resource temp = new Resource(ResourceType.COMPUTER, availableComputers, totalComputers);
                try {
                    buildingFloors.get(floor).getRooms().get(number).updateComputerAvailability(temp);
                    Log.d("JSON Updater", "Floor: " + floor + " Room: " + number + "'s available computer data has been updated");
                } catch (Exception e) {
                    Log.e("JSON Updater", "Room is missing from Firestore: Floor: " + floor + " Room: " + number);
                    e.printStackTrace();
                }
            }

            // Successful update.
            reference.onComplete();

        } catch (Exception e) {
            // Notify UI that there is a network issue.
            reference.onBadNetwork();
            Log.e("JSON", "Something went wrong with fetching JSON data.: ");
            e.printStackTrace();
        }
        return null;
    }

    public void setReference(AsyncResponse reference) {
        this.reference = reference;
    }

    private HostnameVerifier getHostnameVerifier() {
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier verifier =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                return verifier.verify("csi.ncl.ac.uk", session);
            }
        };
        return hostnameVerifier;
    }
}