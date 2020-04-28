package uk.co.openkappa.reservoir.partitioned;

public interface BucketSampler {

  int sample(int[] histogram, int[] sample);
}
