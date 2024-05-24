package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class HAE {

    List<List<Integer>> cycles_X;
    List<List<Integer>> cycles_Y;
    List<List<Integer>> distanceMatrix;
    Instance kro200;
    public HAE(Instance kro200){
        this.kro200 = kro200;
        distanceMatrix = kro200.getDistanceMatrix();
        solve();
    }
//    private List<List<Integer>> solve(){
    private void solve(){
        Map<Double, List<List<Integer>>> map = new HashMap<>();

        for (int i = 0; i < 20 ; i++){
            cycles_X = new RandomStart().getCycles();
            cycles_X = new CandidatesMoves(kro200, cycles_X).cycles;
            Double key = calcCycleLength(cycles_X.get(0));
            if (map.containsKey(key)){
                i--;
                continue;
            }
            map.put(key, cycles_X);
        }

        //wylosowanie rodziców
        Random random = new Random();
        Double firstParentKey = 0.0;
        List<List<Integer>> firstParentValue = null;
        Double secondParentKey = 0.0;
        List<List<Integer>> secondParentValue = null;
        while(true){
            int randomNumber = random.nextInt(20);
            int secondRandomNumber = random.nextInt(20);
            int number = 0;
            if (randomNumber != secondRandomNumber){
                for (Map.Entry<Double, List<List<Integer>>>i: map.entrySet()) {
                    if (number == randomNumber){
                        firstParentKey = i.getKey();
                        firstParentValue = i.getValue();
                    }
                    if (number == secondRandomNumber){
                        secondParentKey = i.getKey();
                        secondParentValue = i.getValue();
                    }
                    number++;
                }
                break;
            }
        }

        Map<Integer, List<Integer[]>> equals = new HashMap<>();
        int numberDictionary = 0;
        for(int i = 0; i < firstParentValue.get(0).size() - 1; i++){
                for(int k = 0; k < secondParentValue.get(0).size() - 1; k++){
                       Integer firstVertexFirstParent = firstParentValue.get(0).get(i);
                       Integer firstVertexFirstParentSecondCycle = firstParentValue.get(1).get(i);
                       Integer secondVertexFirstParent = firstParentValue.get(0).get(i+1);
                       Integer secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(i+1);
                       Integer firstVertexSecondParent = secondParentValue.get(0).get(k);
                       Integer firstVertexSecondParentSecondCycle = secondParentValue.get(1).get(k);
                       Integer secondVertexSecondParent = secondParentValue.get(0).get(k+1);
                       Integer secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(k+1);


                       List<Integer[]> list = new ArrayList<>();

                       if (firstVertexFirstParent == firstVertexSecondParent && secondVertexFirstParent == secondVertexSecondParent){
                           list.add(new Integer[]{firstVertexFirstParent, secondVertexFirstParent});
                           equals.put(numberDictionary, list);
                           numberDictionary++;
                       }
                       else if (firstVertexFirstParentSecondCycle == firstVertexSecondParentSecondCycle && secondVertexFirstParentSecondCycle == secondVertexSecondParentSecondCycle) {
                           list.add(new Integer[]{firstVertexFirstParentSecondCycle, secondVertexFirstParentSecondCycle});
                           equals.put(numberDictionary, list);
                           numberDictionary++;
                       }
                       else if (firstVertexFirstParent == firstVertexSecondParentSecondCycle && secondVertexFirstParent == secondVertexSecondParentSecondCycle){
                           list.add(new Integer[]{firstVertexFirstParent, secondVertexFirstParent});
                           equals.put(numberDictionary, list);
                           numberDictionary++;
                       }
                       else if (firstVertexFirstParentSecondCycle == firstVertexSecondParent && secondVertexFirstParentSecondCycle == secondVertexSecondParent){
                            list.add(new Integer[]{firstVertexFirstParentSecondCycle, secondVertexFirstParentSecondCycle});
                            equals.put(numberDictionary, list);
                            numberDictionary++;
                       }


                }

        }


        

        System.out.println("SKONCZONE");
    }

    private double calcCycleLength(List<Integer> solution){
        double length = 0;
        for(int i= 0; i<solution.size()-1; i++){
            length += distanceMatrix.get(solution.get(i)).get(solution.get(i+1));
        }
        length += distanceMatrix.get(solution.get(solution.size() - 1)).get(solution.get(0));
        return length;
    }

//    public void solutionToCsv(String path) throws IOException {
//        FileWriter fileWriter = new FileWriter(path);
//        PrintWriter printWriter = new PrintWriter(fileWriter);
//        printWriter.print("cycle,x,y\n");
//        for (Integer a : cycles_X.get(0)) {
//            printWriter.printf("%s,%d,%d\n","a", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
//        }
//        printWriter.printf("%s,%d,%d\n","a", instance.coordinates.get(cycles_X.get(0).get(0)).getKey(), instance.coordinates.get(cycles_X.get(0).get(0)).getValue());
//        for (Integer a : cycles_X.get(1)) {
//            printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(a).getKey(), instance.coordinates.get(a).getValue());
//        }
//        printWriter.printf("%s,%d,%d\n","b", instance.coordinates.get(cycles_X.get(1).get(0)).getKey(), instance.coordinates.get(cycles_X.get(1).get(0)).getValue());
//        printWriter.close();
//    }

}
