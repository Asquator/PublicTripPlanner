package rfinder.dynamic.label;

import org.jetbrains.annotations.NotNull;
import rfinder.structures.nodes.PathNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public abstract class MultilabelBag implements Iterable<Multilabel>, Cloneable{
    protected List<Multilabel> multilabels = new LinkedList<>();

    private PathNode pathNode;

    public MultilabelBag() {

    }


    public @NotNull Iterator<Multilabel> iterator() {
        return multilabels.iterator();
    }

    public Stream<Multilabel> stream(){
        return multilabels.stream();
    }

    public abstract boolean addEliminate(Multilabel multilabel);



    public int size(){
        return multilabels.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MultilabelBag clone() {
        try {
            MultilabelBag clone = (MultilabelBag) super.clone();
            clone.multilabels = new LinkedList<>(multilabels);
            clone.pathNode = pathNode;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
