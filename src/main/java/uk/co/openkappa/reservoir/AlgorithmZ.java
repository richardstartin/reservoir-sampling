package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

public class AlgorithmZ implements ReservoirSampler {
    private final double[] reservoir;
    private long counter;
    private long next;
    private final int threshold;

    public AlgorithmZ(int size, int threshold) {
        this.reservoir = new double[size];
        this.threshold = threshold;
        next = reservoir.length + nextSkip();
    }

    @Override
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

    @Override
    public void forEach(DoubleConsumer sampleConsumer) {
        for (double sample : reservoir) {
            sampleConsumer.accept(sample);
        }
    }

    @Override
    public double mean() {
        double sum = 0;
        for (double v : reservoir) {
            sum += v;
        }
        return sum / reservoir.length;
    }

    private long nextSkip() {
        if (counter <= threshold * reservoir.length) {
            return nextSkipLinearSearch();
        }
        double c = (double)(counter + 1)/(counter - reservoir.length + 1);
        while (true) {
            double u = ThreadLocalRandom.current().nextDouble();
            double x = counter * (Math.exp(ThreadLocalRandom.current().nextDouble() / reservoir.length) - 1);
            long s = (long)x;
            double g = (reservoir.length)/(counter + x)*Math.pow(counter/(counter + x), reservoir.length);
            double h = ((double) reservoir.length / (counter + 1))
                    * Math.pow((double) (counter - reservoir.length + 1) / (counter + s - reservoir.length + 1), reservoir.length + 1);
            if (u <= (c * g)/h) {
                return Math.max(1, s);
            }
            // slow path, need to check f
            double f = 1;
            for (int i = 0; i <= s; ++i) {
                f *= (double)(counter - reservoir.length + i)/(counter + 1 + i);
            }
            f *= reservoir.length;
            f /= (counter - reservoir.length);
            if (u <= (c * g)/f) {
                return Math.max(1, s);
            }
        }
    }

    private long nextSkipLinearSearch() {
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

    double[] snapshot() {
        return reservoir;
    }
}
