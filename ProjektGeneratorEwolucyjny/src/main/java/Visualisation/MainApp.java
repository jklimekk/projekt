package Visualisation;

import Configurations.Configuration;
import Other.FileSaver;
import Simulation.Simulation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

// bazowa klasa wizualizacji
public class MainApp extends Application {

    // lista chodzących symulacji i ich parametry
    private final ArrayList<Simulation> simulations = new ArrayList<>();
    private Configuration configuration;

    @Override
    public void start(Stage stage) {
        // pobranie parametrów symulacji
        configuration = new Configuration();

        // wyczyszczenie pliku przed zapisem nowych statystyk
        FileSaver.clearFile();

        // okno
        VBox root = new VBox();
        root.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth() - 20, Screen.getPrimary().getVisualBounds().getHeight() - 40);
        root.setStyle("-fx-background-color: white;");

        // kontener na wszystkie symulacje
        HBox box = new HBox();
        box.setPrefSize(root.getPrefWidth(), root.getPrefHeight());
        box.setStyle("-fx-background-color: lightgrey;");

        // tworzenie kolejnych symulacji w przeznaczonym miejscu
        createSimulations(configuration.getSimulations(), box);

        root.getChildren().add(box);

        // działanie symulacji
        Thread thread = new Thread(() -> {
            Runnable runnable = this::runSimulations;
            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }
                Platform.runLater(runnable);
            }
        });
        thread.setDaemon(true);
        thread.start();

        stage.setTitle("Generator Ewolucyjny");
        stage.setResizable(false);
        stage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // tworzenie symulacji
    private void createSimulations(int number, HBox box) {
        for(int i = 0; i < number; i++) {
            Simulation simulation = new Simulation(configuration);
            simulations.add(simulation);

            SimulationVisualiser sv = new SimulationVisualiser(simulation, box.getPrefWidth()/number, box.getPrefHeight());

            box.getChildren().add(sv);
        }
    }

    // uruchamianie symulacji (kolejny dzień)
    private void runSimulations() {
        for(Simulation simulation : simulations) {
            if(simulation.simulationRunning){
                simulation.runOneDay();
            }
        }
    }
}