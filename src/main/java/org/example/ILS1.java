package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ILS1 {

    List<List<Integer>> cycles_X;
    List<List<Integer>> cycles_Y;
    List<List<Integer>> distanceMatrix;
    Instance kro200;
    double bestCyclesLength = 0;
    public ILS1(Instance kro200 ) {
        this.kro200 = kro200;
        distanceMatrix = kro200.getDistanceMatrix();
        solve();
    }

    private List<List<Integer>> solve() {
        long startTime = System.currentTimeMillis();
        cycles_X = new RandomStart().getCycles();
        cycles_X = new CandidatesMoves(kro200, cycles_X).cycles;
        Integer iteration = 0;
        Integer number = 0;
        while (System.currentTimeMillis() - startTime < 94114) {
            //y := x
            cycles_Y = new ArrayList<>(cycles_X.size());
            for (List<Integer> list : cycles_X) {
                List<Integer> tmp = new ArrayList<>();
                for (Integer element: list){
                    tmp.add(element);
                }
                cycles_Y.add(tmp);
            }

            //Perturbacja (y)
            Random random = new Random();
            int random_number = random.nextInt(7) + 3;

            for (int i = 0; i < random_number; i++){
                int firstNumberOfVertex = random.nextInt(cycles_Y.get(0).size());
                int secondNumberOfVertex = random.nextInt(cycles_Y.get(0).size());
                int randomNumber = random.nextInt(2);
                swapVertexInILS1(cycles_Y, firstNumberOfVertex, secondNumberOfVertex, randomNumber);
//                int randomNumber = random.nextInt(2);
                number++;
            }

            //y := Lokalne przeszukiwanie (y)
            cycles_Y = new CandidatesMoves(kro200, cycles_Y).cycles;

            if (calcCycleLength(cycles_Y.get(0)) + calcCycleLength(cycles_Y.get(1)) < calcCycleLength(cycles_X.get(0)) + calcCycleLength(cycles_X.get(1))){
               // x := y
                cycles_X = cycles_Y;
            }
            iteration++;

        }
        bestCyclesLength = calcCycleLength(cycles_X.get(0)) + calcCycleLength(cycles_X.get(1));
        System.out.println("Liczba lacznaa iteracji: " + iteration);
        System.out.println("Liczba lacznaa perturbacji: " + number);
        return cycles_X;

    }
    private double calcCycleLength(List<Integer> solution){
        double length = 0;
        for(int i= 0; i<solution.size()-1; i++){
            length += distanceMatrix.get(solution.get(i)).get(solution.get(i+1));
        }
        length += distanceMatrix.get(solution.get(solution.size() - 1)).get(solution.get(0));
        return length;
    }

    public void solutionToCsv(String path, Instance instance) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("cycle,x,y\n");
        for (Integer a : cycles_X.get(0)) {
            printWriter.printf("%s,%d,%d\n","a", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
        }
        printWriter.printf("%s,%d,%d\n","a", instance.coordinates.get(cycles_X.get(0).get(0)).getKey(), instance.coordinates.get(cycles_X.get(0).get(0)).getValue());
        for (Integer a : cycles_X.get(1)) {
            printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
        }
        printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(cycles_X.get(1).get(0)).getKey(), instance.coordinates.get(cycles_X.get(1).get(0)).getValue());
        printWriter.close();
    }

    private List<List<Integer>> swapVertexInILS1(List<List<Integer>> cycles, Integer first_Number,Integer second_Number, int chooseOption){

        //wymiana wierzcholkow w jednym cyklu
        if (chooseOption == 0){
            //wymiana w ktorym cyklu
            Random random = new Random();
            int whichCycle = random.nextInt(2);
            Integer firstVertexInFirstCycleValue = cycles.get(whichCycle).get(first_Number);
            Integer secondVertexInFirstCycleValue = cycles.get(whichCycle).get(second_Number);

            cycles_Y.get(whichCycle).set(first_Number, secondVertexInFirstCycleValue);
            cycles_Y.get(whichCycle).set(second_Number, firstVertexInFirstCycleValue);
        }
        //wymiana wierzcholkow miedzy cyklami
        else if (chooseOption == 1){
            Integer firstVertexInFirstCycleValue = cycles.get(0).get(first_Number);
            Integer secondVertexInSecondCycleValue = cycles.get(1).get(second_Number);

            cycles_Y.get(0).set(first_Number, secondVertexInSecondCycleValue);
            cycles_Y.get(1).set(second_Number, firstVertexInFirstCycleValue);
        }

        return cycles_Y;
    }

}
