#!/usr/bin/env python

from scipy.stats import beta
import sys
import json

class GameRecord(object):
    def __init__(self, gameRecordJson):
        self.json_ = gameRecordJson
        
    def getNums(self, turn, player_code, key):
        summary = self.json_[player_code]
        if turn >= len(summary):
            return 0
        return summary[turn][key]
        
    def getNumMinions_p0(self, turn):
        return self.getNums(turn, "p0", "p0_m")

    def getNumMinions_p1(self, turn):
        return self.getNums(turn, "p1", "p1_m")

    def getNumCards_p0(self, turn):
        return self.getNums(turn, "p0", "p0_c")

    def getNumCards_p1(self, turn):
        return self.getNums(turn, "p1", "p1_c")

    def getHealth_p0(self, turn):
        return self.getNums(turn, "p0", "p0_h")

    def getHealth_p1(self, turn):
        return self.getNums(turn, "p1", "p1_h")

class GameRecordSet(object):
    def __init__(self):
        self.results = []
        self.numGames = 0
        
    def add(self, gameResultJson):
        self.results.append(gameResultJson)
        self.numGames = self.numGames + 1
        
    def getP0WinCount(self):
        return reduce(lambda x, y: x + (y["winner"] + 1) % 2, self.results, 0)
        
    def getP1WinCount(self):
        return reduce(lambda x, y: x + y["winner"], self.results, 0)
    
    def getAverageNumbers(self, turn, player_code, key):
        nc = 0
        for res in self.results:
            gr = GameRecord(res["record"])
            nc = nc + gr.getNums(turn, player_code, key)
        return float(nc) / float(self.numGames)

    def getAverageNumMinions_p0(self, turn):
        return self.getAverageNumbers(turn, "p0", "p0_m")

    def getAverageNumMinions_p1(self, turn):
        return self.getAverageNumbers(turn, "p1", "p1_m")

    def getAverageNumCards_p0(self, turn):
        return self.getAverageNumbers(turn, "p0", "p0_c")

    def getAverageNumCards_p1(self, turn):
        return self.getAverageNumbers(turn, "p1", "p1_c")

    def getAverageHealth_p0(self, turn):
        return self.getAverageNumbers(turn, "p0", "p0_h")

    def getAverageHealth_p1(self, turn):
        return self.getAverageNumbers(turn, "p1", "p1_h")
    
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
record = []
grSet = GameRecordSet()
for line in lines:
    ljson = json.loads(line)
    grSet.add(ljson)
    gduration.append(ljson['duration'])

n0 = grSet.getP0WinCount()
n1 = grSet.getP1WinCount()

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
    
for i in range(50):
    print i, gd_dist[i] / nl, grSet.getAverageNumMinions_p0(i), grSet.getAverageNumMinions_p1(i), grSet.getAverageNumCards_p0(i), grSet.getAverageNumCards_p1(i), grSet.getAverageHealth_p0(i), grSet.getAverageHealth_p1(i)
    