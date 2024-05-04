package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;

import java.util.EnumSet;

public class RelaxedParetoBag extends ParetoBag{

    private int nondominating;

    public RelaxedParetoBag(EnumSet<ECriteria> criteria, int nondominating){
        super(criteria);
        this.nondominating = nondominating;
    }

    @Override
    public boolean addEliminate(Multilabel multilabel) {
        if(size() < nondominating){
            multilabels.add(multilabel);
            return true;
        }

        return super.addEliminate(multilabel);
    }
}
