package Map;

import MapElements.AbstractMapElement;
import MapElements.Animal;
import MapElements.Grass;
import MapElements.IMapElement;
import Observer.IPositionChangeObserver;
import Observer.IElementPresencePublisher;
import Observer.IElementPresenceObserver;
import Other.Vector2d;

import java.util.*;

// klasa reprezentująca mapę - świat
public class Map implements IMap, IPositionChangeObserver, IElementPresencePublisher {

    // obiekty określające wymiary przestrzeni świata
    private final RectangleArea mapArea;
    private final RectangleArea jungleArea;

    // parametry do obsługi zwierząt
    private final int moveEnergy;
    private final int plantEnergy;
    private final int startEnergy;

    // kolejkcje obecnych na mapie zwierząt i roślin
    private final java.util.Map<Vector2d, LinkedList<Animal>> animalsHash = new HashMap<>();
    private final java.util.Map<Vector2d, Grass> grassesHash = new HashMap<>();

    // lista obserwatorów obecności elementów na mapie
    private final LinkedList<IElementPresenceObserver> observers = new LinkedList<>();

    // liczba dni od startu symulacji
    private int days;

    // generator do losowań
    private static final Random generator = new Random();

    // konstruktor mapy
    public Map(int width, int height, double jungleRatio, int moveEnergy, int plantEnergy, int startEnergy) {
        mapArea = new RectangleArea(new Vector2d(0,0), new Vector2d(width - 1, height - 1));

        // umiejscowienie dżungli
        jungleArea = mapArea.generateAreaInside(jungleRatio);

        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;
        this.days = 0;
    }

    // funkcja zwracająca obiekt obsługujący wymiary mapy
    public RectangleArea getMapArea() {
        return mapArea;
    }

    // funkcja zwracająca aktualny dzień na mapie
    @Override
    public int getDay() {
        return days;
    }

    // funkcja generująca listę wszystkich zwierząt na mapie
    @Override
    public LinkedList<Animal> animalsOnMap() {
        LinkedList<Animal> animalsOnMap = new LinkedList<>();

        for (LinkedList<Animal> animals : animalsHash.values()) {
            animalsOnMap.addAll(animals);
        }
        return animalsOnMap;
    }

    // funkcja zwracająca najsilniejsze zwierze na podanej pozycji
    @Override
    public Animal animalAtPosition(Vector2d position) {
        LinkedList<Animal> animalsHere = strongestAnimalsAt(position);
        return animalsHere.get(animalsHere.size() - 1);
    }

    // funkcja umiejscawiająca zwierzę na mapie
    @Override
    public void place(Animal animal) {
        if (animalsHash.containsKey(animal.getPosition())) {
            animalsHash.get(animal.getPosition()).add(animal);
        } else {
            animalsHash.put(animal.getPosition(), new LinkedList<>(Collections.singleton(animal)));
        }

        animal.addObserver(this);
        elementPlaced(animal, animal.getPosition());
    }

    // funkcja informująca obserwatorów o pojawieniu się nowego elementu na mapie
    private void elementPlaced(AbstractMapElement element, Vector2d position) {
        for(IElementPresenceObserver observer : observers)
            observer.placed(element, position);
    }

    // funkcja usuwająca element z mapy
    @Override
    public void remove(AbstractMapElement element) {
        Vector2d position = element.getPosition();
        if(grassesHash.containsKey(position) && element.equals(grassesHash.get(position))) {
            grassesHash.remove(position);
        } else {
            Animal movedAnimal = (Animal) element;
            LinkedList<Animal> animalsHere = animalsHash.get(position);
            animalsHere.remove(movedAnimal);

            movedAnimal.removed(days);

            if (animalsHere.size() == 0){
                animalsHash.remove(position);
            } else {
                animalsHash.put(position, animalsHere);
            }

            movedAnimal.removeObserver(this);
        }

        for(IElementPresenceObserver observer : observers)
            observer.removed(element, element.getPosition());
    }

    // funkcja generująca początkową pozycję zwierzęcia
    @Override
    public Vector2d generatePosition() {
        Vector2d v = mapArea.generateVectorInside();

        while (isOccupied(v))
            v = mapArea.generateVectorInside();

        return v;
    }

    // funkcja generująca pozycję zwierzęcia obok pozycji rodzica
    private Vector2d generatePositionNextTo(Vector2d position) {
        ArrayList<Vector2d> positionsAround = mapArea.generateVectorsAround(position);
        Vector2d newPosition = positionsAround.get(generator.nextInt(positionsAround.size()));

        while (isOccupied(newPosition) && positionsAround.size() > 1) {
            positionsAround.remove(newPosition);
            newPosition = positionsAround.get(generator.nextInt(positionsAround.size()));
        }

        return newPosition;
    }

    // funkcja realizująca zawijanie się brzegów mapy
    @Override
    public Vector2d correctPosition(Vector2d v) {
        return mapArea.correctPosition(v);
    }

    // funkcja symulująca dzień na mapie
    @Override
    public void day() {
        days++;

        // usunięcie martwych zwierząt z mapy
        removeDeadAnimals();

        // skręt i przemieszczenie każdego zwierzęcia
        for(Animal animal : animalsOnMap()) {
            animal.turn();
            animal.move();
        }

        // jedzenie
        eating();

        // rozmnażanie zwierząt
        reproduction();

        // dodanie nowych roślin do mapy
        generateGrassInsideJungle();
        generateGrassOutsideJungle();

        // obniżenie energii wszystkich zwierząt
        for(Animal animal : animalsOnMap()) {
            animal.reduceEnergy(moveEnergy);
        }
    }

    // usuwanie martwych zwierząt z mapy
    private void removeDeadAnimals() {
        LinkedList<Animal> animalsOnMap = new LinkedList<>(animalsOnMap());

        for(Animal animal : animalsOnMap) {
            if (animal.getEnergy() <= 0) {
                remove(animal);
            }
        }
    }

    // jedzenie
    private void eating() {
        Set<Vector2d> grassesPositions = new HashSet<>(grassesHash.keySet());

        for(Vector2d position : grassesPositions) {
            if(animalsHash.containsKey(position)){
                ArrayList<Animal> strongest = new ArrayList<>(strongestAnimalsAt(position));
                int number = strongest.size();
                for(Animal animal : strongest)
                    animal.eat(plantEnergy / number);
                remove(grassesHash.get(position));
            }
        }
    }

    // funkcja szukająca pozycji z min. dwójką zwierząt na niej
    private void reproduction() {
        Set<Vector2d> positions = new HashSet<>(animalsHash.keySet());

        for(Vector2d v : positions) {
            if (animalsHash.get(v).size() > 1) {
                reproduce(strongestAnimalsAt(v));
            }
        }
    }

    // funkcja rozmnażająca zwierzęta (jeśli to możliwe)
    private void reproduce(LinkedList<Animal> possibleParents) {
        LinkedList<Animal> parents = new LinkedList<>();

        for(Animal animal : possibleParents)
            if (animal.canBeParent())
                parents.add(animal);

        if (parents.size() < 2) return;

        while(parents.size() > 2) {
            int g = generator.nextInt(parents.size());
            parents.remove(g);
        }

        int childEnergy = parents.get(0).reproduceEnergy() + parents.get(1).reproduceEnergy();

        ArrayList<Integer> partitions = new ArrayList<>(List.of(0, generator.nextInt(32), generator.nextInt(32), 32));
        partitions.sort(Integer::compareTo);

        ArrayList<Integer> gens = new ArrayList<>();

        ArrayList<Integer> fromWhoWhichPart = new ArrayList<>(List.of(0,1,generator.nextInt(2)));

        int index;
        for(int i = 0; i < 3; i++) {
            index = generator.nextInt(fromWhoWhichPart.size());
            gens.addAll(parents.get(fromWhoWhichPart.get(index)).reproduceGenotype(partitions.get(i), partitions.get(i+1)));
            fromWhoWhichPart.remove(index);
        }

        Animal child = new Animal(this, childEnergy, startEnergy, generatePositionNextTo(parents.get(0).getPosition()), gens);
        place(child);

        parents.get(0).newChild(child);
        parents.get(1).newChild(child);
    }

    // funkcja zwracająca zwierzęta z największą energią na zadanej pozycji
    private LinkedList<Animal> strongestAnimalsAt (Vector2d position) {
        LinkedList<Animal> animalsHere = new LinkedList<>(animalsHash.get(position));
        animalsHere.sort(Animal::compareEnergy);

        int maxEnergy = animalsHere.get(animalsHere.size()-1).getEnergy();
        int index = animalsHere.size() - 1;

        while(index >= 0 && animalsHere.get(index).getEnergy() == maxEnergy)
            index--;

        return index == -1 ? animalsHere : new LinkedList<>(animalsHere.subList(index, animalsHere.size()));
    }


    // generowanie pojedynczej trawy w dżungli
    private void generateGrassInsideJungle() {
        if (!anyEmptyPositionsJungle()) return;

        Vector2d v = jungleArea.generateVectorInside();

        while(isOccupied(v))
            v = jungleArea.generateVectorInside();

        Grass grass = new Grass(v);
        grassesHash.put(v, grass);
        elementPlaced(grass, v);
    }

    // generowanie pojedynczej trawy poza dżunglą
    private void generateGrassOutsideJungle() {
        if (!anyEmptyPositionsOutsideJungle()) return;

        Vector2d v = mapArea.generateVectorInside();

        while(isOccupied(v) || (jungleArea.insideArea(v)))
            v = mapArea.generateVectorInside();

        Grass grass = new Grass(v);
        grassesHash.put(v, grass);
        elementPlaced(grass, v);
    }

    // funkcja sprawdzająca dostępne miejsca w dżungli
    private boolean anyEmptyPositionsJungle() {
        HashSet<Vector2d> positionsOccupied = new HashSet<>();
        positionsOccupied.addAll(animalsHash.keySet());
        positionsOccupied.addAll(grassesHash.keySet());

        positionsOccupied.removeIf(v -> !(jungleArea.insideArea(v)));

        return positionsOccupied.size() < jungleArea.getSize();
    }

    // funkcja sprawdzająca dostępne miejsca poza dżunglą
    private boolean anyEmptyPositionsOutsideJungle() {
        HashSet<Vector2d> positionsOccupied = new HashSet<>();
        positionsOccupied.addAll(animalsHash.keySet());
        positionsOccupied.addAll(grassesHash.keySet());

        positionsOccupied.removeIf(jungleArea::insideArea);

        return positionsOccupied.size() < (mapArea.getSize() - jungleArea.getSize());
    }

    // funkcja informująca czy na danej pozycji coś już się znajduje
    @Override
    public boolean isOccupied(Vector2d position) {
        return animalsHash.containsKey(position) || grassesHash.containsKey(position);
    }

    // funkcja zwracająca obiekt na zadanej pozycji
    @Override
    public IMapElement occupiedBy(Vector2d position) {
        if(grassesHash.containsKey(position)) {
            return grassesHash.get(position);
        } else {
            return animalsHash.get(position).get(animalsHash.get(position).size()-1);
        }
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IPositionChangeObserver

    // jako obserwator reaguje na zmianę pozycji zwierzęcia
    @Override
    public void positionChanged(Animal movedElement, Vector2d oldPosition, Vector2d newPosition) {
        LinkedList<Animal> animalsHere = animalsHash.get(oldPosition);

        animalsHere.remove(movedElement);

        if (animalsHere.size() == 0){
            animalsHash.remove(oldPosition);
        } else {
            animalsHash.remove(oldPosition);
            animalsHash.put(oldPosition, animalsHere);
        }

        if(animalsHash.containsKey(newPosition)) {
            animalsHere = animalsHash.get(newPosition);
        } else {
            animalsHere = new LinkedList<>();
        }
        animalsHere.add(movedElement);
        animalsHash.remove(newPosition);
        animalsHash.put(newPosition, animalsHere);
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IElementPresencePublisher

    // funckja dodająca do listy nowego obserwatora
    @Override
    public void addObserver(IElementPresenceObserver observer) {
        observers.add(observer);
    }

    // funckja usuwająca z listy obserwatora
    @Override
    public void removeObserver(IElementPresenceObserver observer) {
        observers.remove(observer);
    }
}