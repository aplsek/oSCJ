#! /usr/bin/env Rscript

##############################
#  INPUT
#
args <- commandArgs()
data1 <- read.table(args[6])   # fiji - scj  
data2 <- read.table(args[7])   # fiji - rt gc 


data_hg_level_a <- read.table(args[8])   # fiji - rt gc CDj hg level a
data_100k <- read.table(args[9])   # fiji - rt gc CDj hg level a


##############################
#  COMPUTE
#
scj <- (data1$V2 - data1$V1)/1000000.0
fiji <- (data2$V2 - data2$V1)/1000000.0


time_level_a <- (data_hg_level_a$V2  - data_hg_level_a$V1) / 1000000.0
time_100k <- (data_100k$V2 - data_100k$V1) /1000000.0



time <- data.frame(scj, fiji, time_level_a, time_100k)

time <- data.frame(scj, fiji)


##############################
#  PLOT
#
type_a=c("l","l")
lty_a=c(1,1)

range <- c(0,1000)
range_y <- c(0.1,0.7)

pdf("perf_bench.pdf",width=25,height=10)
matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Iteration Number", ylab="Time [ms]",lwd=3)

legend("bottomright", inset=.05, title="Legend",
   c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)

postscript()
#X11()	


##############################
#  PLOT
#
type_a=c("l","l")
lty_a=c(1,1)

range <- c(0,300)
range_y <- c(0.1,0.5)

pdf("perf_bench_close.pdf",width=25,height=10)
matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Iteration Number", ylab="Time [ms]",lwd=3)

legend("bottomright", inset=.05, title="Legend",
   c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)

postscript()



##############################
#  SOME STATISTICS
#
max_scj <- max(scj)
max_fiji <- max(fiji)
max_time_level_a <- max(time_level_a)
max_time_100k <- max(time_100k)

mean_scj <- mean(scj)
mean_fiji <- mean(fiji)
mean_level_a <- mean(time_level_a)
mean_time_100k <- mean(time_100k)


maxs <- data.frame(max_scj, max_fiji, max_time_level_a , max_time_100k)
print("----------------------------")
print("PERFORMANCE:")
print("")
print("MAX values:")
print(maxs)
print("")

means <- data.frame(mean_scj, mean_fiji, mean_level_a ,mean_time_100k)
print("MEAN values:")
print(means)
print("")
print("")
print("----------------------------")