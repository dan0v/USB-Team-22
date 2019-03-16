package uk.ac.newcastle.team22.usb.navigation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.newcastle.team22.usb.R;
import uk.ac.newcastle.team22.usb.coreUSB.USBManager;

/**
 * A class which calculates routes between locations in the Urban Sciences Building.
 * The journey planner obeys constraints such as the user's accessibility requirements.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class Navigator {

    /** The shared instance of the Urban Sciences Building navigator. */
    public static Navigator shared = new Navigator();

    /** The weight of the best route between two locations in the Urban Sciences Building. */
    private double bestRouteWeight = Double.MAX_VALUE;

    /** The list of edges for the best route between two locations in the Urban Sciences Building. */
    private List<Edge> bestRoute = new ArrayList<Edge>();

    /** The nodes to traverse when presenting a tour route in the Urban Sciences Building to a user. */
    private final int[] tourRouteNodes = {1000, 1001, 1002, };//1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014};

    /**
     * Calculates route from start Navigable to end Navigable.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable origin, Navigable destination, boolean accessibility) {
        return getRoute(origin.getNavNode(), destination.getNavNode(), accessibility);
    }

    /**
     * Calculate route from building entrance if no start position provided.
     * @param destination The origin of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable destination, boolean accessibility) {
        return getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), destination.getNavNode(), accessibility);
    }

    /**
     * Calculate route from start Node to end Node.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of Edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Node origin, Node destination, boolean accessibility) {
        bestRouteWeight = Double.MAX_VALUE;
        bestRoute = new ArrayList<>();

        // Find shortest route between origin and destination nodes using backtracking.
        recursiveExplore(origin, destination, origin.getFloorNumber(), new ArrayList<Node>(), accessibility, new ArrayList<Edge>(), 0);
        return bestRoute;
    }

    /**
     * @return The List of Edges to traverse for the Urban Sciences Building guided tour.
     */
    public List<Edge> getTourRoute() {
        List<Edge> tourRoute = new ArrayList<Edge>();
        for (int tourNodeID : tourRouteNodes) {
            try {
                Node currentNode = USBManager.shared.getBuilding().getNavigationNodes().get(tourNodeID);
                if (currentNode.getClass().equals(TourNode.class)) {
                    for (Edge tourEdge : currentNode.getEdges()) {
                        tourRoute.add(tourEdge);
                    }
                }
            } catch (Exception e) {
                String msg = String.format("Missing TourNode: %s", tourNodeID);
                Log.e("Navigation", msg, e);
            }
        }
        return tourRoute;
    }

    /**
     * Recursively explore all Edges of adjacent Nodes, backtracking if the current Edge does not
     * meet requirements. <pre>bestRoute</pre> and <pre>bestRouteWeight</pre> will be updated with
     * the shortest route from the provided origin Node to the provided destination Node which meets
     * accessibility requirements.
     *
     * @param currentNode Node whose Edges are being explored.
     * @param finalDestinationNode Node to navigate to.
     * @param previousFloorNumber Floor number of previous node.
     * @param visitedNodes List of Nodes whose Edges were explored to reach current Node.
     * @param accessibility Ensure all Edges added to route meet accessibility requirements.
     * @param candidateRoute Partially constructed route.
     * @param candidateWeight Weight of partially constructed route.
     */
    private void recursiveExplore(Node currentNode, Node finalDestinationNode, int previousFloorNumber, List<Node> visitedNodes, boolean accessibility, List<Edge> candidateRoute, double candidateWeight) {
        // Add node to visited nodes list.
        visitedNodes.add(currentNode);

        for (Edge currentEdge : currentNode.getEdges()) {
            // Edge returns to previously visited node, so ignore this edge.
            if (visitedNodes.contains(currentEdge.getDestination())) {
                continue;
            }
            // Edge leads to a floor further from the destination, so ignore this edge.
            if ((finalDestinationNode.getFloorNumber() > currentNode.getFloorNumber()) && ((currentEdge.getDestination().getFloorNumber() < previousFloorNumber) || (currentEdge.getDestination().getFloorNumber() > finalDestinationNode.getFloorNumber()))) {
                continue;
            }
            // Edge leads to a floor further from the destination, so ignore this edge.
            if ((finalDestinationNode.getFloorNumber() < currentNode.getFloorNumber()) && ((currentEdge.getDestination().getFloorNumber() > previousFloorNumber) || (currentEdge.getDestination().getFloorNumber() < finalDestinationNode.getFloorNumber()))) {
                continue;
            }
            // Edge does not meet accessibility requirements, so ignore this edge.
            if (accessibility && !currentEdge.accessible) {
                continue;
            }
            // Continuing route would be longer than current best route, so ignore this edge.
            if (candidateWeight + currentEdge.weight >= bestRouteWeight) {
                continue;
            }

            // Current shortest route found.
            if (currentEdge.getDestination().equals(finalDestinationNode)) {
                candidateRoute.add(currentEdge);
                bestRoute = new ArrayList<>(candidateRoute);
                bestRouteWeight = candidateWeight + currentEdge.weight;

                // After best route has been updated, remove current edge from candidate route to move back through recursive call chain and explore along the next edge.
                candidateRoute.remove(currentEdge);
                continue;
            }

            // No backtracking possible, so add edge to candidate route.
            candidateRoute.add(currentEdge);

            // Explore along candidate route.
            recursiveExplore(currentEdge.getDestination(), finalDestinationNode, currentNode.getFloorNumber(), visitedNodes, accessibility, candidateRoute, (candidateWeight + currentEdge.weight));

            // Remove current edge from candidate route to move back through recursive call chain and explore along the next edge.
            candidateRoute.remove(currentEdge);
        }

        // Remove current node from list of visited nodes when moving back through recursive call chain.
        visitedNodes.remove(currentNode);
        // Move back up recursive call chain to explore next node.
    }
}
