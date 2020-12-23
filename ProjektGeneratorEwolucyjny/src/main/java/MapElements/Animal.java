package MapElements;

import Map.IMap;
import Observer.IPositionChangeObserver;
import Observer.IPositionChangePublisher;
import Other.MapDirection;
import Other.Vector2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

// klasa reprezentująca zwierzę
public class Animal extends AbstractMapElement implements IPositionChangePublisher {

    // podstawowe parametry zwierzęcia
    private MapDirection orientation;
    private final int startEnergy;
    private int energy;
    private final Genotype genotype;
    private final IMap map;
    private int lifespan;

    // lista obserwatorów
    private final LinkedList<IPositionChangeObserver> observers = new LinkedList<>();

    // lista dzieci zwierzęcia
    private final LinkedList<Animal> children = new LinkedList<>();

    // generator do losowań
    private static final Random generator = new Random();

    // konstruktor - tworzenie zwierzęcia od podstaw
    public Animal(IMap map, int energy) {
        this.map = map;
        this.orientation = generateOrientation();
        this.position = map.generatePosition();
        this.startEnergy = energy;
        this.energy = energy;
        this.lifespan = 0;
        genotype = new Genotype();
    }

    // konstruktor - tworzenie zwierzęcia w wyniku rozmnażania
    public Animal(IMap map, int energy, int startEnergy, Vector2d position, ArrayList<Integer> gens) {
        this.map = map;
        this.orientation = generateOrientation();
        this.position = position;
        this.startEnergy = startEnergy;
        this.energy = energy;
        this.lifespan = 0;
        genotype = new Genotype(gens);
    }

    // funkcja zwracająca energię zwierzęcia
    public int getEnergy() {
        return energy;
    }

    // funkcja zwracająca długośc życia zwierzęcia (po jego śmierci)
    public int getLifespan() {
        return lifespan;
    }

    // funkcja zwracająca genotyp zwierzęcia
    public Genotype getGenotype() {
        return genotype;
    }

    // funkcja zwracająca listę dzieci zwierzęcia
    public LinkedList<Animal> getChildren() {
        return children;
    }

    // funkcja zwracająca listę potomków zwierzęcia
    public LinkedList<Animal> getOffspring() {
        LinkedList<Animal> offspring = new LinkedList<>(children);
        for(Animal child : children) {
            offspring.addAll(child.getOffspring());
        }
        return offspring;
    }

    // funkcja generująca początkową orientację zwierzęcia
    private MapDirection generateOrientation() {
        return switch (generator.nextInt(MapDirection.values().length)) {
            case 0 -> MapDirection.NORTH;
            case 1 -> MapDirection.NORTHEAST;
            case 2 -> MapDirection.EAST;
            case 3 -> MapDirection.SOUTHEAST;
            case 4 -> MapDirection.SOUTH;
            case 5 -> MapDirection.SOUTHWEST;
            case 6 -> MapDirection.WEST;
            default -> MapDirection.NORTHWEST;
        };
    }

    // zmiana orientacji zwierzęcia
    public void turn() {
        int t = genotype.getGen();

        while (t > 0) {
            this.orientation = this.orientation.next();
            t--;
        }
    }

    // zmiana pozycji zwierzęcia na bazie jego orientacji
    public void move() {
        Vector2d newPosition = map.correctPosition(this.position.add(this.orientation.toUnitVector()));
        myPositionChanged(this.position, newPosition);
        this.position = newPosition;
    }

    // funckja przekazująca obserwatorom informację o zmianie pozycji zwierzęcia
    private void myPositionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for(IPositionChangeObserver observator : observers) {
            observator.positionChanged(this, oldPosition, newPosition);
        }
    }

    // jedzenie
    public void eat(int addEnergy) {
        energy += addEnergy;
    }

    // funkcja sprawdzająca czy zwierze może zostać rodzicem
    public boolean canBeParent() {
        return energy >= startEnergy / 2;
    }

    // utrata energii
    public void reduceEnergy(int lostEnergy) {
        energy -= lostEnergy;
    }

    // rozmnażanie - oddawanie energii
    public int reproduceEnergy() {
        int lostEnergy = energy / 4;
        reduceEnergy(lostEnergy);
        return lostEnergy;
    }

    // rozmnażanie - oddawanie konkretnej części genotypu
    public ArrayList<Integer> reproduceGenotype(int partition1, int partition2) {
        return genotype.partOfGenotype(partition1, partition2);
    }

    // funkcja dodająca nowe dziecko zwierzęcia do listy
    public void newChild(Animal child) {
        children.add(child);
    }

    // funkcja porównująca poziomy energii zwierząt (komparator)
    public static int compareEnergy(Animal one, Animal two) {
        return two.getEnergy() - one.getEnergy();
    }

    // funkcja zapisująca długość życia zwierzęcia po jego śmierci
    public void removed(int days) {
        lifespan = days;
    }

    // funckcja do wizualizacji elementu
    @Override
    public ElementImage getImage() {
        if(energy > 3 * startEnergy / 4) {
            return ElementImage.STRONG_ANIMAL;
        } else if(energy > startEnergy / 2) {
            return ElementImage.NORMAL_ANIMAL;
        } else if(energy > startEnergy / 4) {
            return ElementImage.WEAK_ANIMAL;
        } else {
            return ElementImage.ALMOST_DEAD_ANIMAL;
        }
    }

    // funkcje umożliwiające porównywanie zwierząt ze sobą
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;

        return this.position.x == that.position.x && this.position.y == that.position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IPositionChangePublisher

    // funkcja dodająca do listy nowego obserwatora
    @Override
    public void addObserver(IPositionChangeObserver observer) {
        observers.add(observer);
    }

    // funkcja usuwająca z listy obserwatora
    @Override
    public void removeObserver(IPositionChangeObserver observer) {
        observers.remove(observer);
    }
}