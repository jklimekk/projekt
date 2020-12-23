package Observer;

import MapElements.AbstractMapElement;
import Other.Vector2d;

// wzorzec Observer: obiekt - obserwator obecności elementu na mapie
public interface IElementPresenceObserver {

    void placed(AbstractMapElement element, Vector2d position);

    void removed(AbstractMapElement element, Vector2d position);
}
