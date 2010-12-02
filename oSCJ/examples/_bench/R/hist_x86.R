#! /usr/bin/env Rscript

args <- commandArgs(TRUE)
data1 <- read.table(args) 
#data1 <- read.table("data/_d.dat") 
#data2 <- read.table("data/_d.dat")


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

#print(time1)

# Draw
#quartz()


##############################
#   Utilities
#


# parse a file name, get name, heap size, and trigger
# the file name should be in format of name.heap.trigger
parse_filename = function(filename) {
	trig  = unlist(strsplit(filename,".",fixed = TRUE))
	trig2 = unlist(strsplit(trig,"/",fixed = TRUE))
	res = trig2[[3]]
	n = "histo_"
	nn = ".pdf"
	name <- paste(n,res,nn)
	return(name)
}


name = parse_filename(args)

pdf(name,width=7.2,height=5.4)
#postscript()
#X11()




#h2 <- hist(time1, plot=F, breaks=150)
#plot(h2$mids, h2$counts, type="h",xlim=xrange, ylim=yrange, xlab=" ", ylab=" ")

xrange <- c(0.6,1.6)
yrange <- c(1,1000)

h1 <- hist(time1, plot=F, breaks=c(seq(0,max(time1)+1, .01)))#

#
plot(h1$mids, h1$counts, type="h", xlim=xrange, log="y", xlab=" ", ylab=" ")


#h2 <- hist(time2, plot=F, breaks=150)
#plot(h2$mids, h2$counts, type="h", xlim=xrange, ylim=yrange, log="y")
#plot(h1$mids, h1$counts, type="h", xlim=xrange, ylim=yrange, log="y", xlab="Iteration time", ylab="Number of Samples", main="SCJ")
#plot(h2$mids, h2$counts, type="h", xlim=xrange, ylim=yrange, log="y", xlab="Iteration time", ylab="Number of Samples", main="C")

