package uk.co.openkappa.reservoir.partitioned;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class LemireSmarter implements BucketSampler {
  @Override
  public int sample(int[] histo, int[] sample) {
    int sum = 0;
    for (int i : histo) {
      sum += i;
    }
    // build tree
    int l = 0;
    while ((1 << l) < histo.length) {
      l++;
    }
    int[] runninghisto = Arrays.copyOf(histo, histo.length);
    int level = 0;
    for (;
         (1 << level) < runninghisto.length; level++) {
      for (int z = (1 << level) - 1; z + (1 << level) < runninghisto.length; z += 2 * (1 << level)) {
        runninghisto[z + (1 << level)] += runninghisto[z];
      }
    }
    int maxlevel = level;
    int pos = 0;
    while (pos < sample.length) {
      int y = ThreadLocalRandom.current().nextInt(sum); // random integer in [0,sum)
      // select logarithmic time
      level = maxlevel;
      int position = (1 << level) - 1;
      int runningsum = 0;
      for (; level >= 0; level -= 1) {
        if (y > runningsum + runninghisto[position]) {
          runningsum += runninghisto[position];
          position += (1 << level) / 2;
        } else if (y == runningsum + runninghisto[position]) {
          runninghisto[position] -= 1;
          break;
        } else {
          runninghisto[position] -= 1;
          position -= (1 << level) / 2;
        }
      }
      sample[pos++] = position;
      sum -= 1;
    }
    return pos;
  }
}
