package sort_algs;

/**
 * Created by VladVin on 19.09.2016.
 */
public class SortAlgFabric {
    public enum SortAlgType { TIM_SORT }

    public static SortingAlgorithm createAlgorithm(SortAlgType algType) {
        switch (algType) {
            case TIM_SORT:
                return new TimSort();
            default:
                return null;
        }
    }
}
