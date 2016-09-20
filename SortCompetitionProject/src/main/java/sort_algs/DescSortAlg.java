package sort_algs;

/**
 * Created by VladVin on 20.09.2016.
 */
public class DescSortAlg implements SortingAlgorithm {
    public double[] sort(double[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            double tmp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = tmp;
        }

        return array;
    }
}
