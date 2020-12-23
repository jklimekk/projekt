package Visualisation;

import Statistics.MapStatistics;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

// wizualizacja statystyk odnośnie mapy
public class MapStatisticsVisualiser extends VBox {

    // prezentowane statystyki
    private MapStatistics statistics;

    // labelki do odpowiednich statystyk
    private final Label title = new Label();
    private final Label day = new Label();
    private final Label animalsOnMap = new Label();
    private final Label grassesOnMap = new Label();
    private final Label mostPopularGenotype = new Label();
    private final Label genotype = new Label();
    private final Label averageEnergy = new Label();
    private final Label averageLifespan = new Label();
    private final Label averageNumberOfChildren = new Label();

    // konstruktor
    public MapStatisticsVisualiser(double width, double height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: white;" +
                 "-fx-border-width: 10px;" +
                 "-fx-border-color: lightgrey;" +
                 "-fx-padding: 10px;");
    }

    // funkcja inicjalizująca prezentację statystyk
    public void initialize(MapStatistics statistics) {
        this.statistics = statistics;
        setLabels();
        title.setText("MAP STATISTICS");
        title.setFont(new Font("Arial Black", 13));
        mostPopularGenotype.setText("The most popular genotype:");
        getChildren().addAll(title, day, animalsOnMap, grassesOnMap, mostPopularGenotype, genotype, averageEnergy, averageLifespan, averageNumberOfChildren);
    }

    // getter rezprezentowanych statystyk
    public MapStatistics getStatistics() {
        return statistics;
    }

    // funkcja wywołująca aktualizującję statystyk
    public void updateStatistics(){
        statistics.actualiseStatistics();
        setLabels();
    }

    // funckja aktualizująca wypisywane statystyki
    private void setLabels() {
        day.setText("Day: " + statistics.getDay());
        animalsOnMap.setText("Animals on map: " + statistics.getAnimalsOnMap());
        grassesOnMap.setText("Grasses on map: " + statistics.getGrassesOnMap());
        if(statistics.getMostPopularGenotype() == null){
            genotype.setText("no genotypes to check");
        } else {
            genotype.setText(String.valueOf(statistics.getMostPopularGenotype()));
        }
        averageEnergy.setText("Average energy: " + statistics.getAverageEnergy());
        averageLifespan.setText("Average lifespan: " + statistics.getAverageLifespan());
        averageNumberOfChildren.setText("Average number of children: " + statistics.getAverageNumberOfChildren());
    }
}