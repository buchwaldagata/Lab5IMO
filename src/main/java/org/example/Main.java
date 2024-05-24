package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Instance kroA200 = new Instance("src/main/resources/kroA200.tsp");
//        Instance kroB200 = new Instance("src/main/resources/kroB200.tsp");
        long startTime = System.nanoTime();
        List<HAE> solutions = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
            solutions.add(new HAE(kroA200));
        }





    }
}