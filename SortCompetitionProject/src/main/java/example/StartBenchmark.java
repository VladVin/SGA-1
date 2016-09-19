package example;

import becnhmark.Benchmark;
import com.google.caliper.runner.CaliperMain;

/**
 * Created by VladVin on 19.09.2016.
 */
public class StartBenchmark {
    public static void main(String[] args) {
        CaliperMain.main(Benchmark.class, args);
    }
}
