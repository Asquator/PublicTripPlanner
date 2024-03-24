package rfinder.model.network.walking;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class ExtendedGraph<T extends GraphNode, L extends RouteLink<T>> implements RoutableGraph<T, L> {

    private HashMap<T, Set<L>> newLinks = new HashMap<>();
    private final RoutableGraph<T, L> originalGraph;

    public ExtendedGraph(RoutableGraph<T, L> originalGraph) {
        this.originalGraph = originalGraph;
    }

    public ExtendedGraph(RoutableGraph<T, L> originalGraph, HashMap<T, Set<L>> newLinks) {
        this.originalGraph = originalGraph;
        this.newLinks = newLinks;
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

