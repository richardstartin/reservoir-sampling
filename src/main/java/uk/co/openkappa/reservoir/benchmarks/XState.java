package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.AlgorithmX;

public class XState extends ReservoirState {

    AlgorithmX algorithmX;
    @Setup(Level.Trial)
    public void init() {
        createData();
        algorithmX = new AlgorithmX(reservoirSize);
    }
}
