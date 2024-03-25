package rfinder.dynamic;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeMinLabel extends Label{

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");
    private OffsetDateTime timestamp;

    public TimeMinLabel() {
        this.timestamp = OffsetDateTime.now();
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void add(Duration duration){
        timestamp = timestamp.plus(duration);
    }

    @Override
    public int compareTo(Label label) {
        if(getClass() != label.getClass())
            throw new RuntimeException("illegal comparison: " + getClass() + " and " + label.getClass());

        return -timestamp.compareTo(((TimeMinLabel) label).timestamp);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(DEFAULT_ZONE);
        return "TimeMinLabel{" +
                "timestamp=" + formatter.format(timestamp) +
                '}';
    }

}
