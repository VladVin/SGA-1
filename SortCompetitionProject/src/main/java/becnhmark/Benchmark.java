package becnhmark;

import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import sort_algs.Sorter;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by VladVin on 19.09.2016.
 */
public class Benchmark {
    @Param
    private String pathToData;

    private double[] array;
    private double[] arrayCopy;
    private Sorter sorter;


    @BeforeExperiment
    public void setUp() throws IOException {
        sorter = new Sorter();
        loadData();
        arrayCopy = new double[array.length];
    }

    @com.google.caliper.Benchmark
    public void timeSortArray(int reps) {
        for (int i = 0; i < reps; i++) {
            System.arraycopy(array, 0, arrayCopy, 0, array.length);
            sorter.sort(arrayCopy);
        }
    }

    private void loadData() throws IOException {
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

        array = new double[doubles.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = doubles.get(i);
        }
    }
}
