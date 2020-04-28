package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;
import uk.co.openkappa.reservoir.partitioned.LemireSmarter;

public class SmarterState extends BucketSamplingState {

  LemireSmarter sampler;
  @Setup(Level.Trial)
  public void init() {
    createData();
    sampler = new LemireSmarter();
  }
}
