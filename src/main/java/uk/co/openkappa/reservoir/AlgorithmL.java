package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

import static java.lang.Math.exp;
import static java.lang.Math.log;

public class AlgorithmL implements ReservoirSampler {

    private final double[] reservoir;
    private long counter;
    private long next;
    private double w;

    public AlgorithmL(int capacity) {
        this.reservoir = new double[capacity];
        next = reservoir.length;
        w = exp(log(ThreadLocalRandom.current().nextDouble())/reservoir.length);
        skip();
    }


    @Override
    public void add(double value) {
        if (counter < reservoir.length) {
            reservoir[(int)counter] = value;
        } else {
            if (counter == next) {
                reservoir[ThreadLocalRandom.current().nextInt(reservoir.length)] = value;
                skip();
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

    private void skip() {
        next += (long)(log(ThreadLocalRandom.current().nextDouble())/ log(1-w)) + 1;
        w *= exp(log(ThreadLocalRandom.current().nextDouble())/reservoir.length);
    }

    double[] snapshot() {
        return reservoir;
    }
}
