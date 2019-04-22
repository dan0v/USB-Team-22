package uk.ac.newcastle.team22.usb.coreApp;

import android.net.SSLCertificateSocketFactory;
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
 * @version 1.0
 */
public class JSONDataFetcher extends AsyncTask<Void, Void, Void> {

    private final String USBJsonURL = "https://csi.ncl.ac.uk/usb/?json=y";

    private AsyncResponse reference = null;

    /**
     * @param reference UI activity fetching JSON update.
     */
    public JSONDataFetcher(AsyncResponse reference) {
        this.reference = reference;
    }

    @Override
    public Void doInBackground(Void... voids) {
        try {
            URL url = new URL(USBJsonURL);
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

                int floor  = Integer.parseInt(locationObject.get("location_name").getAsString().substring(4,5));
                int number  = Integer.parseInt(locationObject.get("location_name").getAsString().substring(6,9));
                int availableComputers = Integer.parseInt(locationObject.get("location_free").getAsString());
                int totalComputers = Integer.parseInt(locationObject.get("location_total").getAsString());

                Resource temp = new Resource(ResourceType.COMPUTER, availableComputers, totalComputers);
                try {
                    buildingFloors.get(floor).getRooms().get(number).updateComputerAvailability(temp);
                    Log.d("JSON Updater", "Floor: " + floor + " Room: " + number + "'s available computer data has been updated");
                } catch (Exception e) {
                    Log.e("JSON Updater", "Room is missing from Firestore.: Floor: " + floor + " Room: " + number);
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
        reference = reference;
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