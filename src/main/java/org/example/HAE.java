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
    public HAE(Instance kro200) throws IOException {
        this.kro200 = kro200;
        distanceMatrix = kro200.getDistanceMatrix();
        solve();
    }
//    private List<List<Integer>> solve(){
    private void solve() throws IOException {
        Map<Double, List<List<Integer>>> map = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            cycles_X = new RandomStart().getCycles();
            cycles_X = new CandidatesMoves(kro200, cycles_X).cycles;
            Double key = calcCycleLength(cycles_X.get(0));
            if (map.containsKey(key)) {
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

        while (true) {
            int randomNumber = random.nextInt(20);
            int secondRandomNumber = random.nextInt(20);
            int number = 0;
            if (randomNumber != secondRandomNumber) {
                for (Map.Entry<Double, List<List<Integer>>> i : map.entrySet()) {
                    if (number == randomNumber) {
                        firstParentKey = i.getKey();
                        firstParentValue = i.getValue();
                    }
                    if (number == secondRandomNumber) {
                        secondParentKey = i.getKey();
                        secondParentValue = i.getValue();
                    }
                    number++;
                }
                break;
            }
        }

        Map<Integer, List<Integer[]>> equals = new LinkedHashMap<>();
        List<List<Integer>> cycle_0 = new ArrayList<>();
//        List<Integer> listOfInteger = new ArrayList<Integer>();
        List<List<Integer>> cycle_1 = new ArrayList<>();
        int numberDictionary = 0;
        for (int i = 0; i < firstParentValue.get(0).size() ; i++) { // todo sprawdz czy dobrze z tymi -1
            for (int k = 0; k < secondParentValue.get(0).size() ; k++) {
                Integer firstVertexFirstParent = firstParentValue.get(0).get(i);
                Integer firstVertexFirstParentSecondCycle = firstParentValue.get(1).get(i);
                Integer secondVertexFirstParent;
                Integer secondVertexFirstParentSecondCycle;
                Integer firstVertexSecondParent = secondParentValue.get(0).get(k);
                Integer firstVertexSecondParentSecondCycle = secondParentValue.get(1).get(k);
                Integer secondVertexSecondParent;
                Integer secondVertexSecondParentSecondCycle;


                if(i == firstParentValue.get(0).size() - 1 && k == secondParentValue.get(0).size() - 1 ){
                    secondVertexFirstParent = firstParentValue.get(0).get(0);
                    secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(0);
                    secondVertexSecondParent = secondParentValue.get(0).get(0);
                    secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(0);
                }
                else if (i == firstParentValue.get(0).size() - 1){
                    secondVertexFirstParent = firstParentValue.get(0).get(0);
                    secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(0);
                    secondVertexSecondParent = secondParentValue.get(0).get(k + 1);
                    secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(k + 1);
                }
                else if(k == secondParentValue.get(0).size() - 1){
                    secondVertexFirstParent = firstParentValue.get(0).get(i + 1);
                    secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(i + 1);
                    secondVertexSecondParent = secondParentValue.get(0).get(0);
                    secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(0);
                }
                else {
                    secondVertexFirstParent = firstParentValue.get(0).get(i + 1);
                    secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(i + 1);
                    secondVertexSecondParent = secondParentValue.get(0).get(k + 1);
                    secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(k + 1);
                }



//todo: narazie wszystko razem, jak zrobic zeby bylo oddzielone

                List<Integer> list = new ArrayList<>();
                if (firstVertexFirstParent == firstVertexSecondParent && secondVertexFirstParent == secondVertexSecondParent) {


                    list.add(firstVertexFirstParent);
                    list.add(secondVertexFirstParent);
                    cycle_0.add(list);

                } else if (firstVertexFirstParentSecondCycle == firstVertexSecondParentSecondCycle && secondVertexFirstParentSecondCycle == secondVertexSecondParentSecondCycle) {
                    list.add(firstVertexFirstParentSecondCycle);
                    list.add(secondVertexFirstParentSecondCycle);
                    cycle_1.add(list);

                } else if (firstVertexFirstParent == firstVertexSecondParentSecondCycle && secondVertexFirstParent == secondVertexSecondParentSecondCycle) {
                    list.add(firstVertexFirstParent);
                    list.add(secondVertexFirstParent);
                    cycle_0.add(list);


                } else if (firstVertexFirstParentSecondCycle == firstVertexSecondParent && secondVertexFirstParentSecondCycle == secondVertexSecondParent) {
                    list.add(firstVertexFirstParentSecondCycle);
                    list.add(secondVertexFirstParentSecondCycle);
                    cycle_0.add(list);

                }


            }

        }
        System.out.println("PO PRZYPISANIU< przed equals.csv");
        int size = 1;
        List<List<Integer>> the_longest_edge = new ArrayList<>();
        List<Integer> tmp_list = new ArrayList<>();
        for(int i = 0; i < cycle_0.size(); i++){
            for (int j = 0 ; j < cycle_0.size() ; j++){
                if (cycle_0.get(i).get(0) == cycle_0.get(j).get(0)){
                    tmp_list.add(cycle_0.get(i).get(1));
                    tmp_list.add(cycle_0.get(i).get(0));
                    tmp_list.add(cycle_0.get(j).get(1));
                    the_longest_edge.add(tmp_list);
                    size = 2;
                }
                if (cycle_0.get(i).get(1) == cycle_0.get(j).get(1)){
                    tmp_list.add(cycle_0.get(i).get(0));
                    tmp_list.add(cycle_0.get(i).get(1));
                    tmp_list.add(cycle_0.get(j).get(0));

                    the_longest_edge.add(tmp_list);
                    size = 2;
                }

                if (cycle_0.get(i).get(0) == cycle_0.get(j).get(1)){
                    tmp_list.add(cycle_0.get(i).get(1));
                    tmp_list.add(cycle_0.get(i).get(0));
                    tmp_list.add(cycle_0.get(j).get(0));
                    the_longest_edge.add(tmp_list);
                    size = 2;
                }
                if (cycle_0.get(i).get(1) == cycle_0.get(j).get(0)){
                    tmp_list.add(cycle_0.get(i).get(0));
                    tmp_list.add(cycle_0.get(i).get(1));
                    tmp_list.add(cycle_0.get(j).get(1));
                    the_longest_edge.add(tmp_list);
                    size = 2;
                }

            }
        }


//        for (int i = 0; i < equals.keySet().size(); i++){
//            if (equals.get(i);
//            ))
//    }
    }

//        writeToCsv("equals", equals);


        

//        System.out.println("SKONCZONE");
//    }

    private double calcCycleLength(List<Integer> solution){
        double length = 0;
        for(int i= 0; i<solution.size()-1; i++){
            length += distanceMatrix.get(solution.get(i)).get(solution.get(i+1));
        }
        length += distanceMatrix.get(solution.get(solution.size() - 1)).get(solution.get(0));
        return length;
    }
        public void writeToCsv(String path, Map<List<Integer[]>, List<Integer[]>> dictionary) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("number, cycle, edge_A, edge_B\n");

        for (Map.Entry<List<Integer[]>, List<Integer[]>> entry : dictionary.entrySet()) {
            Integer key = entry.getKey().get(0)[0];
            Integer cycle = entry.getKey().get(0)[1];
            if (entry.getValue() == null){
                printWriter.printf("%d,%s\n", key, "break");
                continue;
            }
            List<Integer[]> pairs = entry.getValue();
            Integer firstVertex = pairs.get(0)[0];
            if (pairs.get(0).length == 2){
                Integer secondVertex = pairs.get(0)[1];
                printWriter.printf("%d,%d,%d,%d\n", key, cycle, firstVertex, secondVertex );
            }
            else{
                System.out.println("COS NIE TAK");
            }

        }
        printWriter.close();
    }

}
