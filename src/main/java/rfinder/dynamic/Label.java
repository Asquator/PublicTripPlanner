package rfinder.dynamic;


import rfinder.structures.components.PathLink;

public abstract class Label implements Comparable<Label>, Cloneable {

    @Override
    public Label clone() throws CloneNotSupportedException {
        return (Label) super.clone();
    }
}