package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.AlgorithmL;
import uk.co.openkappa.reservoir.AlgorithmR;

public class LState extends ReservoirState {

    AlgorithmL algorithmL;
    @Setup(Level.Trial)
    public void init() {
        createData();
        algorithmL = new AlgorithmL(reservoirSize);
    }
}
