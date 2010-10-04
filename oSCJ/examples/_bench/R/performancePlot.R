#! /usr/bin/env Rscript

# ./R/performancePlot.R ./data/perf.dat ./data/perf.dat

##############################
#  INPUT
#
args <- commandArgs(TRUE);
 




##############################
#   PLOT
#
range <- c(0,300)
range_y <- c(0.1,0.7)
type_a=c("l","l")
lty_a=c(1,1)

max <- c()
mean <- c()



colors <- c("red","black","blue","green","yellow","brown", "darkred", "indianred", "indianred1", "indianred2", "indianred3", "indianred4", "mediumvioletred", "orangered", "orangered1", "orangered2", "orangered3", "orangered4", "palevioletred", "palevioletred1", "palevioletred2", "palevioletred3", "palevioletred4", "red1", "red2","red3", "red4", "violetred", "violetred1", "violetred2", "violetred3", "violetred4" )






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

souboru <- i

# TODO : FIX teh legend, its still statick...
#legend("bottomright", inset=.05, title="Legend",
#   	c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)
leg <- gsub("./tmp/output_","",args)
leg <- gsub("_d.dat","",leg)

legend("bottomright", inset=.05, title="Legend",
   	leg, col=colors, fill=colors, horiz=TRUE)


dev.off() 

print("----------- PERFORMANCE ---------")

print(args)
print(max)
print(mean)

print("----------- end ---------")








N <- 4
i <-0
pdf("box.pdf",width=25,height=10)


df <- data.frame(time = numeric(N),
    temp = numeric(N), pressure = numeric(N))


#boxplot(1,data=mean, main="Car Milage Data",
#   xlab=args, ylab="Miles Per Gallon") 
ss <- c()
fr <- c()
for (arg in args) {
    fil <- read.table(arg);   
    scj <- (fil$V2 - fil$V1)/1000000.0	
	
	df[i,arg] <- data.frame(scj)
	
	#box[i] <- scj
	#ss <- scj
	print(scj)
	i <- (i + 1) 
	
	
#boxplot(time,data=time, main="Car Milage Data",
#   xlab=leg, names=arg, ylab="Miles Per Gallon", add=TRUE )

}


N <- 1000
m <- matrix(nrow = N, ncol = souboru)
colnames(m) <- leg
i <- 0
for (arg in args) {
    fil <- read.table(arg);   
    scj <- (fil$V2 - fil$V1)/1000000.0	
	
	m[ ,i]  <- scj
	
     i <- i +1
}

# continue with this approach



#print("fff=-------------")
#print(m)


boxplot(m,data=m, main="Execution time",
   xlab=leg, ylab="GC configuration")



#fr < data.frame(fr,col.names(leg))

dev.off() 
