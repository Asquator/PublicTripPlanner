package rfinder.dynamic;

import rfinder.structures.components.PathLink;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Arrays;

public class Multilabel implements Cloneable{
    private final Label[] labels;
    private static final ECriteria[] enumVals = ECriteria.values();

    private PathLink backwardLink;

    public PathLink getBackwardLink() {
        return backwardLink;
    }

    public void setBackwardLink(PathLink backwardLink) {
        this.backwardLink = backwardLink;
    }


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

    public OffsetDateTime getArrivalTime(){
        return ((TimeMinLabel)labels[0]).getTimestamp();
    }

    public void setArrivalTime(OffsetDateTime timestamp){
        ((TimeMinLabel)labels[0]).setTimestamp(timestamp);
    }

    public void addArrivalTime(Duration duration){
        ((TimeMinLabel)labels[0]).add(duration);
    }


    public Multilabel(Multilabel other) {
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

    public Multilabel clone() throws CloneNotSupportedException {
        return new Multilabel(this);
    }

    public boolean dominates(Multilabel other){
        for (int i = 0; i < labels.length; i++) {

            // this dominates other iff labels[i] > other.labels[i] for all i
            if(labels[i].compareTo(other.labels[i]) <= 0)
                return false;
        }
        
        return true;
    }

    public Label getLabel(ECriteria type) {
        return labels[type.ordinal()];
    }

    public Label[] getLabels() {
        return labels;
    }

    @Override
    public String toString() {
        return "Multilabel{" +
                "labels=" + Arrays.toString(labels) +
                ", backwardLink=" + backwardLink +
                '}';
    }
}
