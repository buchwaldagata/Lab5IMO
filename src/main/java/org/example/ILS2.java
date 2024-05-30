package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ILS2 {

    List<List<Integer>> cycles_X;
    List<List<Integer>> cycles_Y;
    List<List<Integer>> distanceMatrix;
    Instance kro200;

    List<Integer> removedElements = new ArrayList<>();
    double bestCyclesLength = 0;
    public ILS2(Instance kro200 ) {
        this.kro200 = kro200;
        distanceMatrix = kro200.getDistanceMatrix();
        solve();
    }

    private List<List<Integer>> solve() {
        long startTime = System.currentTimeMillis();

        cycles_X = new RandomStart().getCycles();
        cycles_X = new CandidatesMoves(kro200, cycles_X).cycles;
        Integer iteration = 0 ;
        Integer number = 0;
        while (System.currentTimeMillis() - startTime < 94114) {
            cycles_Y = new ArrayList<>(cycles_X.size());
            for (List<Integer> list : cycles_X) {
                List<Integer> tmp = new ArrayList<>();
                for (Integer element: list){
                    tmp.add(element);
                }
                cycles_Y.add(tmp);
            }

            //Destroy (y)

            Random random = new Random();
            removeElements(0,random);
            removeElements(1,random);
            number+=60;
            //Repair (y)
            greedyCycle(removedElements, cycles_Y);

            //y := Lokalne przeszukiwanie (y)
            cycles_Y = new CandidatesMoves(kro200, cycles_Y).cycles;

            if (calcCycleLength(cycles_Y.get(0)) + calcCycleLength(cycles_Y.get(1)) < calcCycleLength(cycles_X.get(0)) + calcCycleLength(cycles_X.get(1))){
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

    public void removeElements(Integer whichCycle, Random random ){

        for (int i = 0; i < 30; i++){
            int randomIndex = random.nextInt(100-i);
            Integer removedValue = cycles_Y.get(whichCycle).get(randomIndex);
            cycles_Y.get(whichCycle).remove(randomIndex);
            removedElements.add(removedValue);
        }
    }

    private void greedyCycle(List<Integer> unassignedVertices, List<List<Integer>> cycles){
        int maxSize = kro200.getDistanceMatrix().size()/2;
        while (true){
            if(unassignedVertices.isEmpty()) {
                break;
            }
            if(cycles.get(0).size() < maxSize) {
                findAndAddBestVertex(unassignedVertices, cycles.get(0));
            }

            if(unassignedVertices.isEmpty()) {
                break;
            }
            if(cycles.get(1).size() < maxSize) {
                findAndAddBestVertex(unassignedVertices, cycles.get(1));
            }
        }
    }

    private void findAndAddBestVertex(List<Integer> unassignedVertices, List<Integer> cycle) {
        int resultNewVertex = -1;
        int resultNewVertexNumberInCycle = -1;
        long resultTotalCost = -1L;
        for(int i=0; i<cycle.size()-1; i++) {
            int fromVertex = cycle.get(i);
            int toVertex = cycle.get(i+1);
            Integer oldCost = distanceMatrix.get(fromVertex).get(toVertex);
            for(int newVertex: unassignedVertices) {
                Integer firstCost = distanceMatrix.get(fromVertex).get(newVertex);
                Integer secondCost = distanceMatrix.get(newVertex).get(toVertex);
                Integer newCost = firstCost + secondCost;
                long totalCost = newCost - oldCost;

                if(totalCost<resultTotalCost || resultTotalCost==-1) {
                    resultNewVertexNumberInCycle = i+1;
                    resultNewVertex = newVertex;
                    resultTotalCost = totalCost;
                }
            }
        }
        cycle.add(cycle.get(cycle.size()-1));
        for(int i=cycle.size()-2; i>=resultNewVertexNumberInCycle; i--) {
            cycle.set(i+1, cycle.get(i));
        }
        cycle.set(resultNewVertexNumberInCycle, resultNewVertex);
        int finalResultNewVertex = resultNewVertex;
        unassignedVertices.removeIf(i -> i.equals(finalResultNewVertex));
    }

}
