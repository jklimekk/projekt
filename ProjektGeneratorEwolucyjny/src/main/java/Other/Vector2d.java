package Other;

import java.util.Objects;

// klasa reprezentująca pozycję obietu na mapie
public class Vector2d {
    final public int x;
    final public int y;

    // konstruktor
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // funkcje sprawdzające który wektor leży bliżej początku układu współrzędnych

    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    // funkcje zwracające punkty będące odpowiednimi wierzchołkami prostokąta popartego na podanych położeniach

    public Vector2d upperRight(Vector2d other) {
        int x, y;

        x = Math.max(this.x, other.x);
        y = Math.max(this.y, other.y);

        return new Vector2d(x,y);
    }

    public Vector2d lowerLeft(Vector2d other) {
        int x, y;

        x = Math.min(this.x, other.x);
        y = Math.min(this.y, other.y);

        return new Vector2d(x,y);
    }

    // funkjce zwracające wektory będącymi wynikami operacji

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    // funkcje umożliwiające porównywanie wektorów ze sobą

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;

        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}