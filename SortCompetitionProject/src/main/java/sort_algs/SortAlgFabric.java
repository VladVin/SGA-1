package sort_algs;

/**
 * Created by VladVin on 19.09.2016.
 */
public class SortAlgFabric {
    public enum SortAlgType {
        ASC_SORT,
        DESC_SORT,
        HEAP_SORT,
        STANDARD_SORT,
        RADIX_SORT }

    public static SortingAlgorithm createAlgorithm(SortAlgType algType) {
        switch (algType) {
            case ASC_SORT:
                return new AscSortAlg();
            case DESC_SORT:
                return new DescSortAlg();
            case HEAP_SORT:
                return new HeapSort();
            case STANDARD_SORT:
                return new StandardSortAlg();
            case RADIX_SORT:
                return new Radix();
            default:
                return null;
        }
    }
}
