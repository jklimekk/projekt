package Statistics;

import MapElements.Genotype;
import Other.FileSaver;

import java.util.HashMap;

// klasa używana do zapisu odpowiednich statystyk do pliku
public class MapStatisticsToFile {
    private int days;
    private int sumAnimalsOnMap;
    private int sumGrassesOnMap;
    private int sumAverageNumberOfChildren;
    private int sumAverageEnergy;
    private int sumAverageLifespan;
    private Genotype mostPopularGenotypeAllTime;
    private final HashMap<Genotype, Integer> mostPopularGenotypes;

    // kontruktor
    public MapStatisticsToFile() {
        days = 0;
        sumAnimalsOnMap = 0;
        sumGrassesOnMap = 0;
        sumAverageNumberOfChildren = 0;
        sumAverageEnergy = 0;
        sumAverageLifespan = 0;
        mostPopularGenotypeAllTime = null;
        mostPopularGenotypes = new HashMap<>();
    }

    // funkcje aktualizujące odpowiednie statystyki

    public void actualiseDay() {
        days++;
    }

    public void actualiseAnimalsOnMap(int number) {
        sumAnimalsOnMap += number;
    }

    public void actualiseGrassesOnMap(int number) {
        sumGrassesOnMap += number;
    }

    public void actualiseAverageNumberOfChildren(int number) {
        sumAverageNumberOfChildren += number;
    }

    public void actualiseAverageEnergy(int number) {
        sumAverageEnergy += number;
    }

    public void actualiseAverageLifespan(int number) {
        sumAverageLifespan += number;
    }

    public void actualiseMostPopularGenotypes(Genotype genotype) {
        if(mostPopularGenotypes.containsKey(genotype)) {
            int number = mostPopularGenotypes.get(genotype);
            mostPopularGenotypes.put(genotype, number+1);
        } else {
            mostPopularGenotypes.put(genotype, 1);
        }
        actualiseMostPopularGenotype();
    }

    private void actualiseMostPopularGenotype() {
        int number = 0;
        for(Genotype genotype : mostPopularGenotypes.keySet()) {
            if(mostPopularGenotypes.get(genotype) > number) {
                mostPopularGenotypeAllTime = genotype;
                number = mostPopularGenotypes.get(genotype);
            }
        }
    }

    // funkcja zapisująca statystyki do pliku
    public void saveStatisticsToFile() {
        String readyText = "STATISTICS: " +
                "\nDay: " + days +
                "\nAverage number of animals on map: " + sumAnimalsOnMap / days +
                "\nAverage number of grasses on map: " + sumGrassesOnMap / days +
                "\nAverage number of children: " + sumAverageNumberOfChildren / days +
                "\nAverage energy: " + sumAverageEnergy / days +
                "\nAverage lifespan: " + sumAverageLifespan / days +
                "\nMost popular genotype: " + mostPopularGenotypeAllTime;
        FileSaver.saveToFile(readyText);
    }
}