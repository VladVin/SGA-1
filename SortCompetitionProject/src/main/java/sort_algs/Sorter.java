package sort_algs;

/**
 * Created by VladVin on 19.09.2016.
 */
public class Sorter {
    private enum Order { ASCENDING, DESCENDING, RANDOM, NONE }

    public double[] sort(double[] array) {
        // Determine order of elements in array
        Order order = Order.NONE;
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] <= array[i + 1]) {
                if (order == Order.ASCENDING || order == Order.NONE) {
                    order = Order.ASCENDING;
                } else {
                    order = Order.RANDOM;
                    break;
                }
            } else {
                if (order == Order.DESCENDING || order == Order.NONE) {
                    order = Order.DESCENDING;
                } else {
                    order = Order.RANDOM;
                    break;
                }
            }
        }

        // Choose sorting algorithm depending on elements order
        SortingAlgorithm sortAlg = null;
        switch (order) {
            case ASCENDING:
                sortAlg = SortAlgFabric.createAlgorithm(SortAlgFabric.SortAlgType.TIM_SORT);
                break;
            case DESCENDING:
                // TODO: Use radix sort algorithm
                break;
            case RANDOM:
                // TODO: Use radix sort algorithm
                break;
            default:
                return null;
        }

        // Sort array
        return sortAlg.sort(array);
    }
}
