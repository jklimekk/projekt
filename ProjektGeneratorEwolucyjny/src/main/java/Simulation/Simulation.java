package Simulation;

import Configurations.Configuration;
import Map.IMap;
import Map.Map;
import MapElements.Animal;
import Statistics.MapStatistics;
import Visualisation.AnimalStatisticsVisualiser;
import Visualisation.MapStatisticsVisualiser;
import Visualisation.MapVisualiser;

// symulacja
public class Simulation {

    // parametr określający czy w danym momencie symulacja działa
    public boolean simulationRunning = true;

    // mapa i jej statystyki
    private final IMap map;
    private final MapStatistics statistics;

    // obiekty do wizualizacji
    private MapVisualiser mv;
    private MapStatisticsVisualiser msv;
    private AnimalStatisticsVisualiser asv;

    // gettery

    public IMap getMap() {
        return map;
    }

    public MapStatistics getStatistics() {
        return statistics;
    }

    // funkcje przypisujące obiekty do wizualizacji mapy i jej statystyk

    public void setMapVisualiser(MapVisualiser mv) {
        this.mv = mv;
    }

    public void setMapStatisticsVisualiser(MapStatisticsVisualiser msv) {
        this.msv = msv;
    }

    public void setAnimalStatisticsVisualiser(AnimalStatisticsVisualiser asv) {
        this.asv = asv;
    }

    // konstruktor symulacji
    public Simulation(Configuration parameters) {
        // liczbę zwierząt ustalam na podstawie wielkości mapy
        int animalsAtStart = parameters.getWidth() * parameters.getHeight() / 100;

        map = new Map(parameters.getWidth(), parameters.getHeight(), parameters.getJungleRatio(), parameters.getMoveEnergy(), parameters.getPlantEnergy(), parameters.getStartEnergy());
        statistics = new MapStatistics(map);

        // umiejscowienie zwierząt
        for(int i = 0; i < animalsAtStart; i++)
            map.place(new Animal(map, parameters.getStartEnergy()));
    }

    // funkcja wywołująca działanie kolejnego dnia na mapie
    public void runOneDay() {
        map.day();
        mv.updateMap();
        msv.updateStatistics();
        if(asv != null) {
            asv.updateStatistics();
        }
    }
}