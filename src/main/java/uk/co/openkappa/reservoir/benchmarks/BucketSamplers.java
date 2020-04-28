package uk.co.openkappa.reservoir.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;

public class BucketSamplers {

  @Benchmark
  public int lemireSmarter(SmarterState state) {
    return state.sampler.sample(state.buckets, state.sample);
  }

  @Benchmark
  public int geometric(GeometricState state) {
    return state.sampler.sample(state.buckets, state.sample);
  }

  @Benchmark
  public int lemireNaive(NaiveState state) {
    return state.sampler.sample(state.buckets, state.sample);
  }

}
