package rfinder.query.result;

import rfinder.dynamic.ECriteria;
import rfinder.dynamic.Multilabel;


import java.util.LinkedList;
import java.util.List;

public class SingleExtremumExtractor implements ResultExtractor{

    private final ECriteria criteria;

    public SingleExtremumExtractor(ECriteria criteria){
        this.criteria = criteria;
    }

    @Override
    public QueryResult extract(Multilabel multilabel) {
        List<PathElement> elements = new LinkedList<>();

        return null;
    }
}
