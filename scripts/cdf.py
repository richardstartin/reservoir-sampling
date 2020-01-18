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
    conf = name.split('-')
    reservoir_size = conf[0]
    distribution = DISTRIBUTIONS[conf[1]]
    parameters = conf[2:]
    fig, ax = plt.subplots()
    xmin = 1000000
    xmax = -1
    for grouping in groupings:
        cdf = grouping['cdf']
        x = cdf.lowerlimit + np.linspace(0, cdf.binsize * cdf.cumcount.size, cdf.cumcount.size)
        xmin = min(x.min(), xmin)
        xmax = max(x.max(), xmax)
        ax.plot(x, np.divide(cdf.cumcount, grouping['bincount']), label="Algorithm %s" % grouping['sampler'])
    ax.set(xlabel='x', ylabel='P(X ≤ x)', title=f'{distribution}({parameters}), Reservoir size = {reservoir_size}')
    ax.set_ylim([0, 1.5])
    ax.set_xlim([xmin, xmax])
    ax.grid()
    ax.legend()
    fig.savefig(f'{name}.png')


cdfs = defaultdict(list)
for file in os.listdir('../gen'):
    key = file[2:].strip('.csv')
    conf = file.strip('.csv').split('-')
    algorithm = conf[0]
    reservoir_size = conf[1]
    distribution = conf[2]
    parameters = conf[3:]
    absolute = os.path.abspath(f'../gen/{file}')
    df = pd.read_csv(absolute)
    samples = df.to_numpy()
    cdf = stats.cumfreq(samples, numbins=df.size)
    cdfs[key].append({
        'cdf': cdf,
        'dist': DISTRIBUTIONS[distribution],
        'reservoir_size': reservoir_size,
        'params': parameters,
        'sampler': algorithm,
        'bincount': df.size
    })
for name in cdfs.keys():
    plot_cdf(cdfs[name], name)
