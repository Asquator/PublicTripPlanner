package rfinder.query.result;

import rfinder.dynamic.label.Multilabel;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.structures.links.LabeledLink;
import rfinder.structures.nodes.PathNode;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExtractorHelper {

    public static QuerySolution extractSolution(NetworkQueryContext queryContext, Multilabel headLabel) {

        PathNode currentNode, previousNode;
        NominalPathElement currentNodeElement, previousNodeElement;

        currentNode = queryContext.queryContext().queryGraphInfo().destinationRepr();
        currentNodeElement = currentNode.toElement();

        Multilabel currentLabel = headLabel;
        LabeledLink link = currentLabel.getBackwardLink();
        List<PathElement> elements = new ArrayList<>();

        // push last node
        elements.addFirst(currentNodeElement);

        // get link to previous node while maximizing criteria

        while (link != null){
            previousNode = link.target();
            previousNodeElement = previousNode.toElement();

            // convert link element to shape
            elements.addFirst(link.toSegment(currentNode, currentLabel, queryContext));
            elements.addFirst(previousNodeElement);

            currentNode = previousNode;
            currentLabel = link.targetLabel();
            link = currentLabel.getBackwardLink();
        }

        ((NominalPathElement)elements.getFirst()).setName("SOURCE");
        ((NominalPathElement)elements.getLast()).setName("DESTINATION");

        QuerySolution solution = new QuerySolution(elements, headLabel);

        return solution;
    }


    private static void normalizeSolution(QuerySolution solution) {

        Duration totalDuration = Duration.ZERO;
        double totalKm = 0;

        for (Iterator<PathElement> iter = solution.elements().iterator(); iter.hasNext(); ) {
            PathElement element = iter.next();

            while (element instanceof WalkSegment segment && iter.hasNext()) {
                totalDuration = totalDuration.plus(segment.getDuration());
                totalKm += segment.getKm();

                // skip node
                iter.next();

                // retrieve next segment
                element = iter.next();
            }

            totalDuration = Duration.ZERO;
            totalKm = 0;

            iter.next();

        }
    }

}
