package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Benchmark)
public class BucketSamplingState {

  @Param({"500", "1000", "2000"})
  int k;

  @Param({"4096", "8192"})
  int p;

  int[] buckets;
  int[] sample;
  public void createData() {
    sample = new int[k];
    buckets = new int[p];
    for (int i = 0; i <  buckets.length; ++i) {
      buckets[i] = ThreadLocalRandom.current().nextInt(100);
    }
  }
}
