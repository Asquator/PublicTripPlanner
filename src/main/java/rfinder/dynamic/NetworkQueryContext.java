package rfinder.dynamic;

import rfinder.dao.RouteDAO;
import rfinder.dynamic.label.MultilabelBag;
import rfinder.query.QueryContext;

public record NetworkQueryContext(QueryContext queryContext, RouteDAO routeDAO, MultilabelBag finalBag) {

}
