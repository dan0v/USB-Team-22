package uk.ac.newcastle.team22.usb;

import org.junit.Test;

import java.util.ArrayList;

import uk.ac.newcastle.team22.usb.navigation.Edge;
import uk.ac.newcastle.team22.usb.navigation.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A test class for {@link Node}.
 *
 * @author Daniel Vincent
 * @version 1.0
 */
public class ANodeShould {

    /** Assert that a node has an identifier. */
    @Test
    public void haveANodeIdentifier() {
        Node testNode = new Node(10, 2, new ArrayList<Edge>() {});
        assertEquals(10, testNode.getNodeIdentifier());
    }

    /** Assert that a node has a floor number. */
    @Test
    public void haveAFloorNumber() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        assertEquals(2, testNode.getFloorNumber());
    }

    /** Assert that a nodes have equality. */
    @Test
    public void testEquality() {
        Node testNode = new Node(10,2, new ArrayList<Edge>());
        Node testNode2 = new Node(10,2, new ArrayList<Edge>());
        assertTrue(testNode.equals(testNode2));
    }
}