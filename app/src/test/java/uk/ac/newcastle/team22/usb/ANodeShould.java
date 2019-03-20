package uk.ac.newcastle.team22.usb;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import uk.ac.newcastle.team22.usb.navigation.Direction;
import uk.ac.newcastle.team22.usb.navigation.Edge;
import uk.ac.newcastle.team22.usb.navigation.Node;

/**
 * A test class for {@link Node}.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class ANodeShould {

    @Test
    public void haveANodeIdentifier() {
        Node testNode = new Node(10, 2, new ArrayList<Edge>() {
        });

        assertEquals(10, testNode.getNodeIdentifier());
    }

    @Test
    public void haveAFloorNumber() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());

        assertEquals(2, testNode.getFloorNumber());
    }

//    @Test
//    public void haveEdges() {
//        Edge edge1 = new Edge(new Node(0, 0, new ArrayList<Edge>()), 1, 2, new ArrayList<Direction>(), new ArrayList<Integer>(), false, true);
//        Edge edge2 = new Edge(new Node(1, 0, new ArrayList<Edge>()), 0, 2, new ArrayList<Direction>(), new ArrayList<Integer>(), false, true);
//        List<Edge> testEdges = new ArrayList<>();
//        testEdges.add(edge1);
//        testEdges.add(edge2);
//
//        Node testNode = new Node(10,2, testEdges);
//
//        assertEquals(testEdges, testNode.getEdges());
//    }

    @Test
    public void testEquality() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Node testNode2 = new Node(10,2, new ArrayList<Edge>());

        assertTrue(testNode.equals(testNode2));
    }
}