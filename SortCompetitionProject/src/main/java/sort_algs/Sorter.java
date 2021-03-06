package sort_algs;

/**
 * Created by VladVin on 19.09.2016.
 */
public class Sorter {
    public enum Order { ASCENDING, DESCENDING, RANDOM, NONE }

    public double[] sort(double[] array) {
        // Determine order of elements in array
        Order order = determineOrder(array);

        // Choose sorting algorithm depending on elements order
        SortingAlgorithm sortAlg = null;
        switch (order) {
            case ASCENDING:
                sortAlg = SortAlgFabric.createAlgorithm(SortAlgFabric.SortAlgType.ASC_SORT);
                break;
            case DESCENDING:
                sortAlg = SortAlgFabric.createAlgorithm(SortAlgFabric.SortAlgType.DESC_SORT);
                break;
            case RANDOM:
                sortAlg = SortAlgFabric.createAlgorithm(SortAlgFabric.SortAlgType.HEAP_SORT);
                break;
            default:
                return null;
        }

        // Sort array
        return sortAlg.sort(array);
    }

    public Order determineOrder(double[] array) {
        Order order = Order.NONE;
        for (int i = 0; i < array.length - 1; i++) {
            double diff = array[i + 1] - array[i];
            if (diff == 0.0) {
                continue;
            }
            if (diff > Double.MIN_VALUE) {
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

        return order;
    }
}
