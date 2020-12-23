package Map;

import Other.MapDirection;
import Other.Vector2d;

import java.util.ArrayList;
import java.util.Random;

// klasa reprezentująca prostokątny teren
public class RectangleArea {

    // pozycje przeciwległych wierzchołków prostokątnego terenu
    private final Vector2d lowerLeft;
    private final Vector2d upperRight;

    // generator do losowań
    private static final Random generator = new Random();

    // konstruktor
    public RectangleArea(Vector2d lowerLeft, Vector2d upperRight) {
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
    }

    // funkcje zwracające wymiary terenu

    public int getWidth() {
        return upperRight.x - lowerLeft.x + 1;
    }

    public int getHeight() {
        return upperRight.y - lowerLeft.y + 1;
    }

    public int getSize() {
        return getWidth() * getHeight();
    }

    // funkcja sprawdzająca czy zadana pozycja znajduje się w granicach terenu
    public boolean insideArea(Vector2d position) {
        return position.follows(lowerLeft) && position.precedes(upperRight);
    }

    // funkcja zwracająca losową pozycję zawierającą się w granicach terenu
    public Vector2d generateVectorInside() {
        return new Vector2d(generator.nextInt(getWidth()) + lowerLeft.x, generator.nextInt(getHeight()) + lowerLeft.y);
    }

    // funkcja generująca pozycje dookoła zadanej
    public ArrayList<Vector2d> generateVectorsAround(Vector2d v) {
        ArrayList<Vector2d> positions = new ArrayList<>();
        ArrayList<Vector2d> unitVectors = new ArrayList<>();

        for(MapDirection direction : MapDirection.values())
            unitVectors.add(direction.toUnitVector());

        for(Vector2d u : unitVectors)
            positions.add(correctPosition(v.add(u)));

        return positions;
    }

    // funkcja korygująca pozycję jeśli wystaje poza mapę
    public Vector2d correctPosition(Vector2d v) {
        if(v.follows(lowerLeft) && v.precedes(upperRight)) {
            return v;
        } else {
            int x = (getWidth() + (v.x % getWidth())) % getWidth();
            int y = (getHeight() + (v.y % getHeight())) % getHeight();
            return new Vector2d(x, y);
        }
    }

    // funkcja generująca nowy wydzielony teren wewnątrz obecnego terenu na bazie podanego stosunku ich wielkości
    public RectangleArea generateAreaInside(double ratio) {
        int width = (int) (this.getWidth() * ratio);
        int height = (int) (this.getHeight() * ratio);

        Vector2d newLowerLeft = new Vector2d((this.getWidth() - width)/2, (this.getHeight() - height)/2);
        Vector2d newUpperRight = newLowerLeft.add(new Vector2d(width, height));

        return new RectangleArea(newLowerLeft, newUpperRight);
    }
}