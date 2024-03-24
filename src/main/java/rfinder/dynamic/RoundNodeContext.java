package rfinder.dynamic;

import java.util.Arrays;

public class RoundNodeContext {


    private final MultilabelBag[] roundLabels;
    private final rfinder.structures.nodes.PathNode PathNode;


    public rfinder.structures.nodes.PathNode getPathNode() {
        return PathNode;
    }

    public RoundNodeContext(rfinder.structures.nodes.PathNode node, int size){
        this.PathNode = node;
        roundLabels = new MultilabelBag[size];
    }

    public int size(){
        return roundLabels.length;
    }

    public MultilabelBag[] getRoundLabels() {
        return roundLabels;
    }

    @Override
    public String toString() {
        return "RoundLabeledStop{" +
                "roundLabels=" + Arrays.toString(roundLabels) +
                ", stopNode=" + PathNode +
                '}';
    }
}
