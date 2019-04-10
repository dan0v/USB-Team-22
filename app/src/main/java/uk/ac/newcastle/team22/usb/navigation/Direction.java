package uk.ac.newcastle.team22.usb.navigation;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;

/**
 * A class to define navigation directions.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public enum Direction {
    FORWARD, LEFT, MODERATE_LEFT, SHARP_LEFT, RIGHT, MODERATE_RIGHT, SHARP_RIGHT, LIFT_UP, LIFT_DOWN, STAIR_UP, STAIR_DOWN, TOUR_LOCATION;

    /**
     * @return Localised string representation of the direction.
     */
    public @StringRes int getLocalisedDirection() {
        switch (this) {
            case FORWARD:           return R.string.navigationForward;
            case LEFT:              return R.string.navigationLeft;
            case MODERATE_LEFT:     return R.string.navigationModerateLeft;
            case SHARP_LEFT:        return R.string.navigationSharpLeft;
            case RIGHT:             return R.string.navigationRight;
            case MODERATE_RIGHT:    return R.string.navigationModerateRight;
            case SHARP_RIGHT:       return R.string.navigationSharpRight;
            case LIFT_UP:           return R.string.navigationLiftUp;
            case LIFT_DOWN:         return R.string.navigationLiftDown;
            case STAIR_UP:          return R.string.navigationStairUp;
            case STAIR_DOWN:        return R.string.navigationStairDown;
            default:                return 0;
        }
    }

    /**
     * @return Image representation of the direction.
     */
    public @DrawableRes int getImageRepresentation() {
        switch (this) {
            case FORWARD:           return R.drawable.navigation_forward;
            case LEFT:              return R.drawable.navigation_left;
            case MODERATE_LEFT:     return R.drawable.navigation_moderate_left;
            case SHARP_LEFT:        return R.drawable.navigation_sharp_left;
            case RIGHT:             return R.drawable.navigation_right;
            case MODERATE_RIGHT:    return R.drawable.navigation_moderate_right;
            case SHARP_RIGHT:       return R.drawable.navigation_sharp_right;
            case LIFT_UP:           return R.drawable.navigation_lift_up;
            case LIFT_DOWN:         return R.drawable.navigation_lift_down;
            case STAIR_UP:          return R.drawable.navigation_stairs_up;
            case STAIR_DOWN:        return R.drawable.navigation_stairs_down;
            default:                return 0;
        }
    }

    /**
     * Given a list of Edges, returns a list of directions for turn by turn navigation between nodes.
     * @param edges list of edges whose directions should be parsed.
     * @return List of direction enums.
     * @throws IllegalArgumentException
     */
    public static List<Direction> parseDirections(List<Edge> edges) throws IllegalArgumentException {
        List<Direction> directions = new ArrayList();
        List<Integer> angleList = new ArrayList();

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
                case -1:
                    directions.add(LIFT_UP);
                    continue;
                case -2:
                    directions.add(LIFT_DOWN);
                    continue;
                case -3:
                    directions.add(STAIR_UP);
                    continue;
                case -4:
                    directions.add(STAIR_DOWN);
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
                directions.add(MODERATE_LEFT);
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
                directions.add(MODERATE_RIGHT);
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

    /**
     * Given a list of Edges, returns a list of cards for displaying turn by turn navigation between
     * nodes.
     * @param edges list of edges whose directions should be parsed.
     * @return List of direction enums.
     * @throws IllegalArgumentException
     */
    public static List<AbstractCardData> buildCards(List<Edge> edges, Context context) {
        List<AbstractCardData> cards = new ArrayList();
        List<Node> tourNodes = new ArrayList();
        List<Double> distances = new ArrayList();
        List<Direction> parsedDirections = parseDirections(edges);
        List<String> floorChanges = new ArrayList();
        int i = 0;

        // Simplify input data to list of distances.
        for (Edge currentEdge : edges) {

            // Store node if tour node.
            if (currentEdge.getOrigin().isTourNode()) {
                tourNodes.add(currentEdge.getOrigin());
                distances.add(i, 0.0);
                parsedDirections.add(i, TOUR_LOCATION);
            }

            // Display the floor to exit lifts at.
            if (currentEdge.getOrigin().getFloorNumber() != currentEdge.getDestination().getFloorNumber()) {
                if (currentEdge.getDestination().getFloorNumber() == 0) {
                    floorChanges.add("G");
                } else {
                    floorChanges.add(currentEdge.getDestination().getFloorNumber() + "");
                }
            }

            // Create list of distances.
            for (double currentDistance : currentEdge.distances) {
                if (i == 0) {
                    distances.add(currentDistance);
                } else {
                    // Combine duplicate FORWARD directions.
                    if (parsedDirections.get(i).equals(FORWARD) && parsedDirections.get(i-1).equals(FORWARD)) {
                        distances.set(i - 1, currentDistance + distances.get(i - 1));
                        parsedDirections.remove(i);
                    } else {
                        distances.add(currentDistance);
                    }
                }
                i++;
            }
        }

        // Construct cards.
        int currentNodeIndex = 0;
        int currentFloorChange = 0;
        for (int j = 0; j < parsedDirections.size(); j++) {
            if (parsedDirections.get(j).equals(TOUR_LOCATION)) {
                Node currentNode = tourNodes.get(currentNodeIndex);
                TourCardData currentCard = new TourCardData(currentNode.getName(), currentNode.getDescription(), currentNode.getImageIdentifier());
                cards.add(currentCard);
                currentNodeIndex++;
            } else {
                if (parsedDirections.get(j).equals(LIFT_UP) || parsedDirections.get(j).equals(LIFT_DOWN) || parsedDirections.get(j).equals(STAIR_UP) || parsedDirections.get(j).equals(STAIR_DOWN)) {
                    String directionText = String.format(context.getString(parsedDirections.get(j).getLocalisedDirection()), floorChanges.get(currentFloorChange));
                    DirectionCardData currentCard = new DirectionCardData(directionText, floorChanges.get(currentFloorChange), parsedDirections.get(j).getImageRepresentation());
                    cards.add(currentCard);
                    currentFloorChange++;
                } else {
                    String directionText = context.getString(parsedDirections.get(j).getLocalisedDirection());
                    DirectionCardData currentCard = new DirectionCardData(directionText, ((int) Math.round(distances.get(j))) + "", parsedDirections.get(j).getImageRepresentation());
                    cards.add(currentCard);
                }
            }
        }

        // Add destination card.
        DirectionCardData finalCard = new DirectionCardData(context.getString(R.string.navigationDestinationReached), "", R.drawable.navigation_destination_flag);
        cards.add(finalCard);
        return cards;
    }
}
