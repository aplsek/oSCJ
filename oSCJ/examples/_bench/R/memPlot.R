#! /usr/bin/env Rscript

args <- commandArgs(TRUE)



type_a=c("l","l")
lty_a=c(1,1)
range <- c(0,750)
range_y <- c(650,900)
pdf("mem_bench.pdf",width=25,height=10)
plot(1,type="n",ylim = range_y, xlim=range)


colors <- c("red","black","blue","green","yellow","brown")


i <- 0
for (arg in args) {
    data <- read.table(arg);   
    mem <- (data$V5) / 1000
	
	max <- c(max,max(mem))
	mean <- c(mean,mean(mem))

	time <- data.frame(mem)
	
	i <- i + 1
	matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Time", ylab="Memory Consumption [kB]",lwd=3, add=TRUE, col=colors[i])
}



args <- gsub("./tmp/output_","",args)
args <- gsub("_m.dat","",args)


legend("bottomright", inset=.05, title="Legend",
   	args, col=colors, fill=colors, horiz=TRUE)
   	
   	

dev.off() 

print("----------------------------")
print(args)

print("AMX: ")
print(max)

print("----------------------------")
print("MEMORY USAGE: mean values")
print(mean)
print("----------------------------")


