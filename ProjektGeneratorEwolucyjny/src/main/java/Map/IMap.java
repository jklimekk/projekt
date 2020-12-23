package Map;

import MapElements.AbstractMapElement;
import MapElements.Animal;
import MapElements.IMapElement;
import Other.Vector2d;

import java.util.LinkedList;

// interfejs - mapa, na której znajdują się elementy
public interface IMap {

    int getDay();

    void place(Animal animal);

    void remove(AbstractMapElement element);

    LinkedList<Animal> animalsOnMap();

    Animal animalAtPosition(Vector2d position);

    Vector2d generatePosition();

    Vector2d correctPosition(Vector2d v);

    void day();

    boolean isOccupied(Vector2d position);

    IMapElement occupiedBy(Vector2d position);
}
