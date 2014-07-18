#!/usr/bin/env python

from scipy.stats import beta
import sys

if len(sys.argv) < 2:
	print 'Usage: ./analyzeGameData.py inputFileName.hsres'
	sys.exit(0)

infile = open(sys.argv[1])
lines = infile.readlines()
infile.close()

nl = len(lines)

print 'nl = ' + str(nl)

winner = []
gduration = []
for line in lines:
    ls = line.split(",")
    winner.append(int(ls[0]))
    gduration.append(int(ls[1]))

n0 = winner.count(0)
n1 = winner.count(1)

n = n0 + n1

alpha = 0.05 #95% confidence



lB = beta.ppf(alpha * 0.5, n0, n - n0 + 1)
uB = beta.ppf(1.0 - alpha * 0.5, n0 + 1, n - n0)

print 'p0 win = ' + str(n0)
print 'p1 win = ' + str(n1)
print 'p0 win % = ' + str(float(n0) / float(n))
print 'p1 win % = ' + str(float(n1) / float(n))
print '95% conf: ' + str(lB) + ' < < ' + str(uB)

gd_dist = [0] * 50

for gd in gduration:
	gd_dist[gd] = gd_dist[gd] + 1.0
	
for datum in gd_dist:
	print datum / nl
	

