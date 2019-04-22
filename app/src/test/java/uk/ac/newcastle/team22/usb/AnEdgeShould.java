package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import java.util.ArrayList;

import uk.ac.newcastle.team22.usb.coreUSB.USBManager;
import uk.ac.newcastle.team22.usb.navigation.Edge;
import uk.ac.newcastle.team22.usb.navigation.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test class for {@link Edge}.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class AnEdgeShould {
    @Test
    public void haveAnOrigin() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Edge testEdge = new Edge(testNode, 1, 0, new ArrayList<Integer>(), new ArrayList<Double>(), false, true);
        assertEquals(testNode, testEdge.getOrigin());
    }

    @Test
    public void haveADestination() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Node testDestinationNode = new Node(3,2, new ArrayList<Edge>());
        Edge testEdge = new Edge(testNode, 3, 0, new ArrayList<Integer>(), new ArrayList<Double>(), false, true);

        USBManager.shared.getBuilding().getNavigationNodes().put(3, testDestinationNode);
        assertEquals(testDestinationNode, testEdge.getDestination());
    }

    @Test
    public void haveAWeight() {
        int testWeight = 5;
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Edge testEdge = new Edge(testNode, 1, 0, new ArrayList<Integer>(), new ArrayList<Double>(), false, true);
        assertEquals(testWeight, testEdge.weight, 0);
    }

    @Test
    public void testEquality() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Edge testEdge = new Edge(testNode, 1, 0, new ArrayList<Integer>(), new ArrayList<Double>(), false, true);
        Edge testEdge2 = new Edge(testNode, 1, 0, new ArrayList<Integer>(), new ArrayList<Double>(), false, true);
        assertTrue(testEdge.equals(testEdge2));
    }
}
