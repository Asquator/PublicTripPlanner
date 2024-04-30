package rfinder.query.result;

import rfinder.dynamic.NetworkQueryContext;

import java.util.List;

public interface ResultExtractor {
    List<QuerySolution> extract(NetworkQueryContext queryContext);
}
