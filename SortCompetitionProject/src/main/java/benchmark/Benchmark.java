package benchmark;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import sort_algs.Sorter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Benchmark extends SimpleBenchmark {
    @Param String pathToData;

    private double[] array;
    private double[] arrayCopy;
    private Sorter sorter;

    @Override
    protected void setUp() throws Exception {
        sorter = new Sorter();
        loadData();
        arrayCopy = new double[array.length];
        super.setUp();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("==========================================");
        new Runner().run(Benchmark.class.getName(), args[0]);
        System.out.println("\n\n==========================================");
        new Runner().run(Benchmark.class.getName(), args[1]);
        System.out.println("\n\n==========================================");
        new Runner().run(Benchmark.class.getName(), args[2]);
    }

    public void timeSortArray(int reps) {
        for (int i = 0; i < reps; i++) {
            System.arraycopy(array, 0, arrayCopy, 0, array.length);
            sorter.sort(arrayCopy);
        }
    }

    private void loadData() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(pathToData));

        ArrayList<Double> doubles = new ArrayList<>();
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
