package rfinder.dynamic;

import rfinder.dynamic.label.Multilabel;
import rfinder.dynamic.label.MultilabelBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RoundNodeContext<B extends MultilabelBag> {

    private final rfinder.structures.nodes.PathNode pathNode;
    private List<Multilabel> lastRoundList;
    private List<Multilabel> currentRoundList;
    private List<Multilabel> lastIsolatedList;
    private List<Multilabel> currentIsolatedList;
    private final B bestBag;


    public RoundNodeContext(rfinder.structures.nodes.PathNode node, Supplier<B> bagSupplier){
        this.pathNode = node;
        lastRoundList = new ArrayList<>();
        currentRoundList = new ArrayList<>();
        lastIsolatedList = new ArrayList<>();
        currentIsolatedList = new ArrayList<>();
        bestBag = bagSupplier.get();
    }


    public rfinder.structures.nodes.PathNode getPathNode() {
        return pathNode;
    }

    public List<Multilabel> getCurrentIsolatedList() {
        return currentIsolatedList;
    }

    public List<Multilabel> getCurrentRoundList() {
        return currentRoundList;
    }

    public List<Multilabel> getLastIsolatedList() {
        return lastIsolatedList;
    }

    public List<Multilabel> getLastRoundList() {
        return lastRoundList;
    }

    public B getBestBag() {
        return bestBag;
    }

    public boolean addToCurrentRoundBag(Multilabel multilabel){
        if(!bestBag.addEliminate(multilabel))
            return false;

        currentRoundList.add(multilabel);
        return true;
    }

    public boolean addToCurrentIsolatedBag(Multilabel multilabel){
        if(!bestBag.addEliminate(multilabel))
            return false;

        currentIsolatedList.add(multilabel);
        return true;
    }

    @SuppressWarnings("unchecked")
    public void step(Supplier<? extends MultilabelBag> bagSupplier){
        lastRoundList = currentRoundList;
        lastIsolatedList = currentIsolatedList;
        currentRoundList = new ArrayList<>();
        currentIsolatedList = new ArrayList<>();
    }

}
