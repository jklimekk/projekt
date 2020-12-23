package MapElements;

import Other.Vector2d;

import java.util.Objects;

// klasa reprezentująca trawę
public class Grass extends AbstractMapElement {

    // konstruktor
    public Grass(Vector2d position) {
        this.position = position;
    }

    // funkcje umożliwiające porównywanie traw ze sobą

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Grass))
            return false;
        Grass that = (Grass) other;

        return this.position.x == that.position.x && this.position.y == that.position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }
}
