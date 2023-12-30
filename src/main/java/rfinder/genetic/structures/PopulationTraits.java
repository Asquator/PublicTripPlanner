package rfinder.genetic.structures;

public record PopulationTraits(int maxSize,
                               int maxTransfers,
                               float transferChance,
                               double transferRadius) {
    public PopulationTraits {
        if(maxTransfers < 0)
            maxTransfers = 0;
    }
}
