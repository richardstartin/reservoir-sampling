package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmX {
    private final double[] reservoir;
    private long counter;
    private long next;

    public AlgorithmX(int size) {
        this.reservoir = new double[size];
        next = reservoir.length + nextSkip();
    }

    public void add(double value) {
        if (counter < reservoir.length) {
            reservoir[(int)counter] = value;
        } else {
            if (next == counter) {
                int position = ThreadLocalRandom.current().nextInt(0, reservoir.length);
                reservoir[position] = value;
                next += nextSkip();
            }
        }
        ++counter;
    }

    private long nextSkip() {
        long s = 0;
        double u = ThreadLocalRandom.current().nextDouble();
        double numerator = 1;
        double denominator = 1;
        do {
            numerator *= (Math.max(counter, reservoir.length) + 1 - reservoir.length - s);
            denominator *= (double)(counter + 1 - s);
            ++s;
        } while (numerator/denominator > u);
        return s;
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
