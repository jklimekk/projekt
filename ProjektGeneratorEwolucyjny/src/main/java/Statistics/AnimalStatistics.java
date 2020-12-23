package Statistics;

import MapElements.Animal;
import MapElements.Genotype;
import Other.Vector2d;

// klasa przechowująca statystyki dla danego zwierzęcia
public class AnimalStatistics {
    /*
        Po zatrzymaniu programu można wskazać pojedyncze zwierzę, w celu śledzenia jego historii:
        - określenia liczby wszystkich dzieci, po n-epokach,
        - określenia liczby wszystkich potomków, po n-epokach,
        - określenia epoki, w której zmarło,
     */

    private final Animal animal;

    // podstawowe statystyki zwierzęcia
    private int children;
    private int offspring;
    private int whenDied;

    // konstruktor
    public AnimalStatistics(Animal animal) {
        this.animal = animal;

        children = 0;
        offspring = 0;
        whenDied = -1;
    }

    // gettery statystyk i charakterystyk zwierzęcia

    public Genotype getGenotype() {
        return animal.getGenotype();
    }

    public Vector2d getPosition() {
        return animal.getPosition();
    }

    public int getNumberOfChildren() {
        return children;
    }

    public int getNumberOfOffspring() {
        return offspring;
    }

    public int getWhenDied() {
        return whenDied;
    }

    // funkcja wywołująca aktualizowanie statystyk
    public void actualiseStatistics() {
        actualiseNumberOfChildren();
        actualiseNumberOfOffspring();
        checkIfDead();
    }

    // funkcje aktualizujące odpowiednie statystyki

    private void actualiseNumberOfChildren() {
        children = animal.getChildren().size();
    }

    private void actualiseNumberOfOffspring() {
        offspring = animal.getOffspring().size();
    }

    private void checkIfDead() {
        if(animal.getLifespan() > 0) {
            whenDied = animal.getLifespan();
        } else {
            whenDied = -1;
        }
    }
}
