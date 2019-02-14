package uk.ac.newcastle.team22.usb.coreUSB;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.firebase.FirestoreConstructable;

import android.content.Context;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

/**
 * A class which represents a set of opening hours.
 *
 * @author Alexander MacLeod
 * @version 1.0
 */
public class OpeningHours implements FirestoreConstructable<OpeningHours> {

    /** The days of the week where the service is open. */
    private HashMap<DayOfWeek, Hours> openingHours;

    /** The service which has opening hours. */
    private Service service;

    /** The Firestore document identifier suffix for opening times. */
    private static String opensFirestoreKey = "Open";

    /** The Firestore document identifier suffix for closing times. */
    private static String closesFirestoreKey = "Close";

    @Override
    public OpeningHours initFromFirebase(Map<String, Object> firestoreDictionary, String documentIdentifier) throws FirestoreConstructable.InitialisationFailed {
        this.service = Service.valueFor(documentIdentifier);
        openingHours = new HashMap<>();

        // Loop the days of the week in order to find opening times.
        for (DayOfWeek day : DayOfWeek.values()) {
            String dayName = day.name().toLowerCase();

            LocalTime opens = getLocalTime(firestoreDictionary.get(dayName + opensFirestoreKey));
            LocalTime closes = getLocalTime(firestoreDictionary.get(dayName + closesFirestoreKey));

            // Check if opening times have been set.
            if (opens == null || closes == null) {
                continue;
            }

            OpeningHours.Hours hours = new OpeningHours.Hours(opens, closes);
            openingHours.put(day, hours);
        }
        return this;
    }

    /** Empty constructor. */
    public OpeningHours() {}

    /**
     * @param context The current context.
     * @param day The day of the week.
     * @return The description of the opening hours on a given day.
     */
    public String getDescription(Context context, DayOfWeek day) {
        Hours dayOpeningHours = openingHours.get(day);

        // If the day has null opening hours, treat the service as closed all day.
        if (dayOpeningHours == null) {
            return context.getString(R.string.closed);
        }
        return openingHours.get(day).toString();
    }

    /**
     * @return Boolean value whether the service is open.
     */
    public boolean isOpen() {
        LocalTime now = LocalTime.now();
        DayOfWeek dayOfWeek = DayOfWeek.from(now);
        Hours openingHours = this.openingHours.get(dayOfWeek);

        // If the day has null opening hours, treat the service as closed.
        if (openingHours == null) {
            return false;
        }
        return openingHours.getOpens().isBefore(now) && openingHours.getCloses().isAfter(now);
    }

    /**
     * Creates a {@link LocalTime} from the Firestore representation of the time.
     * @param representation The {@code String} representation of the time.
     * @return The {@link LocalTime}.
     */
    private LocalTime getLocalTime(Object representation) {
        // Check whether a time has been set.
        if (representation == null) {
            return null;
        }

        // Check whether time has been represented correctly.
        String[] components = representation.toString().split(":", 2);
        if (components.length != 2) {
            return null;
        }

        int hours = Integer.parseInt(components[0]);
        int minutes = Integer.parseInt(components[1]);
        return LocalTime.of(hours, minutes);
    }

    /**
     * @return The service which has opening hours.
     */
    public Service getService() {
        return service;
    }

    /**
     * An enum which defines a service with {@link OpeningHours}.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    public enum Service {
        NORMAL("usbNormalHours"), OUT_OF_HOURS("usbOutOfHours"), CAFE("cafeHours");

        /** The identifier of the service. */
        private String identifier;

        /**
         * Constructor for {@link Service} using its raw value.
         * @param identifier The {@code String} representation or raw value of the service;
         */
        Service(String identifier) {
            this.identifier = identifier;
        }

        /**
         * Returns the {@link Service} for a given identifier.
         * @param rawIdentifier The identifier of the service.
         * @return The {@link Service}.
         * @throws FirestoreConstructable.InitialisationFailed
         */
        public static Service valueFor(String rawIdentifier) throws FirestoreConstructable.InitialisationFailed {
            for (Service service : Service.values()) {
                if (service.identifier.equals(rawIdentifier)) {
                    return service;
                }
            }
            throw new FirestoreConstructable.InitialisationFailed("Unknown opening hours service identifier '" + rawIdentifier + "'");
        }
    }

    /**
     * A class which describes when a service is open on a given day.
     *
     * @author Alexander MacLeod
     * @version 1.0
     */
    class Hours {

        /** The opening time. */
        private LocalTime opens;

        /** The closing time. */
        private LocalTime closes;

        Hours(LocalTime opens, LocalTime closes) {
            this.opens = opens;
            this.closes = closes;
        }

        @Override
        public String toString() {
            String opens = this.opens.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            String closes = this.closes.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            return opens + " - " + closes;
        }

        /**
         * @return The opening time.
         */
        LocalTime getOpens() {
            return opens;
        }

        /**
         * @return The closing time.
         */
        LocalTime getCloses() {
            return closes;
        }
    }
}
