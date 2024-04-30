package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;
import rfinder.structures.links.LabeledLink;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Multilabel  {
    protected Label[] labels;
    private static final ECriteria[] enumVals = ECriteria.values();


    /**
     * Construct an initial label bag with all values set to zero (minimal)
     */
    public Multilabel() {
        labels = new Label[enumVals.length];

        for (int i = 0; i < labels.length; i++) {
            try {
                labels[i] = enumVals[i].getLabelClass().newInstance();
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public Multilabel(Multilabel other){
        labels = new Label[other.labels.length];

        try {
            for (int i = 0; i < labels.length; i++) {
                labels[i] = other.labels[i].clone();
            }
        } catch (CloneNotSupportedException e){
            throw new AssertionError(e);
        }

        backwardLink = other.backwardLink;
    }

    private LabeledLink backwardLink;

    public LabeledLink getBackwardLink() {
        return backwardLink;
    }

    public void setBackwardLink(LabeledLink backwardLink) {
        this.backwardLink = backwardLink;
    }

    public LocalDateTime getArrivalTime(){
        return ((TimeMinLabel)labels[0]).getTimestamp();
    }

    public void setArrivalTime(LocalDateTime timestamp){
        ((TimeMinLabel)labels[0]).setTimestamp(timestamp);
    }

    public Label getLabel(ECriteria type) {
        return labels[type.ordinal()];
    }

    public Label[] getLabels() {
        return labels;
    }

    public <T> void update(LabelUpdatePolicy<T> policy, T value){
        policy.update(this, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multilabel that = (Multilabel) o;
        return Arrays.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(backwardLink);
        result = 31 * result + Arrays.hashCode(labels);
        return result;
    }

    @Override
    public String toString() {
        return "Multilabel{" +
                "labels=" + Arrays.toString(labels) +
                ", backwardLink=" + backwardLink +
                '}';
    }

    public boolean paretoDominates(Multilabel other){
        boolean dominates = false;
        int res;
        for (int i = 0; i < labels.length; i++) {

            // this dominates other iff labels[i] >= other.labels[i] for all i and labels[i] > other.labels[i] for at least one i
            res = labels[i].compareTo(other.labels[i]);
            if(res >= 0)
                dominates = true;

            if(res < 0)
                return false;
        }

        return dominates;
    }

}
