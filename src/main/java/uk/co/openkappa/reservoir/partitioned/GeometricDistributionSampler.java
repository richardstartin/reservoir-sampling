package uk.co.openkappa.reservoir.partitioned;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.log;

public class GeometricDistributionSampler implements BucketSampler {

  @Override
  public int sample(int[] histogram, int[] sample) {
    int[] runningHistogram = Arrays.copyOf(histogram, histogram.length);
    for (int i = 1; i < runningHistogram.length; ++i) {
      runningHistogram[i] += runningHistogram[i-1];
    }
    double remainingToSample = sample.length;
    double remainingToSampleFrom = runningHistogram[runningHistogram.length - 1];
    int pos = 0;
    int bucket = 0;
    double p = remainingToSample/remainingToSampleFrom;
    int nextItem = (int)(log(ThreadLocalRandom.current().nextDouble())/log(1-p)) + 1;
    while (pos < sample.length) {
      while (bucket < runningHistogram.length
              && nextItem >= runningHistogram[bucket]) {
        ++bucket;
      }
      sample[pos++] = bucket;
      int gap = (int)(log(ThreadLocalRandom.current().nextDouble())/log(1-p)) + 1;
      nextItem += gap;
      remainingToSampleFrom -= gap;
      --remainingToSample;
      p = remainingToSample/remainingToSampleFrom;
    }
    return pos;
  }
}
