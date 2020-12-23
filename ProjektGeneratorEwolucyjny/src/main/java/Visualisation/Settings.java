package Visualisation;

import Observer.IClickedObserver;
import Other.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

// ustawienia z przyciskami do wykonywania akcji
public class Settings extends HBox implements IClickedObserver {

    Button trackButton;

    private final SimulationVisualiser sv;

    // konstruktor
    public Settings(SimulationVisualiser sv, double width, double height) {
        setPrefSize(width, height);
        setStyle("-fx-border-width: 10px;" +
                 "-fx-border-color: lightgrey;");
        setAlignment(Pos.CENTER);

        this.sv = sv;

        // przycisk do stopowania i wznawiania działania symulacji
        Button stoppingButton = new Button();
        stoppingButton.setText("STOP");
        stoppingButton.setOnAction(event -> {
            sv.getSimulation().simulationRunning = !sv.getSimulation().simulationRunning;
            sv.clearChosenGenotypeVisualiser();
            if(sv.getSimulation().simulationRunning) {
                stoppingButton.setText("STOP");
            } else {
                stoppingButton.setText("RUN");
            }
        });

        // przycisk do pokazywania zwierzęcia z najpopularniejszym genotypem
        Button genotypeButton = new Button();
        genotypeButton.setText("MOST POPULAR GENOTYPE");
        genotypeButton.setOnAction(event -> {
            if(! sv.getSimulation().simulationRunning)
                sv.showAnimalsWithGenotype();
            });

        // przycisk do rozpoczęcia śledzenia zwierzęcia
        trackButton = new Button();
        trackButton.setText("TRACK ANIMAL");
        trackButton.setOnAction(event -> {
            sv.getSimulation().simulationRunning = false;
            trackButton.setText("CHOOSE ANIMAL");
            sv.setTrackingAnimal(true);
        });

        // przycisk do zapisywania statystyk mapy
        Button statisticsButton = new Button();
        statisticsButton.setText("SAVE STATISTICS");
        statisticsButton.setOnAction(event -> sv.getMapStatisticsVisualiser().getStatistics().saveStatisticsToFile());

        getChildren().addAll(stoppingButton, genotypeButton, trackButton, statisticsButton);
    }

    // FUNKCJE IMPLEMENTUJĄCE INTERFEJS: IClickedObserver

    // reakcja na kliknięcie pola
    @Override
    public void clicked(Vector2d position) {
        if(sv.isTrackingAnimal()){
            sv.setTrackingAnimal(false);
            trackButton.setText("TRACK NEW ANIMAL");
            sv.getSimulation().simulationRunning = !sv.getSimulation().simulationRunning;
        }
    }
}