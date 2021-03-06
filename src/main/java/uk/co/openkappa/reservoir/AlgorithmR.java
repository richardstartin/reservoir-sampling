package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

public class AlgorithmR implements ReservoirSampler {

    private final double[] reservoir;
    private long counter;

    public AlgorithmR(int size) {
        this.reservoir = new double[size];
    }

    @Override
    public void add(double value) {
        if (counter < reservoir.length) {
            reservoir[(int)counter] = value;
        } else {
            long replacementIndex = ThreadLocalRandom.current().nextLong(0, counter);
            if (replacementIndex < reservoir.length) {
                reservoir[(int)replacementIndex] = value;
            }
        }
        ++counter;
    }

    @Override
    public double mean() {
        double sum = 0;
        for (double v : reservoir) {
            sum += v;
        }
        return sum / reservoir.length;
    }

    @Override
    public void forEach(DoubleConsumer sampleConsumer) {
        for (double sample : reservoir) {
            sampleConsumer.accept(sample);
        }
    }

    double[] snapshot() {
        return reservoir;
    }
}
