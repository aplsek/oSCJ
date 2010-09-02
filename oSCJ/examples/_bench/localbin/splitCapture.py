#!/usr/bin/python
from sys import argv

def splitCapture(capName):
    print capName
    cf=open(capName+".cap")
    ds=open(capName+"_d.dat",'w')
    rs=open(capName+"_r.dat",'w')
    ms=open(capName+"_m.dat",'w')
    isds=False
    isrs=False
    ismem=False
    while (True):
        line=cf.readline().strip()
        if (line==""): break
        if (line=="=====DETECTOR-STATS-START-BELOW===="):
            isds=True
            continue
        if (line=="=====DETECTOR-STATS-END-ABOVE===="):
            isds=False
            continue
        if (line=="=====DETECTOR-RELEASE-STATS-START-BELOW===="):
            isrs=True
            continue
        if (line=="=====DETECTOR-RELEASE-STATS-END-ABOVE===="):
            isrs=False
            continue
        if (line=="=====MEMORY-BENCH-STATS-END-ABOVE===="):
            ismem=False
            continue
        if (line=="=====MEMORY-BENCH-STATS-START-BELOW===="):
            ismem=True
            continue
        if (isrs):
            rs.write(line+"\n")
        if (isds):
            ds.write(line+"\n")
        if (ismem):
        	ms.write(line+"\n")
    cf.close()
    ds.close();
    rs.close();
    ms.close();

for i in argv[1:]:
    splitCapture(i.split(".cap")[0])
