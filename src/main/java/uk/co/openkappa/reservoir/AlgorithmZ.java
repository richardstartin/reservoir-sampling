package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;

public class AlgorithmZ {
    private final double[] reservoir;
    private long counter;
    private long next;
    private final int threshold;

    public AlgorithmZ(int size, int threshold) {
        this.reservoir = new double[size];
        this.threshold = threshold;
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
        if (counter < threshold * reservoir.length) {
            return nextSkipLinearSearch();
        }
        double c = (double)(counter + 1)/(counter - reservoir.length + 1);
        while (true) {
            double u = ThreadLocalRandom.current().nextDouble();
            double x = ThreadLocalRandom.current().nextDouble();
            double g = counter * (Math.exp(x / reservoir.length) - 1);
            long s = (long)g;
            double h = ((double) reservoir.length / counter)
                    * Math.pow((double) (counter - reservoir.length + 1) / (counter + s - reservoir.length + 1), reservoir.length + 1);
            if (u <= h / (c * g)) {
                return s;
            }
        }
    }

    private long nextSkipLinearSearch() {
        long s = -1;
        double u = ThreadLocalRandom.current().nextDouble();
        double numerator = 1;
        double denominator = 1;
        do {
            ++s;
            numerator *= (counter + 1 - reservoir.length - s);
            denominator *= (double)(counter + 1 - s);
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
