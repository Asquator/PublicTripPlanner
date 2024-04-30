package rfinder.aux;

import java.time.Duration;

public final class Converters {
    private static final int kmh = 5;
    private static final double kmm = (double) kmh / 60;

    public static Duration kmToDuration(double km){
        long ceil = (long) Math.ceil(km / kmm);
        Duration duration = Duration.ofMinutes(ceil);
        return duration;
    }
}
