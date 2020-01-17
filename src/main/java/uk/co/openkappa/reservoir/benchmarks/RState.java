package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.AlgorithmR;

public class RState extends ReservoirState {

    AlgorithmR algorithmR;
    @Setup(Level.Trial)
    public void init() {
        createData();
        algorithmR = new AlgorithmR(reservoirSize);
    }
}
