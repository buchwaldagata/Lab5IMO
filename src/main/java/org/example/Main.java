package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Instance kroA200 = new Instance("src/main/resources/kroA200.tsp");
//        Instance kroB200 = new Instance("src/main/resources/kroB200.tsp");
        long startTime = System.nanoTime();
//        List<HAE> solutions = new ArrayList<>();

//        for (int i = 0; i < 1; i++) {
//            solutions.add(new HAE(kroA200));
//        }

        List<HAE> solutions = new ArrayList<>();
//        List<MSLS> solutions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
//            solutions.add(new MSLS(kroA200));
            solutions.add(new HAE(kroA200));
        }

        long endTime   = System.nanoTime();
        long totalTime = (endTime - startTime)/1000000;
        System.out.println("Czas łączny " + totalTime);
        int best = 0;
        double cost = 999999999;
        for (int i = 0; i < solutions.size(); i++) {
            if (solutions.get(i).bestCyclesLength < cost) {
                best = i;
                cost = solutions.get(i).bestCyclesLength;
            }
        }

        List<Double> critterValues=new ArrayList<>();
        for (HAE solution: solutions) {
            critterValues.add(solution.bestCyclesLength);
        }
        System.out.println("MAX " + critterValues.stream().max(Double::compareTo));
        System.out.println("MIN " + critterValues.stream().min(Double::compareTo));
        if(critterValues.stream().reduce(Double::sum).isPresent()){
            System.out.println("Średnia " + critterValues.stream().reduce(Double::sum).get()/Double.parseDouble(String.valueOf(critterValues.size())));
        }

        solutions.get(best).solutionToCsv("best.csv", kroA200);



    }
}