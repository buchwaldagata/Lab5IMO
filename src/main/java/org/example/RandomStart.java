package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomStart {

    private final List<Integer> cycleA;
    private final List<Integer> cycleB;
    RandomStart(){
        cycleA=new ArrayList<>();
        cycleB=new ArrayList<>();
        getRandomSolution();
    }

    public List<List<Integer>> getCycles(){
        return new ArrayList<>(Arrays.asList(cycleA, cycleB));
    }
    private void getRandomSolution(){
        List<Integer> vertices = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            vertices.add(i);
        }
        for(int i=vertices.size()-1; i>=0 ; i-=2 ){
            int indexA = getRandomNumber(i);
            cycleA.add(vertices.get(indexA));
            vertices.remove(indexA);
            int indexB = getRandomNumber(i-1);
            cycleB.add(vertices.get(indexB));
            vertices.remove(indexB);
        }
    }

    private int getRandomNumber(int max){
        Random random = new Random();
        return random.nextInt(max + 1);
    }

}
