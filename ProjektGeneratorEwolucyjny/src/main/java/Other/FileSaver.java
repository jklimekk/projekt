package Other;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

// klasa do zapisywania informacji do pliku
public final class FileSaver {

    // stała ścieżka do pliku
    private static final String path = "result.txt";

    // funkcja czyszcząca plik przed każdym uruchomieniem symulacji
    public static void clearFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // funkcja zapisująca do pliku podany tekst
    public static void saveToFile(String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.append(text);
            writer.append('\n');
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
