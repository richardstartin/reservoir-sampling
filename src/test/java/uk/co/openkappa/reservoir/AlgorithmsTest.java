package uk.co.openkappa.reservoir;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AlgorithmsTest {

    private static double[] data;
    private static int sampleSize;
    private static double intensity;

    @BeforeClass
    public static void setup() {
        intensity = 0.1;
        sampleSize = 1000;
        data = IntStream.range(0, sampleSize * 1000)
                .mapToDouble(i -> expRV(intensity))
                .toArray();
        // sort to make sure that there is no position bias!
        Arrays.sort(data);
    }

    private static double expRV(double intensity) {
        return (-1.0 / intensity) * Math.log(ThreadLocalRandom.current().nextDouble());
    }


    @Test
    public void testAlgorithmL() {
        var l = new AlgorithmL(sampleSize);
        for (double value : data) {
            l.add(value);
        }
        double mean = l.mean();
        // should be very close to the intensity of the exponentially distributed input
        System.out.println(1/mean);
        assertEquals(1/mean, intensity, 0.01);
    }

    @Test
    public void testAlgorithmR() {
        var r = new AlgorithmR(sampleSize);
        for (double value : data) {
            r.add(value);
        }
        double mean = r.mean();
        // should be very close to the intensity of the exponentially distributed input
        System.out.println(1/mean);
        assertEquals(1/mean, intensity, 0.01);
    }

    @Test
    public void testAlgorithmX() {
        var x = new AlgorithmX(sampleSize);
        for (double value : data) {
            x.add(value);
        }
        double mean = x.mean();
        // should be very close to the intensity of the exponentially distributed input
        System.out.println(1/mean);
        assertEquals(1/mean, intensity, 0.01);
    }

    @Test
    public void testAlgorithmZ() {
        var z = new AlgorithmZ(sampleSize, 40);
        for (double value : data) {
            z.add(value);
        }
        double mean = z.mean();
        // should be very close to the intensity of the exponentially distributed input
        System.out.println(1/mean);
        assertEquals(1/mean, intensity, 0.01);
    }


}