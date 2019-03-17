package uk.ac.newcastle.team22.usb.navigation;

import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;

/**
 * A class to define navigation directions.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public enum Direction {
    FORWARD, LEFT, SLIGHT_LEFT, SHARP_LEFT, RIGHT, SLIGHT_RIGHT, SHARP_RIGHT, LIFT_UP, LIFT_DOWN, STAIR_UP, STAIR_DOWN;

    /**
     * @return Localised String representation of a Direction.
     */
    public @StringRes int getLocalisedDirection() {
        switch (this) {
            case FORWARD:     return R.string.directionForward;
            case LEFT:        return R.string.directionLeft;
            case SLIGHT_LEFT: return R.string.directionSlightLeft;
            case SHARP_LEFT:  return R.string.directionSharpLeft;
            case RIGHT:       return R.string.directionRight;
            case SLIGHT_RIGHT:return R.string.directionSlightRight;
            case SHARP_RIGHT: return R.string.directionSharpRight;
            case LIFT_UP:     return R.string.directionLiftUp;
            case LIFT_DOWN:   return R.string.directionLiftDown;
            case STAIR_UP:    return R.string.directionStairUp;
            case STAIR_DOWN:  return R.string.directionStairDown;
            default:          return 0;
        }
    }

    /**
     * Given a list of Edges, returns a list of directions for turn by turn navigation between nodes.
     * @param edges list of edges whose directions should be parsed.
     * @return List of direction enums.
     * @throws IllegalArgumentException
     */
    public static List<Direction> parseDirections(List<Edge> edges) throws IllegalArgumentException {
        List<Direction> directions = new ArrayList<Direction>();
        List<Integer> angleList = new ArrayList<Integer>();

        // Simplify input data to list of direction angles.
        for (Edge currentEdge : edges) {
            for (int currentAngle : currentEdge.directions) {
                angleList.add(currentAngle);
            }
        }

        // The first direction when navigating will always be FORWARD,
        // as the user will use a compass to face the correct direction.
        directions.add(Direction.FORWARD);

        for (int i = 1; i < angleList.size(); i++) {
            int currentAngle = angleList.get(i);

            switch (currentAngle) {
                case -1:    directions.add(LIFT_UP);
                            continue;
                case -2:    directions.add(LIFT_DOWN);
                            continue;
                case -3:    directions.add(STAIR_UP);
                            continue;
                case -4:    directions.add(STAIR_DOWN);
                            continue;
            }
            int previousAngle = angleList.get(i-1);

            // Exiting a lift or staircase should always be FORWARD.
            if (previousAngle == -1 || previousAngle == -2 || previousAngle == -3 || previousAngle == -4) {
                directions.add(FORWARD);
                continue;
            }

            // Reformat input data to be between -179 and 180 degrees.
            if (currentAngle > 180)
                currentAngle = currentAngle - 360;
            if (previousAngle > 180)
                previousAngle = previousAngle - 360;

            // Calculate the difference between the current and previous angles.
            currentAngle = currentAngle - previousAngle;

            // Ensure angles are kept within expected range.
            if (currentAngle > 180)
                currentAngle = currentAngle - 360;
            else if (currentAngle < -180)
                currentAngle = currentAngle + 360;

            // Assign a Direction to each quadrant the resultant angle can be in.
            if (currentAngle > -25 && currentAngle < 25) {
                directions.add(FORWARD);
                continue;
            }

            if (currentAngle > -60 && currentAngle <= -25) {
                directions.add(SLIGHT_LEFT);
                continue;
            }

            if (currentAngle > -120 && currentAngle <= -60) {
                directions.add(LEFT);
                continue;
            }

            if (currentAngle > -179 && currentAngle <= -120) {
                directions.add(SHARP_LEFT);
                continue;
            }

            if (currentAngle < 60 && currentAngle >= 25) {
                directions.add(SLIGHT_RIGHT);
                continue;
            }

            if (currentAngle < 120 && currentAngle >= 60) {
                directions.add(RIGHT);
                continue;
            }

            if (currentAngle <= 180 && currentAngle >= 120) {
                directions.add(SHARP_RIGHT);
                continue;
            }

            // The current angle does not lie in the allowed range in the database (0 to 359).
            throw new IllegalArgumentException("Invalid Direction value provided: " + currentAngle + " at: " + i);
        }

        return directions;
    }
}
