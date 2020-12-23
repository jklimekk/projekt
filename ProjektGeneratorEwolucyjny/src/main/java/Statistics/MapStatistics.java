package Statistics;

import Map.IMap;
import Map.Map;
import MapElements.AbstractMapElement;
import MapElements.Animal;
import MapElements.Genotype;
import MapElements.Grass;
import Observer.IElementPresenceObserver;
import Other.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

// klasa przechowująca statystyki dla danej mapy
public class MapStatistics implements IElementPresenceObserver {
    /*
        Program ma pozwalać na śledzenie następujących statystyk dla aktualnej sytuacji w symulacji:
        - liczby wszystkich zwierząt,
        - liczby wszystkich roślin,
        - dominujących genotypów,
        - średniego poziomu energii dla żyjących zwierząt,
        - średniej długości życia zwierząt dla martwych zwierząt,
        - średniej liczby dzieci dla żyjących zwierząt.
    */

    private final Map map;
    private final MapStatisticsToFile statisticsToFile;

    // podstawowe statystyki mapy
    private int animalsOnMap;
    private int grassesOnMap;
    private Genotype mostPopularGenotype;
    private int averageEnergy;
    private int averageLifespan;
    private int averageNumberOfChildren;

    // kolekcje elementów wykorzystywane do aktualizowania statystyk
    private ArrayList<Integer> energies = new ArrayList<>();
    private final ArrayList<Integer> lifespans = new ArrayList<>();
    private final HashMap<Genotype, Integer> genotypes = new HashMap<>();

    // konstruktor statystyk mapy
    public MapStatistics(IMap map) {
        this.map = (Map) map;
        this.map.addObserver(this);
        statisticsToFile = new MapStatisticsToFile();

        animalsOnMap = 0;
        grassesOnMap = 0;
        averageEnergy = 0;
        averageLifespan = 0;
        averageNumberOfChildren = 0;
    }

    // gettery poszczególnych statystyk

    public int getDay() {
        return map.getDay();
    }

    public int getAnimalsOnMap() {
        return animalsOnMap;
    }

    public int getGrassesOnMap() {
        return grassesOnMap;
    }

    public Genotype getMostPopularGenotype() {
        return mostPopularGenotype;
    }

    public int getAverageEnergy() {
        return averageEnergy;
    }

    public int getAverageLifespan() {
        return averageLifespan;
    }

    public int getAverageNumberOfChildren() {
        return averageNumberOfChildren;
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IElementPresencePublisher

    // funkcja reagująca na pojawienie się nowego elementu na mapie
    @Override
    public void placed(AbstractMapElement element, Vector2d position) {
        if(element instanceof Grass){
            grassesOnMap++;
        } else {
            animalsOnMap++;
            Animal animal = (Animal) element;
            energies.add(animal.getEnergy());

            if(genotypes.containsKey(animal.getGenotype())) {
                int number = genotypes.get(animal.getGenotype()) + 1;
                genotypes.put(animal.getGenotype(), number);
            } else {
                genotypes.put(animal.getGenotype(), 1);
            }
        }
    }

    // funkcja reagująca na usunięcie elementu z mapy
    @Override
    public void removed(AbstractMapElement element, Vector2d position) {
        if(element instanceof Grass){
            grassesOnMap--;
        } else {
            animalsOnMap--;
            Animal animal = (Animal) element;
            lifespans.add(animal.getLifespan());
            energies.remove(Integer.valueOf(animal.getEnergy()));

            if(genotypes.get(animal.getGenotype()) > 1) {
                int number = genotypes.get(animal.getGenotype()) - 1;
                genotypes.put(animal.getGenotype(), number);
            } else {
                genotypes.remove(animal.getGenotype(), 1);
            }
        }
    }

    // funkcja wywołująca aktualizacje statystyk
    public void actualiseStatistics() {
        actualiseAverageEnergy();
        actualiseAverageLifespan();
        actualiseMostPopularGenotype();
        actualiseAverageNumberOfChildren();

        actualiseMapStatisticsToFile();
    }

    // funkcja wywołująca aktualizację statystyk zapisywanych do pliku
    private void actualiseMapStatisticsToFile() {
        statisticsToFile.actualiseDay();
        statisticsToFile.actualiseAnimalsOnMap(animalsOnMap);
        statisticsToFile.actualiseGrassesOnMap(grassesOnMap);
        statisticsToFile.actualiseAverageEnergy(averageEnergy);
        statisticsToFile.actualiseAverageLifespan(averageLifespan);
        statisticsToFile.actualiseAverageNumberOfChildren(averageNumberOfChildren);
        statisticsToFile.actualiseMostPopularGenotypes(mostPopularGenotype);
    }

    // funkcje aktualizujące poszczególne statystyki

    private void actualiseEnergies() {
        energies = new ArrayList<>();
        LinkedList<Animal> animals = map.animalsOnMap();
        for(Animal animal : animals) {
            energies.add(animal.getEnergy());
        }
    }

    private void actualiseAverageEnergy() {
        actualiseEnergies();
        if(energies.size() > 0) {
            int sum = 0;
            for(int energy : energies) {
                sum += energy;
            }
            averageEnergy = sum/energies.size();
        } else {
            averageEnergy = 0;
        }
    }

    private void actualiseAverageLifespan() {
        if(lifespans.size() > 0) {
            int sum = 0;
            for(int lifespan : lifespans) {
                sum += lifespan;
            }
            averageLifespan = sum/lifespans.size();
        } else {
            averageLifespan = 0;
        }
    }

    private void actualiseMostPopularGenotype() {
        Set<Genotype> genotypesSet = genotypes.keySet();
        int number = 0;
        Genotype gt = null;
        for(Genotype genotype : genotypesSet) {
            if(genotypes.get(genotype) > number) {
                gt = genotype;
                number = genotypes.get(genotype);
            }
        }
        mostPopularGenotype = gt;
    }

    public LinkedList<Animal> animalsWithMostPopularGenotype() {
        LinkedList<Animal> animals = map.animalsOnMap();
        for(Animal animal : map.animalsOnMap()){
            if(animal.getGenotype() != mostPopularGenotype) {
                animals.remove(animal);
            }
        }
        return animals;
    }

    private void actualiseAverageNumberOfChildren() {
        ArrayList<Integer> children = new ArrayList<>();
        int sum = 0;
        LinkedList<Animal> animals = map.animalsOnMap();
        for(Animal animal : animals) {
            children.add(animal.getChildren().size());
            sum += animal.getChildren().size();
        }

        if(children.size() > 0) {
            averageNumberOfChildren = sum / children.size();
        } else {
            averageNumberOfChildren = 0;
        }
    }

    // funkcja do zapisywania statystyk do pliku
    public void saveStatisticsToFile() {
        statisticsToFile.saveStatisticsToFile();
    }
}