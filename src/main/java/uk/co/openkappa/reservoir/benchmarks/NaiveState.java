package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.partitioned.LemireNaive;

public class NaiveState extends BucketSamplingState {
  LemireNaive sampler;
  @Setup(Level.Trial)
  public void init() {
    createData();
    sampler = new LemireNaive();
  }
}
