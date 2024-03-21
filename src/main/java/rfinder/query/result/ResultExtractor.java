package rfinder.query.result;

import rfinder.dynamic.Multilabel;

public interface ResultExtractor {
    QueryResult extract(Multilabel multilabel);
}
