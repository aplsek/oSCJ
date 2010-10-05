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



N <- 0


pdf("perf_bench.pdf",width=25,height=10)
plot(1,type="n",ylim = range_y, xlim=range)
i <- 0

for (arg in args) {
    data <- read.table(arg);   
    scj <- (data$V2 - data$V1)/1000000.0	
	
	max <- c(max,max(scj))
	mean <- c(mean,mean(scj))

	time <- data.frame(scj)
	N <- length(scj)
	i <- (i + 1) 
	
	matplot(time,xlim=range, ylim=range_y, type=type_a, lty=lty_a, xlab="Iteration Number", ylab="Time [ms]",lwd=3,add=TRUE,col=colors[i])

}

file_count <- i 



# TODO : FIX teh legend, its still statick...
#legend("bottomright", inset=.05, title="Legend",
#   	c("oSCJ","Fiji VM - RT GC"), col=c("black","red"), fill=c("black","red"), horiz=TRUE)
leg <- gsub("./tmp/","",args)
leg <- gsub("_d.dat","",leg)

legend("bottomright", inset=.05, title="Legend",
   	leg, col=colors, fill=colors, horiz=TRUE)


dev.off() 

print("----------- PERFORMANCE ---------")

print(args)
print(max)
print(mean)

#out <- matrix(nrow = 3, ncol = lines)
#out[,1] <- leg
#out[,2] <- max
#out[,2] <- mean
#write(out, "", sep = "\t")
#unlink("data") # tidy up


print("----------- end ---------")





f_parse <- function(arg) {
	tmp <- gsub("./tmp/","",arg)
	tmp <- gsub("_d.dat","",tmp)
	#print(tmp)
	sp <- strsplit(tmp,"_",fixed = TRUE)
	sp1 <- unlist(sp)
	heap <- sp1[3]
	heap <- unlist(strsplit(heap,".",fixed = TRUE))
	return(heap)
}








hp <- 0
tg <- 0
triggers <- c()
heaps <- c()


stats <- matrix(nrow = file_count, ncol = 4)
i <- 1
line <- 1
k <- 0
K<-0
for (arg in args) {
    fil <- read.table(arg);   
    scj <- (fil$V2 - fil$V1)/1000000.0	
	
	#m[ ,i]  <- scj
	avg <- mean(scj)
	max <- max(scj)
	#print(max)

	heap <- f_parse(arg)	
	
	stats[line,1] <- as.integer(heap[2])
	stats[line,2] <- as.integer(heap[3])
	stats[line,3] <- avg
	stats[line,4] <- max
	
	if (hp == as.integer(heap[2])) {
	  k <- k +1
	}
	
	if (hp < as.integer(heap[2])) {
       if (K == 0 && hp > 0) {
         K <- k +1
       }
       
       heaps <- c(heaps,as.integer(heap[2]))
       hp <- as.integer(heap[2])
       
	}
	
	if (tg < as.integer(heap[3])) {
       triggers <- c(triggers,as.integer(heap[3]))
        tg <- as.integer(heap[3])
	}
      
     line <- line + 1                  
     i <- i +1
}

#print(stats)
#print(K)

pdf("heap-avg.pdf",width=25,height=10)
range_x <- c(heaps[1],hp)
range_y <- c(0.4,0.8)
type_a=c("l","l")
lty_a=c(1,1)
trig_label<-c()

plot(1,type="n",ylim = range_y,xlim=range_x, ylab="Average Execution Time [ms]",xlab="Heap Size")

for (k in 1:K) { 
    vec1 <- c()  
    vec2 <- c()  
   points <- line / K
   #put <- matrix(nrow = points, ncol = 2)
   index <- 1 + (k-1)
   tt <- c(as.character(stats[index,2]))
   trig_label <- c(trig_label,tt)
   for (i in 1:points) {
  	   vec1 <- c(vec1,c(stats[index,1]))
	   vec2 <- c(vec2,c(stats[index,3]))
   	  #print(index)
   	  index <- index + K 
   	  
   	  
   }
   #print(vec1)
   #print(vec2)
   
   matplot(vec1,vec2, xlim=range_x, ylim=range_y,type=type_a, lty=lty_a,lwd=3,add=TRUE,col=colors[k])
}

legend("topright",trig_label, inset=.05, title="Legend - Trigger values", col=colors, fill=colors, horiz=TRUE)
   	
dev.off() 



pdf("heap-max.pdf",width=25,height=10)
range_x <- c(heaps[1],hp)
range_y <- c(0.4,0.8)
type_a=c("l","l")
lty_a=c(1,1)
plot(1,type="n",ylim = range_y,xlim=range_x, ylab="Max Execution Time [ms]", xlab="Heap Size")
trig_label <- c()
for (k in 1:K) { 
    vec1 <- c()  
    vec2 <- c()  
   points <- line / K
   index <- 1 + (k-1)
   tt <- c(as.character(stats[index,2]))
   trig_label <- c(trig_label,tt)
   for (i in 1:points) {
  	   vec1 <- c(vec1,c(stats[index,1]))
	   vec2 <- c(vec2,c(stats[index,4]))
   	  index <- index + K
   }
   matplot(vec1,vec2, xlim=range_x, ylim=range_y,type=type_a, lty=lty_a,lwd=3,add=TRUE,col=colors[k])
}

legend("topright", inset=.05, title="Legend - Trigger values",
   	trig_label, col=colors, fill=colors, horiz=TRUE)
   	
dev.off() 








print("----------- BOX PLOT ----------------------------- ---------")



pdf("box.pdf",width=25,height=10)



m <- matrix(nrow = N, ncol = file_count)
i <- 1
for (arg in args) {
    fil <- read.table(arg);   
    scj <- (fil$V2 - fil$V1)/1000000.0	
	
	m[ ,i]  <- scj
	
     i <- i +1
}
colnames(m) <- leg
# continue with this approach



#print("fff=-------------")
#print(m)
par(mar=c(5, 4, 4,5) +  6)

box <- boxplot(m,data=m, main="Execution Times",
    ylab="Execution time", srt=45, adj=1 , las = 2)


#text(box, par(“usr”)[3] , labels = leg, srt = 45, adj = c(1.1,1.1), xpd = TRUE, cex=.9)
#axis(2)

#fr < data.frame(fr,col.names(leg))

dev.off() 
