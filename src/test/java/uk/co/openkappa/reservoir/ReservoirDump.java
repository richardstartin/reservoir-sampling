package uk.co.openkappa.reservoir;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class ReservoirDump {




    public static void main(String[] args) {
        run(generateExponential(0.1, 1000_000), 1000, "exp-0.1");
        run(generateExponential(0.2, 1000_000), 1000, "exp-0.2");
        run(generateExponential(0.5, 1000_000), 1000, "exp-0.5");
        run(generateNormal(0,1, 1000_000), 1000, "N-0-1");
        run(generateNormal(10,0.2, 1000_000), 1000, "N-10-0.2");
        run(generateNormal(50,100, 1000_000), 1000, "N-50-100");
    }

    private static void run(double[] data, int reservoirSize, String description) {

        {
            var sampler = new AlgorithmR(reservoirSize);
            for (double value : data) {
                sampler.add(value);
            }
            writeFile("R-" + reservoirSize + "-" + description, sampler.snapshot());
        }
        {
            var sampler = new AlgorithmX(reservoirSize);
            for (double value : data) {
                sampler.add(value);
            }
            writeFile("X-" + reservoirSize + "-" + description, sampler.snapshot());
        }
        {
            var sampler = new AlgorithmZ(reservoirSize, 10);
            for (double value : data) {
                sampler.add(value);
            }
            writeFile("Z-" + reservoirSize + "-" + description, sampler.snapshot());
        }
    }

    private static void writeFile(String name, double[] data) {
        try {
            Files.writeString(Paths.get("gen/"+ name + ".csv"), DoubleStream.of(data).mapToObj(Double::toString).collect(Collectors.joining("\n")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double[] generateExponential(double intensity, int size) {
        double[] data = new double[size];
        for (int i = 0; i < data.length; ++i) {
            data[i] = exp(intensity);
        }
        return data;
    }

    private static double[] generateNormal(double mean, double stddev, int size) {
        double[] data = new double[size];
        for (int i = 0; i < data.length; ++i) {
            data[i] = normal(mean, stddev);
        }
        return data;
    }

    private static double normal(double mean, double stddev) {
        return (stddev * ThreadLocalRandom.current().nextGaussian()) + mean;
    }

    private static double exp(double intensity) {
        return (-1.0 / intensity) * Math.log(ThreadLocalRandom.current().nextDouble());
    }
}
