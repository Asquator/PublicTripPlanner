package rfinder.query.result;

import java.util.List;

public interface ResultExtractor {
    List<QuerySolution> extract(NetworkQueryContext queryContext);
}
