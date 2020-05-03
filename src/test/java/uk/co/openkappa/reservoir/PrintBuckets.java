package uk.co.openkappa.reservoir;

import uk.co.openkappa.reservoir.partitioned.BucketSampler;
import uk.co.openkappa.reservoir.partitioned.GeometricDistributionSampler;
import uk.co.openkappa.reservoir.partitioned.LemireNaive;
import uk.co.openkappa.reservoir.partitioned.LemireSmarter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class PrintBuckets {


  public static void main(String... args) throws IOException {
    int N = 4096;
    int M = 100;
    int[] histogram = new int[N];
    for (int z = 0; z < histogram.length; z++) {
      int k = ThreadLocalRandom.current().nextInt(M);
      histogram[z] = k;
    }
    int k = 1000;
    print(new LemireNaive(), histogram, new int[k]);
    print(new LemireSmarter(), histogram, new int[k]);
    print(new GeometricDistributionSampler(), histogram, new int[k]);
  }


  public static void print(BucketSampler sampler, int[] histogram, int[] result) throws IOException {
    String fileName = "buckets/" + sampler.getClass().getSimpleName() + ".csv";
    if (!Files.exists(Path.of(fileName))) {
      Files.createFile(Path.of(fileName));
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      for (int i = 0; i < 100; ++i) {
        sampler.sample(histogram, result);
        Arrays.sort(result);
        String line = Arrays.toString(result);
        writer.write(line.replaceAll("(\\[)|(\\])|(\\s+)", ""));
        writer.newLine();
      }
    }
  }
}
