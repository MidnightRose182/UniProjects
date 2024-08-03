# -*- coding: utf-8 -*-
"""
---------------------------------------------------------------------------------------------------------------------------------------
Importing Data
"""

import numpy as np
import matplotlib.pyplot as plt
from scipy import stats

files_list = ["P01.csv","P02.csv","P03.csv","P04.csv","P05.csv","P06.csv","P07.csv","P08.csv","P09.csv","P10.csv","P11.csv","P12.csv"]
database =[] #all data collected

for files in files_list:
    data = np.loadtxt(files, delimiter = ',', skiprows = (1), usecols=(1,4,6,8))
    database.append(data)

database = np.concatenate(database[:])

"""
columns:
0 = grating fequency (can find out whether trials are auditory or visual)
1 = response accuracy (1 = correct, 0 = incorrect)
2 = response time
3 = condition (1 = bimodal, 0 = unimodal)
---------------------------------------------------------------------------------------------------------------------------------------
Classification
"""

uni_data = database[database[:,3] == 0] #unimodal data
bi_data = database[database[:,3] == 1] #bimodal data
cor_uni = uni_data[uni_data[:,1] == 1] #correct unimodal data
cor_bi = bi_data[bi_data[:,1] == 1] #correct bimodal data
incor_uni = uni_data[uni_data[:,1] == 0] #incorrect unimodal data
incor_bi = bi_data[bi_data[:,1] == 0] #incorrect bimodal data

audi_uni = np.concatenate((uni_data[uni_data[:,0] == 3], uni_data[uni_data[:,0] == 0])) #unimodal audio trials
audi_bi = np.concatenate((bi_data[bi_data[:,0] == 3], bi_data[bi_data[:,0] == 0])) #bimodal audio trials
vis_uni = np.concatenate((uni_data[uni_data[:,0] == 6], uni_data[uni_data[:,0] == 2])) #unimodal visual trials
vis_bi = np.concatenate((bi_data[bi_data[:,0] == 6], bi_data[bi_data[:,0] == 2])) #bimodal visual trials

conditions = [uni_data, bi_data, cor_uni, cor_bi, incor_uni, incor_bi, audi_uni, audi_bi, vis_uni, vis_bi]

"""
---------------------------------------------------------------------------------------------------------------------------------------
Statistical analysis
"""
statistics = {}
names =["Uni", "Bi", "Correct Uni", "Correct Bi", "Incorrect Uni", "Incorrect Bi", "Audio Uni", "Audio Bi", "Visual Uni", "Visual Bi"]

for num in range(10):
    meanAcc = np.mean(conditions[num], axis = 0)[1]
    meanRT = np.mean(conditions[num], axis = 0)[2]
    SDAcc = np.std(conditions[num], axis = 0)[1]
    SDRT = np.std(conditions[num], axis = 0)[2]
    statistics[names[num]] = (meanAcc, meanRT, SDAcc, SDRT)

#Large standard deviation within unimodal data likely due to one or two outliers above 10 seconds

normal_dist = []
non_normal_dist = []

for name in conditions:
    if stats.shapiro(name[:,2])[1] >= 0.05:
        normal_dist.append(name)
    else:
        non_normal_dist.append(name)
        

"""When running the data through a shapiro wilk test, all conditions get added to the non-normal
distribution list, at a significance level of 0.05.

Furthermore, when looking at the plots, we can see that the accuracy data has a positive skew and therefore 
data is non paramteric.

Dataset with lowest mean reaction time: Incorrect Uni - this could imply that that the mistakes were from 
rushing. Not only this, but it also has the lowest standard deviation in reaction times.
---------------------------------------------------------------------------------------------------------
Pairwise comparisons

The pairwise comparisons that will be done are:
Uni - Bi (RT & Accuracy)
Correct Uni - Correct Bi (RT)
Audio Bi - Visual Bi (RT & Accuracy)

Tests will bedone with the Wilcoxon signed rank test
"""

print(stats.wilcoxon(uni_data[:,2], bi_data[:,2])) # Test for RT
print(stats.wilcoxon(uni_data[:,1], bi_data[:,1])) # Test for accuracy

cor_uni = cor_uni[0:1074] #Array size needs to be the same for the test to work
print(stats.wilcoxon(cor_uni[:,2], cor_bi[:,2])) # Test for RT

print(stats.wilcoxon(audi_bi[:,2], vis_bi[:,2])) # Test for RT
print(stats.wilcoxon(audi_bi[:,1], vis_bi[:,1])) #Test for accuracy
