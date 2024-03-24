package rfinder.query.result;

import rfinder.dynamic.MultilabelBag;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.structures.nodes.PathNode;

import java.util.Map;

public interface ExtractionPolicy {
    QueryResult extract(NetworkQueryContext queryContext);
}
