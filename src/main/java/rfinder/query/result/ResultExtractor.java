package rfinder.query.result;

import rfinder.dynamic.NetworkQueryContext;

public interface ResultExtractor {
    QueryResult extract(NetworkQueryContext queryContext);
}
