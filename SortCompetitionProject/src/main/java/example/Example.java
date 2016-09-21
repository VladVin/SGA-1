package example;

import benchmark.Benchmark;
import sort_algs.Sorter;
//import com.google.caliper.runner.CaliperMain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by VladVin on 19.09.2016.
 */
public class Example {
    private static String[] PATH_TO_DATA = {
            "doubles\\data.txt",
            "doubles\\desc_sorted_data.txt",
            "doubles\\sorted_data.txt" };

    public static void main(String[] args) {
        Sorter sorter = new Sorter();

        for (String path : PATH_TO_DATA) {
            double[] array;
            try {
                array = loadData(path);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("Path: " + path);
            Sorter.Order order = sorter.determineOrder(array);
            System.out.println("Before: " + order.name());
            sorter.sort(array);
            order = sorter.determineOrder(array);
            System.out.println("After: " + order.name() + "\n");
        }

    }

    private static double[] loadData(String pathToData) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathToData));

        ArrayList<Double> doubles = new ArrayList<Double>();
        String line;
        while ((line = br.readLine()) != null) {
            double number;
            try {
                number = Double.parseDouble(line);
            } catch (NumberFormatException ex){
                continue;
            }
            doubles.add(number);
        }

        br.close();

        double[] array = new double[doubles.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = doubles.get(i);
        }

        return array;
    }
}
