package uk.ac.newcastle.team22.usb.navigation;

import java.util.ArrayList;
import java.util.List;

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
    private int bestRouteWeight = Integer.MAX_VALUE;

    /** The list of edges for the best route between two locations in the Urban Sciences Building. */
    private List<Edge> bestRoute = new ArrayList<Edge>();

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
        bestRouteWeight = Integer.MAX_VALUE;
        bestRoute = new ArrayList<>();

        //find shortest route between origin and destination Nodes using backtracking
        recursiveExplore(origin, destination, origin, new ArrayList<Node>(), accessibility, new ArrayList<Edge>(), Integer.MAX_VALUE);
        return bestRoute;
    }

    /**
     * Recursively explore all Edges of adjacent Nodes, backtracking if the current Edge does not
     * meet requirements. <pre>bestRoute</pre> and <pre>bestRouteWeight</pre> will be updated with
     * the shortest route from the provided origin Node to the provided destination Node which meets
     * accessibility requirements.
     *
     * @param originNode Node to navigate from.
     * @param destinationNode Node to navigate to.
     * @param currentNode Node whose Edges are being explored.
     * @param visitedNodes List of Nodes whose Edges were explored to reach current Node.
     * @param accessibility Ensure all Edges added to route meet accessibility requirements.
     * @param candidateRoute Partially constructed route.
     * @param candidateWeight Weight of partially constructed route.
     */
    private void recursiveExplore(Node originNode, Node destinationNode, Node currentNode, List<Node> visitedNodes, boolean accessibility, List<Edge> candidateRoute, int candidateWeight) {
        // Add node to visited nodes list.
        visitedNodes.add(currentNode);

        for (Edge currentEdge : currentNode.getEdges()) {
            // Edge returns to previously visited node, so ignore this edge.
            if (visitedNodes.contains(currentEdge.getDestination())) {
                continue;
            }
            // Edge leads out of between start and end node floors, so ignore this edge.
            if (destinationNode.getFloorNumber() > originNode.getFloorNumber() && currentEdge.getDestination().getFloorNumber() < originNode.getFloorNumber() || currentEdge.getDestination().getFloorNumber() > destinationNode.getFloorNumber()) {
                continue;
            }
            // Edge leads out of between start and end node floors, so ignore this edge.
            if (destinationNode.getFloorNumber() < originNode.getFloorNumber() && currentEdge.getDestination().getFloorNumber() > originNode.getFloorNumber() || currentEdge.getDestination().getFloorNumber() < destinationNode.getFloorNumber()) {
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
            if (currentEdge.getDestination().equals(destinationNode)) {
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
            recursiveExplore(originNode, destinationNode, currentEdge.getDestination(), visitedNodes, accessibility, candidateRoute, (candidateWeight + currentEdge.weight));

            // Remove current edge from candidate route to move back through recursive call chain and explore along the next edge.
            candidateRoute.remove(currentEdge);
        }

        // Remove current node from list of visited nodes when moving back through recursive call chain.
        // Move back up recursive call chain to explore next node.
        visitedNodes.remove(currentNode);
    }
}
