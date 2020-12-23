package Configurations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

// klasa zajmująca się pobieraniem bazowych parametrów symulacji z pliku .json
public class Configuration {
    /*
        Następujące parametry są danymi wejściowymi do aplikacji:
        - Rozmiar mapy (width, height)
        - ilość energii początkowej zwierząt (startEnergy)
        - ilość energii traconej w każdym dniu (moveEnergy)
        - ilość energii zyskiwanej przy zjedzeniu rośliny (plantEnergy)
        - proporcje dżungli do sawanny (jugnleRatio)
    */

    // podstawowe parametry
    private int width;
    private int height;
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio; // liczba z przedziału (0,1)
    private int simulations;  // dodatkowo: liczba symulacji

    // kontruktor
    public Configuration() {
        JsonParser parser = new JsonParser();

        try {
            JsonObject jsonObject = (JsonObject) parser.parse(new FileReader("parameters.json"));
            this.width = jsonObject.get("width").getAsInt();
            this.height = jsonObject.get("height").getAsInt();
            this.startEnergy = jsonObject.get("startEnergy").getAsInt();
            this.moveEnergy = jsonObject.get("moveEnergy").getAsInt();
            this.plantEnergy = jsonObject.get("plantEnergy").getAsInt();
            this.jungleRatio = jsonObject.get("jungleRatio").getAsDouble();
            this.simulations = jsonObject.get("simulations").getAsInt();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // gettery parametrów

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public double getJungleRatio() {
        return jungleRatio;
    }

    public int getSimulations() {
        return simulations;
    }
}
