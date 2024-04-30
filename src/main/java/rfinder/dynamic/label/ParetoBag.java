package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;

import java.util.*;

public class ParetoBag extends MultilabelBag {

    public ParetoBag(){
        super();
    }

    public ParetoBag(MultilabelBag other){
        super(other);
    }

    public boolean addEliminateAll(MultilabelBag bag){
        boolean improved = false;
        for(Multilabel label : bag)
            improved = addEliminate(label) || improved;

        return improved;
    }


    public boolean addEliminate(Multilabel multilabel){
        ListIterator<Multilabel> iter = multilabels.listIterator();

        while (iter.hasNext()){
            Multilabel current = iter.next();

            if(current.paretoDominates(multilabel)){
                return false;
            }

            else if(multilabel.paretoDominates(current)){
                iter.remove();
            }
        }

        multilabels.add(multilabel);
        return true;
    }

    public boolean wouldBeEliminated(Multilabel multilabel){

        for (Multilabel current : multilabels)
            if (current.paretoDominates(multilabel))
                return true;

        return false;
    }

/*    public boolean mergeRouteBag(MultilabelBag routeBag, int stopSequence){
        boolean improved = false;

        for(Multilabel multilabel : routeBag) {
            Multilabel cloned = new Multilabel(multilabel);
            ((RideLink)cloned.getBackwardLink()).setDestSequence(stopSequence);
            improved = addEliminate(cloned) || improved;
        }

        return improved;
    }*/

    public Optional<Multilabel> bestBy(ECriteria criteria){
        return multilabels.stream().max(Comparator.comparing(label -> label.getLabel(criteria)));
    }
    
    @Override
    public String toString() {
        return "MultilabelBag{" +
                "multilabels=" + multilabels +
                '}';
    }
}
