#! /usr/bin/env Rscript

args <- commandArgs(TRUE)



type_a=c("l","l")
lty_a=c(1,1)
range <- c(0,750)
range_y <- c(650,900)
pdf("mem_bench.pdf",width=25,height=10)
plot(1,type="n",ylim = range_y, xlim=range)



colors <- c("red","black","blue","green","yellow","brown", "darkred", "indianred", "indianred1", "indianred2", "indianred3", "indianred4", "mediumvioletred", "orangered", "orangered1", "orangered2", "orangered3", "orangered4", "palevioletred", "palevioletred1", "palevioletred2", "palevioletred3", "palevioletred4", "red1", "red2","red3", "red4", "violetred", "violetred1", "violetred2", "violetred3", "violetred4" )


max_t <- c()
avg <- c()

i <- 0
for (arg in args) {
    data <- read.table(arg);   
    mem <- (data$V4) / 1000
	
	max_t <- c(max_t,max(mem))
	avg <- c(avg,mean(mem))

	time <- data.frame(mem)
	
	i <- i + 1
	matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Time", ylab="Memory Consumption [kB]",lwd=3, add=TRUE, col=colors[i])
}



args <- gsub("./tmp/output_","",args)
args <- gsub("_m.dat","",args)


legend("bottomright", inset=.05, title="Legend",
   	args, col=colors, fill=colors, horiz=TRUE)
   	
   	

dev.off() 

print("------------ MEMORY USAGE ----------------")
print(args)

print("MAX: ")
print(max_t)

print("----------------------------")
print("MEAN")
print(avg)
print("----------------------------")


