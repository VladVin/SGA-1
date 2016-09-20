package sort_algs;

/**
 * Created by VladVin on 20.09.2016.
 */
public class HeapSort implements SortingAlgorithm {
    public double[] sort(double[] a) {
        heapify(a);

        int count = a.length;
        int end = count - 1;
        while (end > 0) {
            swap(a, 0, end);
            end--;
            siftDown(a, 0, end);
        }

        return a;
    }

    private static void heapify(double[] a) {
        int count = a.length;
        int start = (count - 2) / 2;
        while (start >= 0) {
            siftDown(a, start, count - 1);
            start--;
        }
    }

    private static void siftDown(double[] a, int start, int end) {
        int root = start;
        while (root * 2 + 1 <= end) {
            int child = root * 2 + 1;
            if (child + 1 <= end && a[child] < a[child + 1]) {
                child++;
            }
            if (a[root] < a[child]) {
                swap(a, root, child);
                root = child;
            } else {
                return;
            }
        }
    }

    private static void swap(double[] a, int idx1, int idx2) {
        double tmp = a[idx1];
        a[idx1] = a[idx2];
        a[idx2] = tmp;
    }
}
