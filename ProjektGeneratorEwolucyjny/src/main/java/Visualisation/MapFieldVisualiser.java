package Visualisation;

import MapElements.IMapElement;
import Observer.IClickedObserver;
import Observer.IClickedPublisher;
import Other.Vector2d;
import javafx.scene.image.ImageView;

import java.util.LinkedList;

// wizualizacja poszczególnych pól mapy
public class MapFieldVisualiser extends ImageView implements IClickedPublisher {
    private final IMapElement.ElementImage base = IMapElement.ElementImage.GROUND;

    // pozycja reprezentowanego pola mapy
    private final Vector2d position;

    // lista obserwatorów
    private final LinkedList<IClickedObserver> observers = new LinkedList<>();

    // konstruktor
    public MapFieldVisualiser(double length, int x, int y) {
        setFitWidth(length);
        setFitHeight(length);
        setImage(base.image);
        position = new Vector2d(x, y);

        // informowanie obserwatorów o kliknięciu pola
        setOnMouseClicked(event -> {
            for(IClickedObserver observer : observers) {
                observer.clicked(position);
            }
        });
    }

    // funckja uaktualniająca wizualizację pola w zależności od obecnego na nim obiektu
    public void updateField(IMapElement element) {
        if(element == null) {
            setImage(base.image);
        } else {
            setImage(element.getImage().image);
        }
    }

    // funckja uaktualniająca dane pole na wskazujące zwierze z najpopularniejszym genotypem
    public void updateFieldToMostPopularGenotype() {
        setImage(IMapElement.ElementImage.GENOTYPE.image);
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IClickedPublisher

    // funkcja dodająca do listy nowego obserwatora
    @Override
    public void addObserver(IClickedObserver observer) {
        observers.add(observer);
    }

    // funkcja usuwająca z listy obserwatora
    @Override
    public void removeObserver(IClickedObserver observer) {
        observers.add(observer);
    }
}