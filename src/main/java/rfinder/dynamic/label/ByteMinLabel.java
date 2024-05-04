package rfinder.dynamic.label;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ByteMinLabel extends Label{

    private byte cost = 0;

    public byte getCost() {
        return cost;
    }

    public void setCost(byte cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(@NotNull Label label) {
        if (getClass() != label.getClass())
            throw new RuntimeException("illegal comparison: " + getClass() + " and " + label.getClass());

        // lowest cost = highest fitness
        return -Byte.compare(cost, ((ByteMinLabel) label).cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteMinLabel that = (ByteMinLabel) o;
        return cost == that.cost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost);
    }

    @Override
    public String
    toString() {
        return String.valueOf(cost);
    }
}
