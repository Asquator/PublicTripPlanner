package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.*;


public class ExtendedRoutableGraph<T extends GraphNode, L extends RouteLink<T>> implements RoutableGraph<T, L> {

    private HashMap<T, Set<L>> newLinks = new HashMap<>();
    private final RoutableGraph<T, L> originalGraph;

    public ExtendedRoutableGraph(RoutableGraph<T, L> originalGraph) {
        this.originalGraph = originalGraph;
    }


    public void addLink(T source, L link){
        if(!newLinks.containsKey(source))
            newLinks.put(source, new HashSet<>());

        newLinks.get(source).add(link);
    }


    @Override
    public Set<L> getLinks(T node) {
        Set<L> links = originalGraph.getLinks(node);

        if(links == null)
            links = new HashSet<>();

        else
            links = new HashSet<>(links);

        if(newLinks.containsKey(node))
            links.addAll(newLinks.get(node));

        return links;
    }
}

