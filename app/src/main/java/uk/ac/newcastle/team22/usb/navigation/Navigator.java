package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * Calculates route from start navigable to end navigable.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable origin, Navigable destination, boolean accessibility) {
        return getRoute(origin.getNavigationNode(), destination.getNavigationNode(), accessibility);
    }

    /**
     * Calculate route from building entrance if no start position provided.
     * @param destination The origin of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of edges to be traversed to reach destination.
     */
    public List<Edge> getRoute(Navigable destination, boolean accessibility) {
        return getRoute(USBManager.shared.getBuilding().getNavigationNodes().get(0), destination.getNavigationNode(), accessibility);
    }

    /**
     * Calculate route from start Node to end Node.
     * @param origin The origin of the journey.
     * @param destination The destination of the journey.
     * @param accessibility Boolean value whether the journey needs to be accessible.
     * @return List of edges to be traversed to reach the destination.
     */
    public List<Edge> getRoute(Node origin, Node destination, boolean accessibility) {
        bestRouteWeight = Double.MAX_VALUE;
        bestRoute = new ArrayList<>();

        // Find shortest route between origin and destination nodes using backtracking.
        recursiveExplore(origin, destination, origin.getFloorNumber(), new ArrayList<Node>(), accessibility, new ArrayList<Edge>(), 0);
        return bestRoute;
    }

    /**
     * @return List of edges to traverse for the Urban Sciences Building guided tour.
     */
    public List<Edge> getTourRoute() {
        List<Edge> tourRoute = new ArrayList<Edge>();
        Map<Integer, Node> nodeMap = USBManager.shared.getBuilding().getNavigationNodes();
        for (int tourNodeID : USBManager.shared.getBuilding().getTourNodeIdentifiers()) {
            Node currentNode = nodeMap.get(tourNodeID);
            for (Edge tourEdge : currentNode.getEdges()) {
                // Current tour node is the last node of the tour.
                if (tourEdge.getDestination().equals(currentNode)) {
                    break;
                }
                tourRoute.add(tourEdge);
            }
        }
        return tourRoute;
    }

    /**
     * Recursively explore all edges of adjacent nodes, backtracking if the current edge does not
     * meet requirements. <pre>bestRoute</pre> and <pre>bestRouteWeight</pre> will be updated with
     * the shortest route from the provided origin node to the provided destination node which meets
     * accessibility requirements.
     *
     * @param currentNode Node whose edges are being explored.
     * @param finalDestinationNode Node to navigate to.
     * @param previousFloorNumber Floor number of previous node.
     * @param visitedNodes List of nodes whose edges were explored to reach current node.
     * @param accessibility Ensure all edges added to route meet accessibility requirements.
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
