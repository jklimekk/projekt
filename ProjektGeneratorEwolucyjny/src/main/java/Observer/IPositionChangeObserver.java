package Observer;

import MapElements.Animal;
import Other.Vector2d;

// wzorzec Observer: obiekt - obserwator zmiany pozycji
public interface IPositionChangeObserver {

    void positionChanged(Animal movedElement, Vector2d oldPosition, Vector2d newPosition);
}
