#! /usr/bin/env Rscript

##############################
#  INPUT
#
args <- commandArgs()
data1 <- read.table(args[6])   # fiji - scj  
data2 <- read.table(args[7])   # fiji - rt gc 



##############################
#  COMPUTE
#
scj <- (data1$V2 - data1$V1)/1000000.0
fiji <- (data2$V2 - data2$V1)/1000000.0

time <- data.frame(scj, fiji)



##############################
#  PLOT
#
type_a=c("l","l")
lty_a=c(1,1)

range <- c(0,350)
range_y <- c(0.1,1)

pdf("perf_bench.pdf",width=25,height=10)
matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Iteration Number", ylab="Time [ms]",lwd=3)

legend("bottomright", inset=.05, title="Legend",
   c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)

postscript()
#X11()	



##############################
#  SOME STATISTICS
#
max_scj <- max(scj)
max_fiji <- max(fiji)

mean_scj <- mean(scj)
mean_fiji <- mean(fiji)



maxs <- data.frame(max_scj, max_fiji)
print("----------------------------")
print("PERFORMANCE:")
print("")
print("MAX values:")
print(maxs)
print("")

means <- data.frame(mean_scj, mean_fiji)
print("MEAN values:")
print(means)
print("")
print("")
print("----------------------------")