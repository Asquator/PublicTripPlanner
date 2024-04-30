package rfinder.dynamic.label;


public abstract class Label<T> implements Comparable<Label>, Cloneable {

    @Override
    @SuppressWarnings("unchecked")
    public Label<T> clone() throws CloneNotSupportedException {
        return (Label<T>) super.clone();
    }

}