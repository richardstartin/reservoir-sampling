package uk.co.openkappa.reservoir;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleConsumer;

import static java.lang.Math.exp;
import static java.lang.Math.log;

public class AlgorithmZ implements ReservoirSampler {
    private final double[] reservoir;
    private long counter;
    private long next;
    private final int threshold;

    public AlgorithmZ(int size, int threshold) {
        this.reservoir = new double[size];
        this.threshold = threshold;
        next = reservoir.length;
        skip();
    }

    @Override
    public void add(double value) {
        if (counter < reservoir.length) {
            reservoir[(int)counter] = value;
        } else {
            if (next == counter) {
                int position = ThreadLocalRandom.current().nextInt(0, reservoir.length);
                reservoir[position] = value;
                skip();
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

    private void skip() {
        if (counter <= threshold * reservoir.length) {
            linearSearch();
        } else {
            double c = (double) (counter + 1) / (counter - reservoir.length + 1);
            double w = exp(-log(ThreadLocalRandom.current().nextDouble()) / reservoir.length);
            long s;
            while (true) {
                double u = ThreadLocalRandom.current().nextDouble();
                double x = counter * (w - 1D);
                s = (long) x;
                double g = (reservoir.length) / (counter + x) * Math.pow(counter / (counter + x), reservoir.length);
                double h = ((double) reservoir.length / (counter + 1))
                        * Math.pow((double) (counter - reservoir.length + 1) / (counter + s - reservoir.length + 1), reservoir.length + 1);
                if (u <= (c * g) / h) {
                    break;
                }
                // slow path, need to check f
                double f = 1;
                for (int i = 0; i <= s; ++i) {
                    f *= (double) (counter - reservoir.length + i) / (counter + 1 + i);
                }
                f *= reservoir.length;
                f /= (counter - reservoir.length);
                if (u <= (c * g) / f) {
                    break;
                }
                w = exp(-log(ThreadLocalRandom.current().nextDouble()) / reservoir.length);
            }
            next += s + 1;
        }
    }

    private void linearSearch() {
        long s = 0;
        double u = ThreadLocalRandom.current().nextDouble();
        double quotient = (double)(counter + 1 - reservoir.length)/(counter + 1);
        int i = 1;
        do {
            quotient *= (double)(counter + 1 + i - reservoir.length)/(counter + i + 1);
            ++s;
            ++i;
        } while (quotient > u);
        next += s + 1;
    }

    double[] snapshot() {
        return reservoir;
    }
}
