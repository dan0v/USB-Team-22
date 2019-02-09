package uk.ac.newcastle.team22.usb.navigation;

import android.support.annotation.StringRes;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class to define navigation directions.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public enum Direction {
    FORWARD, LEFT, RIGHT, LIFTUP, LIFTDOWN, STAIRUP, STAIRDOWN;

    /**
     * @return Localised String representation of a Direction.
     */
    public @StringRes int getLocalisedDirection() {
        switch (this) {
            case FORWARD:   return R.string.directionForward;
            case LEFT:  return R.string.directionLeft;
            case RIGHT: return R.string.directionRight;
            case LIFTUP:    return R.string.directionLiftUp;
            case LIFTDOWN:  return R.string.directionLiftDown;
            case STAIRUP:    return R.string.directionStairUp;
            case STAIRDOWN:  return R.string.directionStairDown;
            default:    return 0;
        }
    }

    /**
     *
     * @param input String representation of a Direction from Firebase.
     * @return Direction enum.
     * @throws IllegalArgumentException
     */
    public static Direction parseDirection(String input) throws IllegalArgumentException {
        switch (input) {
            case "f":   return FORWARD;
            case "l":   return RIGHT;
            case "r":   return LEFT;
            case "lu":  return LIFTUP;
            case "ld":  return LIFTDOWN;
            case "su":  return STAIRUP;
            case "sd":  return STAIRDOWN;
            default:    throw new IllegalArgumentException("Nonexistent Direction String provided");
        }
    }
}
