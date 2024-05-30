package org.example;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MSLS {
    List<List<Integer>> cycles;
    double bestCyclesLength = Double.MAX_VALUE;
    Instance kro200 = new Instance("src/main/resources/kroB200.tsp");

    MSLS(){
        int iterations = 100;
        solve(iterations);
    }

    private void solve(int iterations) {
        new RandomStart();
        RandomStart startingCycles;
        HillClimbing cyclesSolution;
        List<List<Integer>> newCycles;
        double length;

        for (int i = 0; i < iterations; i++) {
            startingCycles = new RandomStart();
            cyclesSolution = new HillClimbing(kro200, startingCycles);

            newCycles = cyclesSolution.getBestCycles();
            length = cyclesSolution.getSolutionValue();

            if (length < bestCyclesLength) {
                cycles = newCycles;
                bestCyclesLength = length;
            }

        }
    }
    public double getSolution(){
        return bestCyclesLength;
    }
    public void solutionToCsv(String path) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("cycle,x,y\n");
        for (Integer a : cycles.get(0)) {
            printWriter.printf("%s,%d,%d\n","a", kro200.coordinates.get(a).getKey(), kro200.coordinates.get(a).getValue());
        }
        for (Integer a : cycles.get(1)) {
            printWriter.printf("%s,%d,%d\n","b", kro200.coordinates.get(a).getKey(), kro200.coordinates.get(a).getValue());
        }
        printWriter.close();
    }
}
