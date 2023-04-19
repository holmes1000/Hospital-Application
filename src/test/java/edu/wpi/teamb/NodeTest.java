package edu.wpi.teamb;
import org.junit.jupiter.api.Test;
import edu.wpi.teamb.DBAccess.ORMs.Node;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class NodeTest {

    @Test
    public void nodeGetterAndSetterTest (){
        Node myNode = new Node();
        myNode.setNodeID(100);
        myNode.setxCoord(2265);
        myNode.setyCoord(904);
        myNode.setFloor("L1");
        myNode.setBuilding("45 Francis");



        assertEquals(myNode.getNodeID(), 100);
        assertEquals(myNode.getxCoord(), 2265);
        assertEquals(myNode.getyCoord(), 904);
        assertEquals(myNode.getFloor(), "L1");
        assertEquals(myNode.getBuilding(), "45 Francis");
    }


}
