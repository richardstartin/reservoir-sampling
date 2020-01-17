package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
public class ThreadLocalRandomBenchmark {

    @Param({"1000", "1000000", "1000000000"})
    long max;

    @Benchmark
    public long tlr() {
        return ThreadLocalRandom.current().nextLong(max);
    }
}
