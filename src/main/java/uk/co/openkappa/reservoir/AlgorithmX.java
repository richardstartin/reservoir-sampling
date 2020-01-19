package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmX {
    private final double[] reservoir;
    private long counter;
    private long next;

    public AlgorithmX(int size) {
        this.reservoir = new double[size];
        this.next = reservoir.length;
    }

    public void add(double value) {
        if (counter < reservoir.length) {
            reservoir[(int)counter] = value;
        } else if (next == counter) {
            int position = ThreadLocalRandom.current().nextInt(0, reservoir.length);
            reservoir[position] = value;
            next += nextSkip();
        }
        ++counter;
    }

    private long nextSkip() {
        long s = 0;
        double u = ThreadLocalRandom.current().nextDouble();
        double quotient = (double)(counter + 1 - reservoir.length)/(counter + 1);
        int i = 1;
        do {
            quotient *= (double)(counter + 1 + i - reservoir.length)/(counter + i + 1);
            ++s;
            ++i;
        } while (quotient > u);
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
