package uk.co.openkappa.reservoir.partitioned;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class LemireNaive implements BucketSampler {

  @Override
  public int sample(int[] histogram, int[] sample) {
    int[] runningHistogram = Arrays.copyOf(histogram, histogram.length);
    int sum = 0;
    for (int i : histogram) {
      sum += i;
    }
    int pos = 0;
    while (pos < sample.length) {
      int y = ThreadLocalRandom.current().nextInt(sum);
      int runningSum = 0;
      int z = 0;
      for (; z < runningHistogram.length; z++) {
        runningSum += runningHistogram[z];
        if (y < runningSum) {
          break;
        }
      }
      sample[pos++] = z;
      runningHistogram[z] -= 1;
      sum -= 1;
    }
    return pos;
  }

  public static void demo() {
    int N = 4096;
    int M = 100;
    int[] histo = new int[N];
    for (int z = 0; z < histo.length; z++) {
      int k = ThreadLocalRandom.current().nextInt(M);
      histo[z] = k;
    }
    int k = 1000;
    var naiveSampler = new LemireNaive();
    var streamingSampler = new GeometricDistributionSampler();
    var smarterSampler = new LemireSmarter();
    int[] result = new int[k];
    long t1 = System.nanoTime();
    naiveSampler.sample(histo, result);
    long t2 = System.nanoTime();
    System.out.println(t2 - t1);
    System.out.println(Arrays.toString(result));
    long t3 = System.nanoTime();
    streamingSampler.sample(histo, result);
    long t4 = System.nanoTime();
    System.out.println(t4 - t3);
    System.out.println(Arrays.toString(result));
    long t5 = System.nanoTime();
    smarterSampler.sample(histo, result);
    long t6 = System.nanoTime();
    System.out.println(t6 - t5);
    System.out.println(Arrays.toString(result));

  }

  public static void main(String[] args) {
    demo();
  }
}
