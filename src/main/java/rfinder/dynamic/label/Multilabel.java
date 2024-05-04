package rfinder.dynamic.label;

import rfinder.dynamic.ECriteria;
import rfinder.structures.links.LabeledLink;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumSet;

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

            if(other.backwardLink != null)
                backwardLink = other.backwardLink.clone();

        } catch (CloneNotSupportedException e){
            throw new AssertionError(e);
        }
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


    @Override
    public String toString() {
        return "Multilabel{" +
                "labels=" + Arrays.toString(labels) +
                ", backwardLink=" + backwardLink +
                '}';
    }

    public boolean paretoDominates(Multilabel other, EnumSet<ECriteria> criteria){
        boolean dominates = true;
        int res;
        for (ECriteria param : criteria) {

            // this dominates other iff labels[i] >= other.labels[i] for all i and labels[i] > other.labels[i] for at least one i
            res = labels[param.ordinal()].compareTo(other.labels[param.ordinal()]);


            if(res < 0)
                return false;
        }

        return dominates;
    }

}
