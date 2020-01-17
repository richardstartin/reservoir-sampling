package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.AlgorithmZ;

public class ZState extends ReservoirState {

    AlgorithmZ algorithmZ;
    @Setup(Level.Trial)
    public void init() {
        createData();
        algorithmZ = new AlgorithmZ(reservoirSize, 40);
    }
}
