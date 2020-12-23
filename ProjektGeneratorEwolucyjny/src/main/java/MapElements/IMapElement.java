package MapElements;

import Other.Vector2d;
import javafx.scene.image.Image;

// interfejs - elementy znajdujące się na mapie
public interface IMapElement {

    Vector2d getPosition();

    @Override
    boolean equals(Object other);

    @Override
    int hashCode();

    ElementImage getImage();

    // enum przechowujący obrazy do wizualizacji elementów na mapie
    enum ElementImage {

        GROUND("dirt.jpg"),
        GENOTYPE("genotype.png"),
        STRONG_ANIMAL("strong animal.png"),
        NORMAL_ANIMAL("normal animal.png"),
        WEAK_ANIMAL("weak animal.png"),
        ALMOST_DEAD_ANIMAL("almost dead animal.png"),
        GRASS("grass.png");

        public Image image;

        ElementImage(String path) {
            image = new Image(path);
        }
    }
}
