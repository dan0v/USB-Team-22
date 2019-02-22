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
     * @param previousAngle Integer representation of the angle required to reach current location.
     * @param currentAngle Integer representation of the angle required to reach next location.
     * @return Direction enum.
     * @throws IllegalArgumentException
     */
    public static Direction parseDirection(int previousAngle, int currentAngle) throws IllegalArgumentException {
        switch (currentAngle) {
            case -1:  return LIFT_UP;
            case -2:  return LIFT_DOWN;
            case -3:  return STAIR_UP;
            case -4:  return STAIR_DOWN;
            default:  break;
        }

        // TODO return forward, left, or right Direction based on the difference between navigation angles provided.

        throw new IllegalArgumentException("Invalid Direction value provided");
    }
}
