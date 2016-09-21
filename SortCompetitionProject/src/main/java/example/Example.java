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
public class StartBenchmark {
    private static String PATH_TO_DATA = "doubles\\data.txt";

    public static void main(String[] args) {
        Sorter sorter = new Sorter();
        double[] array;
        try {
            array = loadData();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Sorter.Order order = sorter.determineOrder(array);
        System.out.println("Before: " + order.name());
        sorter.sort(array);
        order = sorter.determineOrder(array);
        System.out.println("After: " + order.name());

//        CaliperMain.main(Benchmark.class, args);
//        double[] array;
//        try {
//            array = loadData();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        Sorter sorter = new Sorter();
////        int count = 1000;
////        double[] arr = new double[count];
////        System.arraycopy(array, 0, arr, 0, count);
////        DescSortAlg descSortAlg = new DescSortAlg();
////        descSortAlg.sort(arr);
////        Radix radixSort = new Radix();
////        radixSort.sort(array);
////        HeapSort heapSort = new HeapSort();
////        heapSort.sort(array);
////        sorter.sort(arr);
//        Sorter.Order order = sorter.determineOrder(array);
//        int a = 0;
    }

    private static double[] loadData() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(PATH_TO_DATA));

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
