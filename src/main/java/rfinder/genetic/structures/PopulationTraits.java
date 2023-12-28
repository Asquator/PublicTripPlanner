package rfinder.genetic.structures;

public record PopulationTraits(int size, int maxTransfers) {
    public PopulationTraits {
        if(maxTransfers < 0)
            maxTransfers = 0;
    }
}
