package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import uk.co.openkappa.reservoir.AlgorithmL;
import uk.co.openkappa.reservoir.AlgorithmR;
import uk.co.openkappa.reservoir.AlgorithmX;
import uk.co.openkappa.reservoir.AlgorithmZ;

public class ReservoirSamplers {

    @Benchmark
    public AlgorithmR R(RState state) {
        AlgorithmR r = state.algorithmR;
        for (double v : state.data) {
            Blackhole.consumeCPU(state.costOfWork);
            r.add(v);
        }
        return r;
    }


    @Benchmark
    public AlgorithmX X(XState state) {
        AlgorithmX x = state.algorithmX;
        for (double v : state.data) {
            Blackhole.consumeCPU(state.costOfWork);
            x.add(v);
        }
        return x;
    }

    @Benchmark
    public AlgorithmZ Z(ZState state) {
        AlgorithmZ z = state.algorithmZ;
        for (double v : state.data) {
            Blackhole.consumeCPU(state.costOfWork);
            z.add(v);
        }
        return z;
    }

    @Benchmark
    public AlgorithmL L(LState state) {
        AlgorithmL l = state.algorithmL;
        for (double v : state.data) {
            Blackhole.consumeCPU(state.costOfWork);
            l.add(v);
        }
        return l;
    }
}
