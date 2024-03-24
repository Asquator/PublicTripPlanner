package rfinder.query.result;

import javafx.scene.paint.Stop;
import rfinder.dao.RouteDAO;
import rfinder.dynamic.ECriteria;
import rfinder.dynamic.Multilabel;
import rfinder.dynamic.MultilabelBag;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.structures.common.Location;
import rfinder.structures.components.PathLink;
import rfinder.structures.components.RideLink;
import rfinder.structures.components.WalkLink;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;


import java.util.LinkedList;
import java.util.List;

public class SingleExtremumExtractor implements ResultExtractor {

    private final ECriteria criteria;
    private final RouteDAO dao;

    public SingleExtremumExtractor(RouteDAO dao, ECriteria criteria){
        this.criteria = criteria;
        this.dao = dao;
    }

    @Override
    public QueryResult extract(NetworkQueryContext queryContext) {
        List<PathElement> elements = new LinkedList<>();
        List<Location> shape;

        Multilabel bestLabel;
        PathNode currentNode, previousNode;
        NominalPathElement currentNodeElement, previoudNodeElement;
        PathElement element;
        PathLink link;

        MultilabelBag currentBag = queryContext.finalBag();
        currentNode = queryContext.pathFinder().getDestinationRepr();
        currentNodeElement = currentNode.toElement();

        // push last node
        elements.addFirst(currentNodeElement);

        // get link to previous node while maximizing criteria
        link = currentBag.bestBy(criteria).orElseThrow().getBackwardLink();

        while (link != null){

            previousNode = link.target();
            previoudNodeElement = previousNode.toElement();

            // convert link element to shape
            if(link instanceof RideLink rideLink){
                shape = dao.getShapeAlongRoute(rideLink.getRouteID(), previousNode.getLocation(), currentNode.getLocation());
                element = new RideSegment((StopView) previoudNodeElement, (StopView) currentNodeElement,
                        rideLink.getRouteID(), rideLink.getSourceSequence(), shape);
            }

            else if(link instanceof WalkLink){
                shape = queryContext.pathFinder().findPath(previousNode, currentNode).getShape();
                element = new WalkSegment(previoudNodeElement, currentNodeElement, shape);
            }

            else
                throw new AssertionError("unknown link type");

            // push link element
            elements.addFirst(element);

            // update bag
            currentBag = queryContext.labels().getLabelBag(previousNode);
            currentNode = previousNode;
            link = currentBag.bestBy(criteria).orElseThrow().getBackwardLink();
        }

        // push first node
        elements.addFirst(currentNode.toElement());

        return new QueryResult(elements);

    }



}
