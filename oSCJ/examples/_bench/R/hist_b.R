#! /usr/bin/env Rscript

args <- commandArgs(TRUE)
data1 <- read.table(args) 
#data1 <- read.table("data/_d.dat") 
#data2 <- read.table("data/_d.dat")

xrange <- c(20,120)
yrange <- c(1,2000)
#xrange <- NULL
#yrange <- NULL

# Columns of data: 
#   V1 - beforeDetect(ns) 
#   V2 - afterDetect(ns) 
#   V3 - detectedCollision 
#   V4 - suspectedCollision 
#   V5:7 -  0 0 0 
#   V8 - index

time1 <- (data1$V2 - data1$V1)/1000000.0
#time2 <- (data2$V2 - data2$V1)/1000000.0

# Draw
#quartz()
pdf(width=7.2,height=5.4)
#postscript()
#X11()

h1 <- hist(time1, plot=F, breaks=50)
#h2 <- hist(time2, plot=F, breaks=150)
plot(h1$mids, h1$counts, type="h", xlim=xrange, ylim=yrange, log="y", xlab=" ", ylab=" ")
#plot(h2$mids, h2$counts, type="h", xlim=xrange, ylim=yrange, log="y")
#plot(h1$mids, h1$counts, type="h", xlim=xrange, ylim=yrange, log="y", xlab="Iteration time", ylab="Number of Samples", main="SCJ")
#plot(h2$mids, h2$counts, type="h", xlim=xrange, ylim=yrange, log="y", xlab="Iteration time", ylab="Number of Samples", main="C")
