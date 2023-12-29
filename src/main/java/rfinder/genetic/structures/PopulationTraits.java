package rfinder.genetic.structures;

public record PopulationTraits(int maxSize,
                               int maxTransfers,
                               float transferChance) {
    public PopulationTraits {
        if(maxTransfers < 0)
            maxTransfers = 0;
    }
}
