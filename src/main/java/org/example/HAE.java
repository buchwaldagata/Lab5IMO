package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

class HAE {

    List<List<Integer>> cycles_X;
    List<List<Integer>> distanceMatrix;
    Double bestCyclesLength = 0.0;
    Instance kro200;
    public HAE(Instance kro200) throws IOException {
        this.kro200 = kro200;
        distanceMatrix = kro200.getDistanceMatrix();
        solve();
    }
    //    private List<List<Integer>> solve(){
    private void solve() throws IOException {
        long startTime = System.currentTimeMillis();
        Map<Double, List<List<Integer>>> population = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            cycles_X = new RandomStart().getCycles();
            cycles_X = new CandidatesMoves(kro200, cycles_X).cycles;
            Double key = calcCycleLength(cycles_X.get(0)) + calcCycleLength(cycles_X.get(1));
            if (population.containsKey(key)) {
                i--;
                continue;
            }
            population.put(key, cycles_X);
        }

        int iterations = 0;
        while (System.currentTimeMillis() - startTime < 21527) {
            iterations++;
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
                    for (Map.Entry<Double, List<List<Integer>>> i : population.entrySet()) {
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

            List<List<Integer>> cycle_0 = new ArrayList<>();
//        List<Integer> listOfInteger = new ArrayList<Integer>();
            List<List<Integer>> cycle_1 = new ArrayList<>();
            for (int i = 0; i < firstParentValue.get(0).size(); i++) { // todo sprawdz czy dobrze z tymi -1
                for (int k = 0; k < secondParentValue.get(0).size(); k++) {
                    Integer firstVertexFirstParent = firstParentValue.get(0).get(i);
                    Integer firstVertexFirstParentSecondCycle = firstParentValue.get(1).get(i);
                    Integer secondVertexFirstParent;
                    Integer secondVertexFirstParentSecondCycle;
                    Integer firstVertexSecondParent = secondParentValue.get(0).get(k);
                    Integer firstVertexSecondParentSecondCycle = secondParentValue.get(1).get(k);
                    Integer secondVertexSecondParent;
                    Integer secondVertexSecondParentSecondCycle;


                    if (i == firstParentValue.get(0).size() - 1 && k == secondParentValue.get(0).size() - 1) {
                        secondVertexFirstParent = firstParentValue.get(0).get(0);
                        secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(0);
                        secondVertexSecondParent = secondParentValue.get(0).get(0);
                        secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(0);
                    } else if (i == firstParentValue.get(0).size() - 1) {
                        secondVertexFirstParent = firstParentValue.get(0).get(0);
                        secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(0);
                        secondVertexSecondParent = secondParentValue.get(0).get(k + 1);
                        secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(k + 1);
                    } else if (k == secondParentValue.get(0).size() - 1) {
                        secondVertexFirstParent = firstParentValue.get(0).get(i + 1);
                        secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(i + 1);
                        secondVertexSecondParent = secondParentValue.get(0).get(0);
                        secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(0);
                    } else {
                        secondVertexFirstParent = firstParentValue.get(0).get(i + 1);
                        secondVertexFirstParentSecondCycle = firstParentValue.get(1).get(i + 1);
                        secondVertexSecondParent = secondParentValue.get(0).get(k + 1);
                        secondVertexSecondParentSecondCycle = secondParentValue.get(1).get(k + 1);
                    }

//sortowanie podwjnych krawedzi

                    if (firstVertexFirstParent > secondVertexFirstParent) {
                        int tmp = firstVertexFirstParent;
                        firstVertexFirstParent = secondVertexFirstParent;
                        secondVertexFirstParent = tmp;
                    }

                    if (firstVertexFirstParentSecondCycle > secondVertexFirstParentSecondCycle) {
                        int tmp = firstVertexFirstParentSecondCycle;
                        firstVertexFirstParentSecondCycle = secondVertexFirstParentSecondCycle;
                        secondVertexFirstParentSecondCycle = tmp;
                    }

                    if (firstVertexSecondParent > secondVertexSecondParent) {
                        int tmp = firstVertexSecondParent;
                        firstVertexSecondParent = secondVertexSecondParent;
                        secondVertexSecondParent = tmp;
                    }

                    if (firstVertexSecondParentSecondCycle > secondVertexSecondParentSecondCycle) {
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

            expandEdges(cycle_0);
            expandEdges(cycle_1);

            //pewtla po wierzcholkach, sprawdzenie czy wierzhcolek nalezy juz do jakiejs krawedzi
            List<List<Integer>> edges = new ArrayList<>();
            int verticesNumber = kro200.getDistanceMatrix().size();
            for (int vertex = 0; vertex < verticesNumber; vertex++) {
                boolean isVertexAssigned = false;
                for (List<Integer> edge : cycle_0) {
                    if (edge.contains(vertex)) {
                        isVertexAssigned = true;
                        break;
                    }
                }
                if (!isVertexAssigned) {
                    for (List<Integer> edge : cycle_1) {
                        if (edge.contains(vertex)) {
                            isVertexAssigned = true;
                            break;
                        }
                    }
                }
                if (!isVertexAssigned) {
                    List<Integer> newEdge = new ArrayList<>();
                    newEdge.add(vertex);
                    edges.add(newEdge);
                }
            }

            //dodanie bezpanskich krawedzi i stworzenie z najdluzszej cyklu
            List<Integer> cycle_0_final = createCycle(cycle_0, edges, firstParentValue, 0);
            List<Integer> cycle_1_final = createCycle(cycle_1, edges, firstParentValue, 1);

            greedyCycle(edges, cycle_0_final, cycle_1_final);

            List<List<Integer>> childValue = new ArrayList<>();
            childValue.add(cycle_0_final);
            childValue.add(cycle_1_final);
            childValue = new CandidatesMoves(kro200, childValue).cycles;

            Double childKey = calcCycleLength(cycle_0_final) + calcCycleLength(cycle_1_final);
            Double biggestKey = -1.0;
            for(Double key: population.keySet()) {
                if(key > biggestKey) {
                    biggestKey = key;
                }
            }
            if(childKey<biggestKey) {
                population.remove(biggestKey);
                population.put(childKey, childValue);
            }
        }
        Double smallestKey = Double.MAX_VALUE;
        for(Double key: population.keySet()) {
            if(key < smallestKey) {
                smallestKey = key;
            }
        }
        cycles_X = population.get(smallestKey);
        bestCyclesLength = smallestKey;
        System.out.println("iterations: " + iterations);
    }

    private static List<Integer> createCycle(List<List<Integer>> cycle_0, List<List<Integer>> edges, List<List<Integer>> firstParentValue, int cyclerNumber) {
        int maxLength = -1;
        int maxLengthIndex = -1;
        for (int i = 0; i< cycle_0.size(); i++) {
            int length = cycle_0.get(i).size();
            if(length>maxLength) {
                maxLength = length;
                maxLengthIndex = i;
            }
        }
        List<Integer> cycle_final = new ArrayList<>();
        for (int i = 0; i< cycle_0.size(); i++) {
            if(i==maxLengthIndex) {
                cycle_final = cycle_0.get(i);
            }
            else {
                edges.add(cycle_0.get(i));
            }
        }
        //gdy rodzice nie maja wspolnych krawedzi
        if(maxLengthIndex == -1) {
            int firstVertex = firstParentValue.get(cyclerNumber).get(0);
            int secondVertex = firstParentValue.get(cyclerNumber).get(1);
            cycle_final.add(firstVertex);
            cycle_final.add(secondVertex);
            edges.removeIf(j -> j.get(0).equals(firstVertex) || j.get(0).equals(secondVertex));
        }
        return cycle_final;
    }

    private static void expandEdges(List<List<Integer>> cycle) {
        List<Boolean> usedEdges = new ArrayList<>();
        for (int i = 0; i< cycle.size(); i++) {
            usedEdges.add(false);
        }
        for (int i = 0; i< cycle.size(); i++) {
            if(usedEdges.get(i)){
                continue;
            }
            List<Integer> edge = cycle.get(i);
            while (true) {
                boolean isEdgeExtended = false;
                int firstVertexInEdge = edge.get(0);
                int lastVertexInEdge = edge.get(edge.size() - 1);
                for (int j = i+1; j<cycle.size(); j++) {
                    List<Integer> newEdge = cycle.get(j);
                    if (usedEdges.get(j)) {
                        continue;
                    }
                    if (newEdge.get(0) == firstVertexInEdge) {
                        edge.add(0, newEdge.get(1));
                        isEdgeExtended = true;
                    } else if (newEdge.get(1) == firstVertexInEdge) {
                        edge.add(0, newEdge.get(0));
                        isEdgeExtended = true;
                    } else if (newEdge.get(0) == lastVertexInEdge) {
                        edge.add(newEdge.get(1));
                        isEdgeExtended = true;
                    } else if (newEdge.get(1) == lastVertexInEdge) {
                        edge.add(newEdge.get(0));
                        isEdgeExtended = true;
                    }
                    if (isEdgeExtended) {
                        usedEdges.set(j, true);
                        break;
                    }
                }
                if (!isEdgeExtended) {
                    break;
                }
            }
        }

        for(int i = usedEdges.size() - 1; i >= 0; i--) {
            if(usedEdges.get(i)) {
                cycle.remove(i);
            }
        }
    }

    private double calcCycleLength(List<Integer> solution){
        double length = 0;
        for(int i= 0; i<solution.size()-1; i++){
            length += distanceMatrix.get(solution.get(i)).get(solution.get(i+1));
        }
        length += distanceMatrix.get(solution.get(solution.size() - 1)).get(solution.get(0));
        return length;
    }


    private void greedyCycle(List<List<Integer>> edges, List<Integer> cycle0, List<Integer> cycle1){
        while (true){
            if(edges.isEmpty()) {
                break;
            }
            if(cycle0.size() <= cycle1.size()) {
                findAndAddBestVertex(edges, cycle0);
            }

            if(edges.isEmpty()) {
                break;
            }
            if(cycle1.size() <= cycle0.size()) {
                findAndAddBestVertex(edges, cycle1);
            }
        }
        while (cycle0.size() > cycle1.size()){
            transferVertex(cycle0, cycle1);
        }
        while (cycle1.size() > cycle0.size()){
            transferVertex(cycle1, cycle0);
        }
    }

    private void transferVertex(List<Integer> fromCycle, List<Integer> toCycle) {
        int resultNewVertex = -1;
        int resultNewVertexNumberInCycle = -1;
        long resultTotalCost = -1L;
        for(int i=0; i<toCycle.size()-1; i++) {
            int fromVertexToCycle = toCycle.get(i);
            int toVertexToCycle;
            if(i == toCycle.size()-1) {
                toVertexToCycle = toCycle.get(0);
            } else {
                toVertexToCycle = toCycle.get(i+1);
            }
            Integer oldCostFromCycle = distanceMatrix.get(fromVertexToCycle).get(toVertexToCycle);
            for(int transferredVertexIndex=0; transferredVertexIndex<fromCycle.size(); transferredVertexIndex++) {
                int transferredVertex = fromCycle.get(transferredVertexIndex);
                int fromVertexFromCycle;
                if(transferredVertexIndex == 0) {
                    fromVertexFromCycle = fromCycle.get(fromCycle.size()-1);
                } else {
                    fromVertexFromCycle = fromCycle.get(transferredVertexIndex-1);
                }
                Integer firstCostToCycle = distanceMatrix.get(fromVertexFromCycle).get(transferredVertex);
                int toVertexFromCycle;
                if(transferredVertexIndex == fromCycle.size()-1) {
                    toVertexFromCycle = fromCycle.get(0);
                } else {
                    toVertexFromCycle = fromCycle.get(transferredVertexIndex+1);
                }
                Integer secondCostToCycle = distanceMatrix.get(transferredVertex).get(toVertexFromCycle);
                Integer newCostToCycle = distanceMatrix.get(fromVertexFromCycle).get(toVertexFromCycle);

                Integer firstCostFromCycle = distanceMatrix.get(fromVertexToCycle).get(transferredVertex);
                Integer secondCostFromCycle = distanceMatrix.get(transferredVertex).get(toVertexToCycle);
                Integer newCost = firstCostFromCycle + secondCostFromCycle + newCostToCycle;
                Integer oldCost = oldCostFromCycle + firstCostToCycle + secondCostToCycle;
                long totalCost = newCost - oldCost;

                if(totalCost<resultTotalCost || resultTotalCost==-1) {
                    resultNewVertexNumberInCycle = i+1;
                    resultNewVertex = transferredVertex;
                    resultTotalCost = totalCost;
                }
            }
        }
        toCycle.add(-1);
        for(int i=toCycle.size()-2; i>=resultNewVertexNumberInCycle; i--) {
            toCycle.set(i+1, toCycle.get(i));
        }
        toCycle.set(resultNewVertexNumberInCycle, resultNewVertex);
        int finalResultNewVertex = resultNewVertex;
        fromCycle.removeIf(i -> i.equals(finalResultNewVertex));
    }

    private void findAndAddBestVertex(List<List<Integer>> edges, List<Integer> cycle) {
        List<Integer> resultNewEdge = new ArrayList<>();
        int resultNewEdgeNumberInCycle = -1;
        long resultTotalCost = -1L;
        for(int i=0; i<cycle.size(); i++) {
            int fromVertex = cycle.get(i);
            int toVertex;
            if(i == cycle.size()-1) {
                toVertex = cycle.get(0);
            } else {
                toVertex = cycle.get(i+1);
            }
            Integer oldCost = distanceMatrix.get(fromVertex).get(toVertex);
            for(List<Integer> newEdge: edges) {
                if(newEdge.size() == 1) { //wszczepienie krawedzi
                    int newVertex = newEdge.get(0);
                    Integer firstCost = distanceMatrix.get(fromVertex).get(newVertex);
                    Integer secondCost = distanceMatrix.get(newVertex).get(toVertex);
                    Integer newCost = firstCost + secondCost;
                    long totalCost = newCost - oldCost;

                    if(totalCost<resultTotalCost || resultTotalCost==-1) {
                        resultNewEdgeNumberInCycle = i+1;
                        resultNewEdge = newEdge;
                        resultTotalCost = totalCost;
                    }
                } else { //wszczepienie wierzcholka
                    Integer edgeFirstElement = newEdge.get(0);
                    Integer edgeLastElement = newEdge.get(newEdge.size()-1);

                    Integer firstCost = distanceMatrix.get(fromVertex).get(edgeFirstElement);
                    Integer secondCost = distanceMatrix.get(edgeLastElement).get(toVertex);
                    Integer newCost = firstCost + secondCost;
                    long totalCost = newCost - oldCost;

                    if(totalCost<resultTotalCost || resultTotalCost==-1) {
                        resultNewEdgeNumberInCycle = i+1;
                        resultNewEdge = newEdge;
                        resultTotalCost = totalCost;
                    }

                    List<Integer> newEdgeReversed = new ArrayList<>(newEdge);
                    Collections.reverse(newEdgeReversed);

                    edgeFirstElement = newEdgeReversed.get(0);
                    edgeLastElement = newEdgeReversed.get(newEdgeReversed.size()-1);

                    firstCost = distanceMatrix.get(fromVertex).get(edgeFirstElement);
                    secondCost = distanceMatrix.get(edgeLastElement).get(toVertex);
                    newCost = firstCost + secondCost;
                    totalCost = newCost - oldCost;

                    if(totalCost<resultTotalCost || resultTotalCost==-1) {
                        resultNewEdgeNumberInCycle = i+1;
                        resultNewEdge = newEdgeReversed;
                        resultTotalCost = totalCost;
                    }
                }
            }
        }

        for(int i=0; i<resultNewEdge.size(); i++){
            cycle.add(-1);
        }
        for(int i=cycle.size()-1-resultNewEdge.size(); i>=resultNewEdgeNumberInCycle; i--) {
            cycle.set(i+resultNewEdge.size(), cycle.get(i));
        }
        for(int i=0; i<resultNewEdge.size(); i++) {
            cycle.set(i+resultNewEdgeNumberInCycle, resultNewEdge.get(i));
        }

        Iterator<List<Integer>> iterator = edges.iterator();
        while (iterator.hasNext()) {
            List<Integer> edge = iterator.next();
            if(edge.equals(resultNewEdge)) {
                iterator.remove();
                break;
            }
            Collections.reverse(resultNewEdge);
            if(edge.equals(resultNewEdge)) {
                iterator.remove();
                break;
            }

        }
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


}
