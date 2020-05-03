import os
from collections import defaultdict

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy import stats

DISTRIBUTIONS = {
    'exp' : "Exponential",
    'N': "Normal"
}


def plot_cdf(groupings, name):
    fig, ax = plt.subplots()
    for grouping in groupings:
        cdfs = grouping['cdfs']
        for cdf in cdfs:
            x = cdf.lowerlimit + np.linspace(0, cdf.binsize * cdf.cumcount.size, cdf.cumcount.size)
            ax.plot(x, np.divide(cdf.cumcount, cdf.cumcount[len(cdf.cumcount)-1]))
    ax.set(xlabel='x', ylabel='P(X ≤ x)', title=f'{name}')
    ax.set_ylim([0, 1.5])
    ax.set_xlim([0, 4096])
    ax.grid()
    ax.legend()
    fig.savefig(f'{name}.png')


samplers = defaultdict(list)
for file in os.listdir('../buckets'):
    key = file.strip('.csv')
    absolute = os.path.abspath(f'../buckets/{file}')
    df = pd.read_csv(absolute, header=None)
    samples = df.to_numpy().tolist()
    cdfs = []
    for sample in samples:
        cdfs.append(stats.cumfreq(sample, numbins=4096))
    samplers[key].append({
        'cdfs': cdfs,
        'sampler': key
    })
for name in samplers.keys():
    plot_cdf(samplers[name], name)
