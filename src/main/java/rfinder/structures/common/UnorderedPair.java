package rfinder.structures.common;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

public record UnorderedPair<T>(T first, T second) implements Iterable<T> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnorderedPair<?> that = (UnorderedPair<?>) o;
        return (Objects.equals(first, that.first) && Objects.equals(second, that.second)) ||
                (Objects.equals(first, that.second) && Objects.equals(second, that.first));
    }


    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }


    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private byte i = 0;

            @Override
            public boolean hasNext() {
                return i < 2;
            }

            @Override
            public T next() {
                return switch (i) {
                    case 0 -> {
                        i++;
                        yield first;
                    }
                    case 1 -> {
                        i++;
                        yield second;
                    }
                    default -> throw new IllegalStateException();
                };
            }
        };

    }
}