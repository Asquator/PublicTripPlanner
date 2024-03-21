package rfinder.dynamic;

import java.time.Duration;

class DurationMinLabel extends Label {
    private Duration duration;
    private static final Duration INIT_DURATION = Duration.ofSeconds(0);

    public DurationMinLabel() {
        this.duration = INIT_DURATION;
    }

    public Duration getDuration() {
        return duration;
    }

    public void addDuration(Duration duration) {
        this.duration = this.duration.plus(duration);
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
    public String toString() {
        return "DurationMinLabel{" +
                "duration=" + duration.toMinutes() + ":" + duration.toSecondsPart()+
                '}';
    }
}
