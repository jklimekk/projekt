package MapElements;

import Other.Vector2d;

// klasa abstrakcyjna elementów znajdujących się na mapie
public abstract class AbstractMapElement implements IMapElement {

    // pozycja elementu na mapie
    protected Vector2d position;

    // funkcja zwracająca pozycję
    @Override
    public Vector2d getPosition() {
        return position;
    }

    // funkcja do wizualizacji elementu
    @Override
    public ElementImage getImage() {
        return ElementImage.GRASS;
    }
}
