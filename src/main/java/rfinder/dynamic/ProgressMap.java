package rfinder.dynamic;

import java.util.EnumMap;

public class ProgressMap<P> extends EnumMap<ECriteria, P> {
    public ProgressMap(Class<ECriteria> keyType) {
        super(keyType);
    }
}
