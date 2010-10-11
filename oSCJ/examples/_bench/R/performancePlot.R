#! /usr/bin/env Rscript

##############################
#  INPUT
#
args = commandArgs(TRUE);


##############################
#   Global Var
#
baseColors = c("black","red","green","blue","cyan","purple","yellow","grey", "orange", "pink", "brown", "darkblue","gold")
extraColors = colors()[c(12, 25, 47, 76, 107, 108, 376, 367, 594, 657, 650, 525, 91)]
colors = c(baseColors, extraColors) 

plot_type   = c("l","l")
line_type   = c(1,1)
plot_width  = 15
plot_height = 6


##############################
#   Utilities
#


# parse a file name, get heap size
parse_filename = function(filename) {
	#print(tmp)
	sp  = strsplit(filename,"_",fixed = TRUE)
	sp1 = unlist(sp)
	ret = sp1[3]
	ret = unlist(strsplit(ret,".",fixed = TRUE))
	return(ret)
}


sort_matrix = function(M) {
    len = length(M)
    if(len == 0)
        return(M)
    n_rows = nrow(M)
    n_cols = ncol(M)
    o      = order(M[,1])
    M1     = M[o,]

    c = 2
    while(c <= n_cols) {
        pre_c   = c - 1;
        master  = 0
        slave   = 0
        n_slave = 0
        r       = 1
        base_r  = 0
        o       = c()
        while (r <= n_rows) {
            master_item = M1[r,pre_c]
            if(master_item != master) {
                if(n_slave != 0) {
                    o = c(o, base_r + order(M1[(base_r+1):(r-1),c]))
                }
                master  = master_item
                n_slave = 1
                base_r  = r - 1
            } else {
                n_slave = n_slave + 1
            }
            r = r + 1
        }
        o  = c(o, base_r + order(M1[(base_r+1):(r-1),c]))
        M1 = M1[o,]
        c  = c + 1;
    }
    return(M1)
}

# Return for certain heap size, how many trigger level there is.
# E.g. if the matrix is:
#
# HEAP TRIGGER  ...
# 1000 100
# 1000 200
# 1000 300
# 2000 110
# ...
#
# return 3
get_nTriggers = function(M) {
    n = 0
    heap = M[1,1]
    for(h in M[,1]) {
        if(h != heap)
            return(n)
        n = n + 1
    }
    return(n)
}

#
# Return a [name, data] list
#
get_database = function(filenames) {
	database = list()
	for(file in filenames) {
		n = gsub("./tmp/","",file)
		n = gsub("_d.dat","",n)
		d = list(name=n, data=read.table(file))
		database = c(database, list(d))
	}
	return(database)
}

# stats is a matrix looking like:
# HEAP TRIGGER WCET
# 1000 100     x
# 1000 200     x
# 1000 300     x
# 2000 110     x
# ... ...
get_stats = function(database) {
	stats = matrix(nrow = length(database), ncol = 3)
	
	line = 1
	for (d in database) {
		name  = d$name
		data  = d$data
    	time  = (data$V2 - data$V1)/1000000.0
	    avg   = mean(time)
    	max   = max(time)

    	name_heap_trig = parse_filename(name)	
	    stats[line,1]  = as.integer(name_heap_trig[2]) # heap
    	stats[line,2]  = as.integer(name_heap_trig[3]) # trig
	    stats[line,3]  = max

	    line = line + 1                  
	}

	stats = sort_matrix(stats)
	return(stats)
}

##############################
#   Plots
#


plot_perf = function(database) {
	print("------------------------ Performance -----------------------------")

	max   = c()
	mean  = c()
	leg   = c()
	frame = list()
	
	for (d in database) {
		name  = d$name
		data  = d$data
	    time  = (data$V2 - data$V1)/1000000.0
		max   = c(max,max(time))
		mean  = c(mean,mean(time))
		leg   = c(leg, name)
		frame = c(frame, list(time))
	}
	frame   = data.frame(frame)

	pdf("perf_bench.pdf", width=25, height=10)
	range_x = c(0,300)
	title   = "Time"
	label_x = "Iteration"
	label_y = "Worse Case Time [ms]"
	matplot(frame, main=title, xlim=range_x, type=plot_type, lty=line_type, xlab=label_x, ylab=label_y, col=colors)
	legend("topright", inset=.05, leg, col=colors, fill=colors)
	
#	print("----------- PERFORMANCE ---------")
#	print(max)
#	print(mean)
#	print("----------- end ---------")
}


plot_time_heap = function(stats) {
	
	print("---------------------- WCET vs. Heap ---------------------------")

	TRIGGERS = get_nTriggers(stats)
	HEAPS    = nrow(stats) / TRIGGERS
	
	leg     = c()
   	frame_x = list()
	frame_y = list()  
	for (t in 1:TRIGGERS) {
		x_vec   = c()
		y_vec   = c()
		for (h in 1:HEAPS) {
     		x_vec = c(x_vec,c(stats[(h -1) * TRIGGERS + t,1]))
	   	   	y_vec = c(y_vec,c(stats[(h -1) * TRIGGERS + t,3]))
	   	}
	   	frame_x = c(frame_x, list(x_vec))
	   	frame_y = c(frame_y, list(y_vec))
	   	leg     = c(leg, as.character(stats[t,2] * 100 / stats[t,1]))
	}

   	frame_x = data.frame(frame_x)
   	frame_y = data.frame(frame_y)

	pdf("WCET_vs_heap.pdf", width=plot_width, height=plot_height)
	title   = "WCET vs. Heap"
	label_x = "Heap [KB]"
	label_y = "Worse Case Time [ms]"
	matplot(frame_x, frame_y, main=title, type=plot_type, lty=line_type, ylab=label_y, xlab=label_x, col=colors)
	legend("topright", inset=.05, title="Trigger [%Heap]", leg, col=colors, fill=colors)
}

	

plot_time_heap_box = function(database) {

	print("---------------------- Time vs. Heap box ---------------------------")

	leg   = c()
	frame = list()
	for (d in database) {
		name  = d$name
		data  = d$data
    	time  = (data$V2 - data$V1)/1000000.0
    	leg   = c(leg, name)
		frame = c(frame, list(list(time)))
	}
	frame = data.frame(frame)
	colnames(frame) = leg

	pdf("Time_vs_heap_box.pdf", width=plot_width, height=plot_height)
	title   = "Time vs. Heap"
	label_y = "Execution time [ms]"
	par(mar=c(11,4,4,5))
	boxplot(frame, data=frame, main=title, ylab=label_y, srt=45, adj=1, las = 2)
}

plot_time_trigger = function(stats) {

	print("---------------------- WCET vs. Trigger ---------------------------")

	TRIGGERS = get_nTriggers(stats)
	HEAPS    = nrow(stats) / TRIGGERS
	
	leg     = c()
   	frame_x = list()
	frame_y = list()  
	for (h in 1:HEAPS) {
		i       = (h - 1) * TRIGGERS + 1
		x_vec   = c(stats[i:(i+TRIGGERS-1),2] * 100 / stats[i,1])
		y_vec   = c(stats[i:(i+TRIGGERS-1),3])
	   	frame_x = c(frame_x, list(x_vec))
	   	frame_y = c(frame_y, list(y_vec))
	   	leg     = c(leg, as.character(stats[i,1]))
	}
	
#  	print(frame_x)
#  	print(frame_y)
   	frame_x = data.frame(frame_x)
   	frame_y = data.frame(frame_y)

	pdf("WCET_vs_trigger.pdf", width=plot_width, height=plot_height)
	title   = "WCET vs. Trigger"
	label_x = "Trigger [%Heap]"
	label_y = "Worse Case Time [ms]"
	range_x = c(0, 100)
	matplot(frame_x, frame_y, main=title, type=plot_type, lty=line_type, xlim=range_x, xlab=label_x, ylab=label_y, col=colors)
	legend("topright", inset=.05, title="Heap Size [KB]", leg, col=colors, fill=colors)
}


##############################
#   main()
#

print(args)
data  = get_database(args)
stats = get_stats(data)
plot_perf(data)
plot_time_heap(stats)
plot_time_heap_box(data)
plot_time_trigger(stats)
