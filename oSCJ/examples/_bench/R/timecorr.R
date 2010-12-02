#! /usr/bin/env Rscript

data1 <- read.table("data/cdc-x86-10k-60planes-60per_d.dat")   # c    
data2 <- read.table("data/mini-x86-10000-60planes-per60_d.dat") # scj 
#data3 <- read.table("data/scj6_d.dat") # rtsj

range <- c(2000,2200)

# Columns of data: 
#   V1 - beforeDetect(ns) 
#   V2 - afterDetect(ns) 
#   V3 - detectedCollision 
#   V4 - suspectedCollision 
#   V5:7 -  0 0 0 
#   V8 - index

# for board
time1 <- (data1$V2 - data1$V1)/1000000.0
# for x86
#time1 <- (data1$V2 - data1$V1)/1000.0 
time2 <- (data2$V2 - data2$V1)/1000000.0
#time3 <- (data3$V2 - data3$V1)/1000000.0
time <- data.frame(time1,time2)

type_a=c("l","l")
lty_a=c(1,1)
lty_a1=c(2,2)

# Draw
#quartz()
pdf(width=10,height=5)
#postscript()
#X11()

plot(time, xlim=range, type=type_a, lty=lty_a,xlab="Iteration", ylab="Milliseconds")
abline(h=max1, col="black", lty=lty_a1)
abline(h=max2, col="red", lty=lty_a1)
#abline(h=c(max1,min1,avg1), col="red", lty=4)
#abline(h=c(max2,min2,avg2), col="blue", lty=4)

print("C")
print(max1)
print(avg1)
print("SCJ")
print(max2)
print(avg2)