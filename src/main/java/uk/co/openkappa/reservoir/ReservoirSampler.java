package uk.co.openkappa.reservoir;

import java.util.function.DoubleConsumer;

public interface ReservoirSampler {
    void add(double value);

    double mean();

    void forEach(DoubleConsumer sampleConsumer);
}
