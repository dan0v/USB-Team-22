package uk.ac.newcastle.team22.usb.navigation;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreApp.AbstractCardData;
import uk.ac.newcastle.team22.usb.coreUSB.Floor;

/**
 * A class used to construct the CardView data used for navigation.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class CardBuilder {

    /**
     * Given a list of edges, returns a list of cards for displaying turn by turn navigation between
     * nodes.
     * @param edges list of edges whose directions should be parsed.
     * @return List of direction enums.
     */
    public static List<AbstractCardData> buildCards(List<Edge> edges, Context context) {
        List<AbstractCardData> cards = new ArrayList();
        List<Node> tourNodes = new ArrayList();
        List<Double> distances = new ArrayList();
        List<Direction> parsedDirections = Direction.parseDirections(edges);
        List<String> formattedFloorChanges = new ArrayList();
        List<Integer> floorChangesDifference = new ArrayList();

        int i = 0;

        // Simplify input data to list of distances.
        for (Edge currentEdge : edges) {

            // Store node if tour node.
            if (currentEdge.getOrigin().isTourNode()) {
                tourNodes.add(currentEdge.getOrigin());
                distances.add(i, 0.0);
                parsedDirections.add(i, Direction.TOUR_LOCATION);
                i++;
            }

            // Display the floor to exit lifts at.
            if (currentEdge.getOrigin().getFloorNumber() != currentEdge.getDestination().getFloorNumber()) {
                int floorNumber = currentEdge.getDestination().getFloorNumber();

                Floor edgeFloor = new Floor(floorNumber);
                formattedFloorChanges.add(edgeFloor.getFormattedName(context));

                floorChangesDifference.add(edgeFloor.getNumber() - currentEdge.getOrigin().getFloorNumber());
            }

            // Create list of distances.
            for (double currentDistance : currentEdge.distances) {
                if (i == 0) {
                    distances.add(currentDistance);
                } else {
                    // Combine duplicate FORWARD directions.
                    if (parsedDirections.get(i).equals(Direction.FORWARD) && parsedDirections.get(i - 1).equals(Direction.FORWARD)) {
                        distances.set(i - 1, currentDistance + distances.get(i - 1));
                        parsedDirections.remove(i);
                        i--;
                    } else {
                        distances.add(currentDistance);
                    }
                }
                i++;
            }
        }

        // Construct cards.
        int currentTourNodeIndex = 0;
        int currentFloorChange = 0;
        
        for (int j = 0; j < parsedDirections.size(); j++) {
            if (parsedDirections.get(j).equals(Direction.TOUR_LOCATION)) {
                Node currentNode = tourNodes.get(currentTourNodeIndex);
                TourCardData currentCard = new TourCardData(currentNode.getName(), (currentNode.getDescription().replace("\\n", "\n")), currentNode.getImage());
                cards.add(currentCard);
                currentTourNodeIndex++;
            } else {
                if (parsedDirections.get(j).equals(Direction.LIFT_UP) || parsedDirections.get(j).equals(Direction.LIFT_DOWN) || parsedDirections.get(j).equals(Direction.STAIR_UP) || parsedDirections.get(j).equals(Direction.STAIR_DOWN)) {
                    String directionText = String.format(context.getString(parsedDirections.get(j).getLocalisedDirection()), formattedFloorChanges.get(currentFloorChange));
                    int floorDifference = floorChangesDifference.get(currentFloorChange);
                    String floorChangeText;
                    if (floorDifference == 1 || floorDifference == -1) {
                        floorChangeText = String.format(context.getString(R.string.navigationFloor), floorDifference);
                    }
                    else {
                        floorChangeText = String.format(context.getString(R.string.navigationFloors), floorDifference);
                    }
                    DirectionCardData currentCard = new DirectionCardData(directionText, floorChangeText, parsedDirections.get(j).getImageRepresentation());
                    cards.add(currentCard);
                    currentFloorChange++;
                } else {
                    String directionText = context.getString(parsedDirections.get(j).getLocalisedDirection());
                    int distance = ((int) Math.round(distances.get(j)));
                    String distanceText;

                    // Ignore tour directions in the same location.
                    if (distance == 0) {
                        continue;
                    }

                    if (distance == 1) {
                        distanceText = context.getString(R.string.navigationStep);
                    }
                    else {
                        distanceText = String.format(context.getString(R.string.navigationSteps), distance);
                    }
                    DirectionCardData currentCard = new DirectionCardData(directionText, distanceText, parsedDirections.get(j).getImageRepresentation());
                    cards.add(currentCard);
                }
            }
        }

        // Add destination card.
        DirectionCardData finalCard = new DirectionCardData(context.getString(R.string.navigationEnd),context.getString(R.string.navigationDestinationReached), R.drawable.navigation_destination_flag);
        cards.add(finalCard);
        return cards;
    }
}
