package rfinder.dynamic.label;

import java.time.Duration;
import java.util.Objects;

public class DurationMinLabel extends Label{
    private Duration duration;
    private static final Duration INIT_DURATION = Duration.ofSeconds(0);

    public DurationMinLabel() {
        this.duration = INIT_DURATION;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(Label label) {
        if(getClass() != label.getClass())
            throw new RuntimeException("illegal comparison: " + getClass() + " and " + label.getClass());

        // lowest duration = highest fitness
        return -duration.compareTo(((DurationMinLabel) label).duration);
    }

    @Override
    public Label clone() {
        try {
            DurationMinLabel label = (DurationMinLabel) super.clone();
            label.duration = Duration.ofSeconds(duration.getSeconds());
            return label;
        } catch (CloneNotSupportedException e){
            throw new AssertionError(e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DurationMinLabel that = (DurationMinLabel) o;
        return Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(duration);
    }

    @Override
    public String toString() {
        return Long.toString(duration.toMinutes());
    }
}
