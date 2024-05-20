package rfinder.query.result;

import rfinder.dynamic.label.Multilabel;
import rfinder.structures.links.LabeledLink;
import rfinder.structures.nodes.PathNode;

import java.util.ArrayList;
import java.util.List;

public class ExtractorHelper {

    public static QuerySolution extractSolution(NetworkQueryContext queryContext, Multilabel label) {

        PathNode currentNode, previousNode;
        TerminalPathElement previousNodeElement;

        currentNode = queryContext.queryContext().queryGraphInfo().destinationRepr();

        Multilabel currentLabel = label;
        LabeledLink link = currentLabel.getBackwardLink();
        List<PathElement> elements = new ArrayList<>();

        // push last node
        TerminalPathElement destElement = currentNode.toElement();
        elements.addFirst(destElement);

        while (link != null){
            previousNode = link.target();
            previousNodeElement = previousNode.toElement();

            // convert link element to segment with shape
            PathSegment segment = link.toSegment(currentNode, currentLabel, queryContext);

            // push segment and the next node
            elements.addFirst(segment);
            elements.addFirst(previousNodeElement);

            // advance one step
            currentNode = previousNode;
            currentLabel = link.targetLabel();

            link = currentLabel.getBackwardLink();
        }

        ((TerminalPathElement)elements.getFirst()).setName("SOURCE");
        ((TerminalPathElement)elements.getLast()).setName("DESTINATION");

        return new QuerySolution(elements, label);
    }

}
