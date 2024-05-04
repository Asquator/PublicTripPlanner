package rfinder.dynamic.label;


public abstract class Label implements Comparable<Label>, Cloneable {

    @Override
    public Label clone() throws CloneNotSupportedException {
        return (Label) super.clone();
    }

}