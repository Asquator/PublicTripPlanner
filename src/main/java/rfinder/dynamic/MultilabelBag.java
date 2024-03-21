package rfinder.dynamic;

public class MultilabelBag extends AbstractMultilabelBag<Multilabel> {

    public MultilabelBag(){
        super();
    }

    public MultilabelBag(MultilabelBag other){
        super(other);
    }

    public boolean mergeRouteBag(RouteMultilabelBag routeBag){
        boolean improved = false;

        for(RouteMultilabel multilabel : routeBag)
            improved = add(new Multilabel(multilabel)) || improved;

        return improved;
    }
}
