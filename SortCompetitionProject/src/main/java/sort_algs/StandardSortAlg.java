package sort_algs;

import java.util.Arrays;

/**
 * Created by VladVin on 20.09.2016.
 */
public class StandardSortAlg implements SortingAlgorithm {
    public double[] sort(double[] array) {
        Arrays.sort(array);
        return array;
    }
}
