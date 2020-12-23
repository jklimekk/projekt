package Visualisation;

import MapElements.Animal;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

// klasa do przedstawienia genotypu wybranego zwierzęcia
public class ChosenGenotypeVisualiser extends HBox {

    private final Label genotype = new Label();

    // konstruktor
    public ChosenGenotypeVisualiser(double width, double height) {
        setPrefWidth(width);
        setPrefHeight(height);
        setStyle("-fx-background-color: lightpink;" +
                 "-fx-padding: 10px;");

        // opisanie labelek
        Label title = new Label();
        title.setText("CHOSEN GENOTYPE: ");
        title.setFont(new Font("Arial Black", 13));
        genotype.setText("-");

        getChildren().addAll(title, genotype);
    }

    // funkcja inicjalizująca prezentację genotypu
    public void initialize(Animal animal) {
        genotype.setText(String.valueOf(animal.getGenotype()));
    }

    // funkcja do czyszczenia labelki prezentującej genotyp
    public void clear() {
        genotype.setText("-");
    }
}