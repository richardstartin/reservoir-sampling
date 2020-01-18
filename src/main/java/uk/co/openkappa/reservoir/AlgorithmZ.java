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
        long s;
        double w = Math.exp((-Math.log(ThreadLocalRandom.current().nextDouble())/reservoir.length));
        double term = counter - reservoir.length + 1;
        while (true) {
            double u = ThreadLocalRandom.current().nextDouble();
            double x = counter * (w-1.0);
            s = (long)x;
            double lhs = Math.exp(Math.log(((u * (Math.pow((counter + 1)/term, 2)) * (term + s)/(counter + x))))/reservoir.length);
            double rhs = (((counter + x)/(term + s)) * term)/counter;
            if (lhs < rhs) {
                w = rhs/lhs;
                continue;
            }
            double y = ((u * (counter) + 1)/term * (counter + s + 1))/(counter + s);
            double numeratorLimit, denominator;
            if (s >= reservoir.length) {
                denominator = counter;
                numeratorLimit = term + s;
            } else {
                denominator = counter - reservoir.length + s;
                numeratorLimit = counter + 1;
            }
            double numerator = counter + s;
            while ( numerator > numeratorLimit) {
                y = (y * numerator)/denominator;
                denominator -= 1;
                numerator -= 1;
            }
            w = Math.exp((-Math.log(ThreadLocalRandom.current().nextDouble())/reservoir.length));
            if (Math.exp(Math.log(y)/reservoir.length) < (counter + x)/counter) {
                break;
            }
        }
        return s;
    }

    private long nextSkipLinearSearch() {
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
