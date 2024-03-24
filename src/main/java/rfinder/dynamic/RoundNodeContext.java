package rfinder.dynamic;

import rfinder.structures.nodes.StopNode;

import java.util.Arrays;

public class RoundStopContext {

    private final MultilabelBag[] roundLabels;
    private final StopNode stopNode;


    public StopNode getStopNode() {
        return stopNode;
    }

    public RoundStopContext(StopNode stopNode, int size){
        this.stopNode = stopNode;
        roundLabels = new MultilabelBag[size];
    }

    public MultilabelBag[] getRoundLabels() {
        return roundLabels;
    }

    @Override
    public String toString() {
        return "RoundLabeledStop{" +
                "roundLabels=" + Arrays.toString(roundLabels) +
                ", stopNode=" + stopNode +
                '}';
    }
}
