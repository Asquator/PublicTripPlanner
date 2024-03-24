package rfinder.dynamic;

import java.util.*;

public class AbstractMultilabelBag<M extends Multilabel> implements Iterable<M>{

    // always sorted by the first label, starting from the worst
    private List<M> multilabels = new LinkedList<>();

    public AbstractMultilabelBag(){

    }

    public boolean isEmpty(){
        return multilabels.isEmpty();
    }

    public int size(){
        return multilabels.size();
    }

    @SuppressWarnings("unchecked")
    public AbstractMultilabelBag(AbstractMultilabelBag<M> other){
        multilabels = new LinkedList<>();
        try {
            for (M multilabel : other.multilabels) {
                multilabels.add((M) multilabel.clone());
            }
        } catch (CloneNotSupportedException e){
            throw new AssertionError(e);
        }
    }

    public Iterator<M> iterator(){
        return multilabels.iterator();
    }


    public boolean addAll(AbstractMultilabelBag<M> bag){
        boolean improved = false;
        for(M label : bag)
            improved = add(label) || improved;

        return improved;
    }

    public boolean add(M multilabel){
        ListIterator<M> iter = multilabels.listIterator();

        while (iter.hasNext()){
            M current = iter.next();
            if(current.dominates(multilabel)){
                return false;
            }

            else if(multilabel.dominates(current)){
                iter.remove();
            }
        }

        multilabels.add(multilabel);
        return true;
    }

    public Optional<M> bestBy(ECriteria criteria){
        return multilabels.stream().max(Comparator.comparing(label -> label.getLabel(criteria)));
    }
    
    @Override
    public String toString() {
        return "MultilabelBag{" +
                "multilabels=" + multilabels +
                '}';
    }
}
