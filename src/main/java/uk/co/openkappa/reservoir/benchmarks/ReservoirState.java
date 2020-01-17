package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
public class ReservoirState {

    @Param({"500", "1000", "2000"})
    int reservoirSize;

    @Param({"1000000", "5000000"})
    int inputSize;

    @Param({"0", "10", "100"})
    int costOfWork;

    double[] data;

    public void createData() {
        data = new double[inputSize];
        for (int i = 0; i < data.length; ++i) {
            data[i] = ThreadLocalRandom.current().nextDouble();
        }
    }
}
