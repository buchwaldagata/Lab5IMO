package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static java.lang.reflect.Array.set;

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

                if(firstVertexFirstParent>secondVertexFirstParent) {
                    int tmp = firstVertexFirstParent;
                    firstVertexFirstParent = secondVertexFirstParent;
                    secondVertexFirstParent = tmp;
                }

                if(firstVertexFirstParentSecondCycle>secondVertexFirstParentSecondCycle) {
                    int tmp = firstVertexFirstParentSecondCycle;
                    firstVertexFirstParentSecondCycle = secondVertexFirstParentSecondCycle;
                    secondVertexFirstParentSecondCycle = tmp;
                }

                if(firstVertexSecondParent>secondVertexSecondParent) {
                    int tmp = firstVertexSecondParent;
                    firstVertexSecondParent = secondVertexSecondParent;
                    secondVertexSecondParent = tmp;
                }

                if(firstVertexSecondParentSecondCycle>secondVertexSecondParentSecondCycle) {
                    int tmp = firstVertexSecondParentSecondCycle;
                    firstVertexSecondParentSecondCycle = secondVertexSecondParentSecondCycle;
                    secondVertexSecondParentSecondCycle = tmp;
                }

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


        List<List<Integer>> list_of_edges = new ArrayList<>();

        for(int i = 0; i < cycle_0.size(); i++){
            for (int j = cycle_0.size() - 1 ; j >=0 ; j--){
                List<Integer> temporary_list = new ArrayList<>();
                Integer first_in_first = cycle_0.get(i).get(0);
                Integer second_in_first = cycle_0.get(i).get(1);
                Integer first_in_second = cycle_0.get(j).get(0);
                Integer second_in_second = cycle_0.get(j).get(1);

                if(i != j ){
                    if (first_in_first == first_in_second){
                        temporary_list.add(second_in_first);
                        temporary_list.add(first_in_first);
                        temporary_list.add(second_in_second);
                        list_of_edges.add(temporary_list);
                    }
                    if (first_in_first == second_in_second){
                        temporary_list.add(second_in_first);
                        temporary_list.add(first_in_first);
                        temporary_list.add(first_in_second);
                        list_of_edges.add(temporary_list);
                    }
                }
            }
        }

        System.out.println("SKONCZONE");
        removeDuplicates(list_of_edges);
        System.out.println("Skopcznone 2");


        for(int k=0; k< list_of_edges.size(); k ++){
            for(int l=0; l< list_of_edges.size(); l++){
                if(k!=l){
                    while (!haveNoCommonElements(list_of_edges.get(k), list_of_edges.get(l))) {

                    }
                }


            }
        }

//        List<Set<Integer>> connected = findConnectedComponents(list_of_edges);
//        System.out.println("Skonczone3");
    }

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

    public static void removeDuplicates(List<List<Integer>> listOfLists) {
        List<Set<Integer>> uniqueSets = new ArrayList<>();

        Iterator<List<Integer>> iterator = listOfLists.iterator();

        while (iterator.hasNext()) {
            List<Integer> currentList = iterator.next();
            Set<Integer> currentSet = new HashSet<>(currentList);

            boolean isDuplicate = false;
            for (Set<Integer> uniqueSet : uniqueSets) {
                if (currentSet.equals(uniqueSet)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                iterator.remove();
            } else {
                uniqueSets.add(currentSet);
            }
        }
    }

//    public static List<Set<Integer>> findConnectedComponents(List<List<Integer>> listOfLists) {
//        Map<Integer, Set<Integer>> graph = new HashMap<>();
//
//        // Tworzenie grafu z list
//        for (List<Integer> list : listOfLists) {
//            for (int i = 0; i < list.size() - 1; i++) {
//                int u = list.get(i);
//                int v = list.get(i + 1);
//                graph.computeIfAbsent(u, k -> new HashSet<>()).add(v);
//                graph.computeIfAbsent(v, k -> new HashSet<>()).add(u);
//            }
//        }
//
//        // Znalezienie połączonych komponentów
//        List<Set<Integer>> connectedComponents = new ArrayList<>();
//        Set<Integer> visited = new HashSet<>();
//
//        for (int node : graph.keySet()) {
//            if (!visited.contains(node)) {
//                Set<Integer> component = new HashSet<>();
//                dfs(node, graph, visited, component);
//                connectedComponents.add(component);
//            }
//        }
//
//        return connectedComponents;
//    }
//
//    private static void dfs(int node, Map<Integer, Set<Integer>> graph, Set<Integer> visited, Set<Integer> component) {
//        Stack<Integer> stack = new Stack<>();
//        stack.push(node);
//
//        while (!stack.isEmpty()) {
//            int current = stack.pop();
//            if (!visited.contains(current)) {
//                visited.add(current);
//                component.add(current);
//                for (int neighbor : graph.getOrDefault(current, Collections.emptySet())) {
//                    if (!visited.contains(neighbor)) {
//                        stack.push(neighbor);
//                    }
//                }
//            }
//        }
//    }

    public static boolean haveNoCommonElements(List<Integer> list1, List<Integer> list2) {
        for (Integer element : list1) {
            if (list2.contains(element)) {
                return false;
            }
        }
        return true;
    }


}
