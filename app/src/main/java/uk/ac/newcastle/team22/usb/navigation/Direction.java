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
    FORWARD, LEFT, RIGHT, LIFT_UP, LIFT_DOWN, STAIR_UP, STAIR_DOWN;

    /**
     * @return Localised String representation of a Direction.
     */
    public @StringRes int getLocalisedDirection() {
        switch (this) {
            case FORWARD:     return R.string.directionForward;
            case LEFT:        return R.string.directionLeft;
            case RIGHT:       return R.string.directionRight;
            case LIFT_UP:     return R.string.directionLiftUp;
            case LIFT_DOWN:   return R.string.directionLiftDown;
            case STAIR_UP:    return R.string.directionStairUp;
            case STAIR_DOWN:  return R.string.directionStairDown;
            default:          return 0;
        }
    }

    /**
     * @param input String representation of a Direction from Firebase.
     * @return Direction enum.
     * @throws IllegalArgumentException
     */
    public static Direction parseDirection(String input) throws IllegalArgumentException {
        switch (input) {
            case "f":   return FORWARD;
            case "l":   return RIGHT;
            case "r":   return LEFT;
            case "lu":  return LIFT_UP;
            case "ld":  return LIFT_DOWN;
            case "su":  return STAIR_UP;
            case "sd":  return STAIR_DOWN;
            default:    throw new IllegalArgumentException("Invalid Direction String provided");
        }
    }
}
