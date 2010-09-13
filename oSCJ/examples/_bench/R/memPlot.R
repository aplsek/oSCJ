#! /usr/bin/env Rscript

args <- commandArgs()




data1 <- read.table(args[6])   # fiji - scj  - miniCdj
data2 <- read.table(args[7])   # fiji - rt gc CDj

data3 <- read.table(args[8])   # fiji - scj - emptyBench

range <- c(0,750)

range_y <- c(380,850)

# Columns of data: 
#   V1 - beforeDetect(ns) 
#   V2 - afterDetect(ns) 
#   V3 - detectedCollision 
#   V4 - suspectedCollision 
#   V5:7 -  0 0 0 
#   V8 - index
# cdc board: 1074228568000 1074228568000 0 0 0 0 0 929400 18446744073708622216 9

# for board



mem_scj  <- (data1$V2 + data1$V3 + data1$V4 + data1$V6) / 1000
mem_gc <- ( data2$V4 ) / 1000
scopes <- (data1$V5 + 500000) /1000
empty_bench <- (data3$V2 + data3$V3 + data3$V4 + data3$V6) / 1000

scj_heap <- ( data1$V4 + data1$V6) / 1000
time <- data.frame(mem_scj , mem_gc, scopes, scj_heap, empty_bench)

pdf("mem_bench.pdf",width=25,height=10)

type_a=c("l","l")
lty_a=c(1,1)

matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Time", ylab="Memory Consumption [kB]",lwd=3)

legend("bottomright", inset=.05, title="Legend",
   c("oSCJ - memory used \n(private + mission + immortal + heap)","Fiji VM - RT GC","oSCJ - total memory", "oSCJ - heap+immortal", "oSCJ - Empty Benchmark"), col=c("black","red","green", "blue", "lightskyblue"), fill=c("black","red","green","blue", "lightskyblue"), horiz=TRUE)

postscript()
#X11()	

max_mission <- max(data1$V3)
max_private <- max(data1$V2)
max_immortal <-  max(data1$V4)
max_heap <-  max(data1$V6)

max_empty <- max(empty_bench)

print("MAX EMPTY")
print(max_empty)

print("max private and mission:")
print(max_private)
print(max_mission)
print(max_immortal)
print(max_heap)


pr <- max_private
pr <- max_mission
pr <- max_immortal
pr <- max_heap
print("[  ...   ...    ....    ....]")
print(pr)


mean_scj <- mean(mem_scj)
mean_GC <- mean(mem_gc)
means <- data.frame(mean_scj,mean_GC)
print("----------------------------")
print("MEMORY USAGE: mean values")
print(means)
print(" ")
print(" ")
print("----------------------------")


