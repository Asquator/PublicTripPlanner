package rfinder.query.result;

import rfinder.dynamic.label.Multilabel;

import java.util.List;
public final class QuerySolution {

    private final List<PathElement> elements;

    private final Multilabel finalMultilabel;

    public QuerySolution(List<PathElement> elements, Multilabel finalMultilabel) {

        this.elements = elements;
        this.finalMultilabel = finalMultilabel;
    }

    public Multilabel getFinalLabel() {
        return finalMultilabel;
    }


    public List<PathElement> elements() {
        return elements;
    }

    @Override
    public String toString() {
        return "QuerySolution[" +
                "elements=" + elements + ']';
    }

}
