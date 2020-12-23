package Visualisation;

import MapElements.Animal;
import Observer.IClickedObserver;
import Other.Vector2d;
import Simulation.Simulation;
import Map.Map;
import Statistics.AnimalStatistics;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// wizualizacja poszczególnych symulacji
public class SimulationVisualiser extends VBox implements IClickedObserver {

    // obiekty do wizualizacji odpowiednich części wizualizacji
    private final MapVisualiser mv;
    private final MapStatisticsVisualiser msv;
    private final AnimalStatisticsVisualiser asv;
    private final ChosenGenotypeVisualiser gv;
    private final Simulation simulation;
    private boolean trackingAnimal = false;

    // konstruktor
    public SimulationVisualiser(Simulation simulation, double width, double height) {
        setPrefWidth(width);
        setPrefHeight(height);
        setStyle("-fx-background-color: lightgrey;" +
                 "-fx-padding: 10px;");

        // przypisanie symulaci do wizualizacji
        this.simulation = simulation;

        // ustawienia z przyciskami
        Settings settings = new Settings(this, width, height);

        // wizualizacja mapy
        this.mv = new MapVisualiser(width, height * 0.6);
        mv.initialize((Map) simulation.getMap(), this, settings);

        // wizualizacja genotypu wybranego zwierzęcia
        gv = new ChosenGenotypeVisualiser(width, height);

        // kontener na statystyki
        HBox box = new HBox();

        // statystyki do mapy
        this.msv = new MapStatisticsVisualiser(width * 0.6, height);
        msv.initialize(simulation.getStatistics());

        // statystyki do zwierzęcia
        this.asv = new AnimalStatisticsVisualiser(width * 0.4, height);

        simulation.setMapVisualiser(mv);
        simulation.setMapStatisticsVisualiser(msv);

        box.getChildren().addAll(msv, asv);
        getChildren().addAll(mv, settings, gv, box);
    }

    // funkcja do śledzenia zwierzęcia
    public void setTrackingAnimal(boolean sth) {
        trackingAnimal = sth;
    }

    // funkcja informująca czy odbywa się śledzenie zwierzęcia
    public boolean isTrackingAnimal() {
        return trackingAnimal;
    }

    // gettery

    public Simulation getSimulation() {
        return simulation;
    }

    public MapStatisticsVisualiser getMapStatisticsVisualiser() {
        return msv;
    }

    // funkcja do pokazywania zwierząt z najpopularniejszym genotypem
    public void showAnimalsWithGenotype() {
        mv.showAnimalsWithMostPopularGenotype(simulation.getStatistics().animalsWithMostPopularGenotype());
    }

    // funkcja do czyszczenia pola z pokazanym genotypem wybranego zwierzęcia
    public void clearChosenGenotypeVisualiser() {
        gv.clear();
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IClickedObserver

    // reakcja na kliknięcie pola
    @Override
    public void clicked(Vector2d position) {
        if(simulation.simulationRunning) return;

        if(simulation.getMap().isOccupied(position) && simulation.getMap().occupiedBy(position) instanceof Animal) {
            Animal animal = simulation.getMap().animalAtPosition(position);
            if(trackingAnimal){
                // rozpoczęcie śledzenia zwierzęcia
                AnimalStatistics as = new AnimalStatistics(animal);
                asv.initialize(as);
                simulation.setAnimalStatisticsVisualiser(asv);
            } else {
                // prezentacja genotypu wybranego zwierzęcia
                gv.initialize(animal);
            }
        }
    }
}
