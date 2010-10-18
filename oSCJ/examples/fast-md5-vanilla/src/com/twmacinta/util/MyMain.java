package com.twmacinta.util;

import bench.Benchmark;
import bench.Constants;

/**
 * 
 * Created by Ales Plsek, October 2010.
 * @author plsek
 *
 */

public class MyMain {
	
	
	 public static void doMD5work() {
		 for(String in : Constants.input ) {
			MyMD5Input myMD = new MyMD5Input();
			myMD.run(in);
			//myMD.finalHash(in);
		 }
	 }
	 
	 
	 private static void main(String[] arg) {
		 Benchmark.init();
		 
		 for(int i = 0; i < Constants.RUNS;i++) {
			 long start = System.nanoTime();
			 doMD5work();
			 long  end =   System.nanoTime();
	         Benchmark.set(start, end);
		 }
		 
		 Benchmark.dumpResults();
	 }
	 
}
