#! /usr/bin/env Rscript

##############################
#  INPUT
#
args = commandArgs(TRUE);

##############################
#   Global Var
#
baseColors = c("black","red","green","blue","yellow","cyan","purple","orange","grey","pink","brown","gold","darkblue")
extraColors = colors()[c(12, 25, 47, 76, 107, 108, 376, 367, 594, 657, 650, 525, 91)]
colors = c(baseColors, extraColors) 

plot_type   = c("l","l")
line_type   = c(1,1)
plot_width  = 15
plot_height = 6


##############################
#   Utilities
#


#
# Return a [name, data] list
#
get_database = function(filenames) {
	database = list()
	for(file in filenames) {
		n = gsub("./tmp/","",file)
		n = gsub("_m.dat","",n)
		d = list(name=n, data=read.table(file))
		database = c(database, list(d))
	}
	return(database)
}


##############################
#   Plots
#

plot_mem = function(database) {
	 print("------------------------ Memory -----------------------------")

	 max   = c()
	 mean  = c()
	 leg   = c()
	 frame = list()
	 
	 for (d in database) {
	     name  = d$name
	     data  = d$data
             mem   = (data$V5) / 1000
	     max   = c(max,max(mem))
	     mean  = c(mean,mean(mem))
	     leg   = c(leg, name)
	     frame = c(frame, list(mem))
	     
	     print(leg)
 	 }
 	 frame   = data.frame(frame)

	 pdf("mem_bench.pdf", width=25, height=10)
	 range_x = c(0,300)
	 title   = "Memory Usage"
	 label_x = "Iteration"
	 label_y = "Memory Usage [KB]"
	 matplot(frame, main=title, xlim=range_x, type=plot_type, lty=line_type, xlab=label_x, ylab=label_y, col=colors, lwd=2)
	 legend("topright", inset=.05, leg, col=colors, fill=colors)
	 
	 
	print("MEMORY USAGE STATISTICS:")
	line = 2
	output=matrix(nrow = length(database)+1, ncol = 3)
	output[1,1] = "NAME"
	output[1,2] = "MAX MEM [kB]"
	output[1,3] = "MEAN MEM [kB]"
	maax   = c()
	mean  = c()
	for (d in database) {
	    name  = d$name
	     data  = d$data
         mem   = (data$V5) / 1000
	     output[line,1] = name
		 output[line,2] = max(mem)
		 output[line,3] = mean(mem)
	   
	    line = line + 1   
	}
	print(output)
	print("----------- end -----------------------------------------------------------------")

	 
}

plot_mem_box = function(database) {
	 print("------------------------ Memory Distribution (box) -----------------------------")

	 leg   = c()
	 frame = list()
	 for (d in database) {
	     name  = d$name
	     data  = d$data
             mem   = (data$V5) / 1000
	     leg   = c(leg, name)
	     frame = c(frame, list(list(mem)))
	 }
	 frame = data.frame(frame)
	 colnames(frame) = leg

	 pdf("Memory_Distr_box.pdf", width=plot_width, height=plot_height)
	 title   = "Memory Distribution"
	 label_y = "Execution time [ms]"
	 par(mar=c(11,4,4,5))
	 boxplot(frame, data=frame, main=title, ylab=label_y, srt=45, adj=1, las = 2)
}

##############################
#   main()
#

print(args)
data  = get_database(args)
plot_mem(data)
plot_mem_box(data)