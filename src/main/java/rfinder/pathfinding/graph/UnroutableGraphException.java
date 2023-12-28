package rfinder.pathfinding.graph;

public class UnroutableGraphException extends IllegalStateException{
    public UnroutableGraphException() {
        super("not a routable graph");
    }

    public UnroutableGraphException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnroutableGraphException(Throwable cause) {
        super(cause);
    }
}
