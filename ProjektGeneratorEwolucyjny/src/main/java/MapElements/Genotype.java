package MapElements;

import Other.MapDirection;

import java.util.ArrayList;
import java.util.Random;

// klasa reprezentująca genotyp zwierzęcia
public class Genotype {
    private final int NUMBER_OF_GENS = MapDirection.values().length;
    private final int NUMBER_OF_GENS_IN_GENOTYPE = 32;

    // genotyp - lista genów
    ArrayList<Integer> genotype = new ArrayList<>();

    // licznik występowania poszczególnych genów
    int[] counter = new int[NUMBER_OF_GENS];

    // generator do losowań
    private static final Random generator = new Random();

    // konstruktor generujący nowy genotyp
    public Genotype() {
        int g;
        while(genotype.size() < NUMBER_OF_GENS_IN_GENOTYPE) {
            g = generator.nextInt(NUMBER_OF_GENS);
            genotype.add(g);
        }
        countGens();
        correctGenotype();
    }

    // konstruktor generujący genotyp zwierzęcia na bazie fragmentów genotypów rodziców
    public Genotype(ArrayList<Integer> gens) {
        genotype = new ArrayList<>(gens);
        countGens();
        correctGenotype();
    }

    // funkcja zliczająca występowanie wszystich genów
    private void countGens() {
        for(int g : genotype) {
            counter[g] ++;
        }
    }

    // funkcja naprawiająca genotyp tak, aby zawierał każdy kierunek chociaż raz
    private void correctGenotype() {
        for(int i = 0; i < NUMBER_OF_GENS; i++)
            if (counter[i] == 0) {
                genotype.add(i);
                counter[i]++;
            }

        while(genotype.size() > NUMBER_OF_GENS_IN_GENOTYPE) {
            int g = generator.nextInt(NUMBER_OF_GENS);
            if(counter[g] > 1) {
                genotype.remove(Integer.valueOf(g));
                counter[g]--;
            }
        }

        genotype.sort(Integer::compareTo);
    }

    // rozmnażanie - oddawanie części genotypu
    public ArrayList<Integer> partOfGenotype(int partition1, int partition2) {
        return new ArrayList<>(genotype.subList(partition1, partition2));
    }

    // funkcja losująca gen w celu wyboru obrotu zwierzęcia
    public int getGen() {
        return genotype.get(generator.nextInt(NUMBER_OF_GENS_IN_GENOTYPE));
    }

    @Override
    public String toString() {
        return String.valueOf(genotype);
    }
}