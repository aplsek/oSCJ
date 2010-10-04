#! /usr/bin/env Rscript

# ./R/performancePlot.R ./data/perf.dat ./data/perf.dat

##############################
#  INPUT
#
args <- commandArgs(TRUE);
 




##############################
#   PLOT
#
range <- c(0,1000)
range_y <- c(0.1,0.7)
type_a=c("l","l")
lty_a=c(1,1)

max <- c()
mean <- c()

colors <- c("red","black","blue","green","yellow","brown")

pdf("perf_bench.pdf",width=25,height=10)
plot(1,type="n",ylim = range_y, xlim=range)
i <- 0

for (arg in args) {
    data <- read.table(arg);   
    scj <- (data$V2 - data$V1)/1000000.0	
	
	max <- c(max,max(scj))
	mean <- c(mean,mean(scj))

	time <- data.frame(scj)
	
	i <- (i + 1) 
	
	matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Iteration Number", ylab="Time [ms]",lwd=3,add=TRUE,col=colors[i])

}

# TODO : FIX teh legend, its still statick...
#legend("bottomright", inset=.05, title="Legend",
#   	c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)
args <- gsub("./tmp/output_","",args)
args <- gsub("_d.dat","",args)

legend("bottomright", inset=.05, title="Legend",
   	args, col=colors, fill=colors, horiz=TRUE)


dev.off() 

print("----------- end ---------")

print(args)
print(max)
print(mean)

print("----------- end ---------")

