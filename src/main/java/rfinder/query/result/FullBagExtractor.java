package rfinder.query.result;

import rfinder.dynamic.label.Multilabel;
import rfinder.dynamic.NetworkQueryContext;

import java.util.ArrayList;
import java.util.List;

import static rfinder.query.result.ExtractorHelper.extractSolution;

public class FullBagExtractor implements ResultExtractor {
    @Override
    public List<QuerySolution> extract(NetworkQueryContext queryContext) {
        List<QuerySolution> solutions = new ArrayList<>();

        for (Multilabel multilabel : queryContext.finalBag()){
            solutions.add(extractSolution(queryContext, multilabel));
        }

        return solutions;
    }
}
