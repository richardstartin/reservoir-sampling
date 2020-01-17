package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmR {

    private final double[] reservoir;
    private long counter;

    public AlgorithmR(int size) {
        this.reservoir = new double[size];
    }

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

    public double mean() {
        double sum = 0;
        for (double v : reservoir) {
            sum += v;
        }
        return sum / reservoir.length;
    }

    double[] snapshot() {
        return reservoir;
    }
}
