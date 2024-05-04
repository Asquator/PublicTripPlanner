package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;

import java.util.*;

public class ParetoBag extends MultilabelBag {

    private final EnumSet<ECriteria> criteria;

    public ParetoBag(EnumSet<ECriteria> criteria){
        super();
        this.criteria = criteria;
    }

    public boolean addEliminate(Multilabel multilabel){
        ListIterator<Multilabel> iter = multilabels.listIterator();

        while (iter.hasNext()){
            Multilabel current = iter.next();

            if(current.paretoDominates(multilabel, criteria)){
                return false;
            }

            else if(multilabel.paretoDominates(current, criteria)){
                iter.remove();
            }
        }

        multilabels.add(multilabel);
        return true;
    }
}
