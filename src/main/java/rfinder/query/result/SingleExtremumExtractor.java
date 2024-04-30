/*
package rfinder.query.result;

import rfinder.dynamic.*;
import rfinder.dynamic.label.ParetoMultilabel;

import java.util.List;

import static rfinder.query.result.ExtractorHelper.extractSolution;

public class SingleExtremumExtractor implements ResultExtractor {

    private final ECriteria criteria;

    public SingleExtremumExtractor(ECriteria criteria){
        this.criteria = criteria;
    }

    @Override
    public List<QuerySolution> extract(NetworkQueryContext queryContext) {
        ParetoMultilabel multilabel = queryContext.finalBag().bestBy(criteria).orElseThrow();

        return List.of(extractSolution(queryContext, multilabel));
    }
}
*/
