package Visualisation;

import Statistics.AnimalStatistics;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

// wizualizacja statystyk wybranego zwierzęcia
public class AnimalStatisticsVisualiser extends VBox {
    private AnimalStatistics statistics;

    // labelki do odpowiednich statystyk zwierzęcia
    private final Label genotype = new Label();
    private final Label position = new Label();
    private final Label children = new Label();
    private final Label offspring = new Label();
    private final Label whenDied = new Label();

    // konstruktor
    public AnimalStatisticsVisualiser(double width, double height) {
        setPrefSize(width, height);
        setStyle("-fx-background-color: pink;" +
                 "-fx-border-width: 10px;" +
                 "-fx-border-color: lightgrey;" +
                 "-fx-padding: 10px;");

        Label title = new Label();
        title.setText("ANIMAL STATISTICS");
        title.setFont(new Font("Arial Black", 13));

        Label genotypeTitle = new Label();
        genotypeTitle.setText("Genotype:");

        genotype.setText("-");
        position.setText("Position: -");
        children.setText("Children: -");
        offspring.setText("Offspring: -");
        whenDied.setText("Died: -");

        getChildren().addAll(title, genotypeTitle, genotype, position, children, offspring, whenDied);
    }

    // funkcja inicjalizująca prezentację statystyk
    public void initialize(AnimalStatistics statistics) {
        this.statistics = statistics;
        genotype.setText(String.valueOf(statistics.getGenotype()));
        setLabels();
    }

    // funkcja wywołująca aktualizującję statystyk
    public void updateStatistics(){
        statistics.actualiseStatistics();
        setLabels();
    }

    // funckja aktualizująca wypisywane statystyki
    private void setLabels() {
        position.setText("Position: " + statistics.getPosition());
        children.setText("Children: " + statistics.getNumberOfChildren());
        offspring.setText("Offspring: " + statistics.getNumberOfOffspring());
        if(statistics.getWhenDied() != -1){
            whenDied.setText("Died: " + statistics.getWhenDied() + " day");
        } else {
            whenDied.setText(("Died: still alive"));
        }
    }
}