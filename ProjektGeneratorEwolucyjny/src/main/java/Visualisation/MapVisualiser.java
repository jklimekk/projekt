package Visualisation;

import Map.Map;
import MapElements.Animal;
import MapElements.IMapElement;
import Observer.IClickedObserver;
import Other.Vector2d;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;

// wizualizacja mapy
public class MapVisualiser extends GridPane {

    // dwuwymiarowa tabela pól mapy
    private MapFieldVisualiser[][] fields;

    // wizualizowana mapa
    private Map map;

    // konstruktor
    public MapVisualiser(double width, double height) {
        setPrefSize(width, height);
        setAlignment(Pos.CENTER);
    }

    // funkcja inicjalizująca wizualizację mapy
    public void initialize(Map map, IClickedObserver observer, Settings settings)  {
        this.map = map;
        fields = new MapFieldVisualiser[map.getMapArea().getWidth()][map.getMapArea().getHeight()];

        double size = Math.min(getPrefWidth()/map.getMapArea().getWidth(), getPrefHeight()/map.getMapArea().getHeight());

        // uzupełnianie tablicy
        for(int i = 0; i < map.getMapArea().getWidth(); i++){
            for(int j = 0; j < map.getMapArea().getHeight(); j++){
                fields[i][j] = new MapFieldVisualiser(size, i, j);
                fields[i][j].addObserver(observer);
                fields[i][j].addObserver(settings);
                add(fields[i][j], i, j);
            }
        }
    }

    // funkcja uaktualniająca wizualizację mapy
    public void updateMap(){
        for(int i = 0; i < map.getMapArea().getWidth(); i++){
            for(int j = 0; j < map.getMapArea().getHeight(); j++){

                Vector2d position = new Vector2d(i, j);

                if(! map.isOccupied(position)){
                    fields[i][j].updateField(null);
                } else {
                    IMapElement element = map.occupiedBy(position);
                    fields[i][j].updateField(element);
                }
            }
        }
    }

    // funkcja wyszukująca i pokazująca zwierzę z najpopularniejszym genotypem
    public void showAnimalsWithMostPopularGenotype(LinkedList<Animal> animals) {
        for(Animal animal : animals) {
            Vector2d position = animal.getPosition();
            fields[position.x][position.y].updateFieldToMostPopularGenotype();
        }
    }
}
