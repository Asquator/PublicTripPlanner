package rfinder.model.network.walking;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ExtendedGraph<T extends GraphNode> implements RoutableGraph<T> {

    private HashMap<T, Set<RouteLink<T>>> newLinks = new HashMap<>();
    private final RoutableGraph<T> originalGraph;

    public ExtendedGraph(RoutableGraph<T> originalGraph) {
        this.originalGraph = originalGraph;
    }

    public ExtendedGraph(RoutableGraph<T> originalGraph, HashMap<T, Set<RouteLink<T>>> newLinks) {
        this.originalGraph = originalGraph;
        this.newLinks = newLinks;
    }

    public void addLink(T source, RouteLink<T> link){
        if(!newLinks.containsKey(source))
            newLinks.put(source, new HashSet<>());

        newLinks.get(source).add(link);
    }

    @Override
    public Set<RouteLink<T>> getLinks(T node) {
        Set<RouteLink<T>> links = originalGraph.getLinks(node);

        if(links == null)
            links = new HashSet<>();

        else
            links = new HashSet<>(links);

        if(newLinks.containsKey(node))
            links.addAll(newLinks.get(node));

        return links;
    }
}

