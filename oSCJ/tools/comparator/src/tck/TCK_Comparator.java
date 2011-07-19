/**
 *  This file is part of oSCJ.
 *
 *   oSCJ is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   oSCJ is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with oSCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Ales Plsek
 */

package tck;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import astparser.MethodVisitor;
import checker.Clazz;
import checker.Report;
import filereader.Constants;
import filereader.Reader;


import static checker.Report.report;


/**
*	TODO: visit all subdirectories, not only realtiem and safetycritical
*	TODO: ignore certain annotations : @Pure, @Allocate, etc.
*
**/
public class TCK_Comparator {

	

	//total number of errors
	static int total = 0;
	
	public TCK_Comparator () {
		Report.createLog();
	}
	
    public static void main(String[] args) throws ParseException, IOException  {
     
    	
    	if (args.length < 1) {
    		System.err.println("Error: not enough arguments.");
    		System.err.println("\t Please, provide:");
    		System.err.println("\t 1st arg: Specification Directory [Source] ");
    		System.err.println("\t 2nd arg: Implementation Directory [Target] ");
    		System.err.println("\t ");
    		System.err.println("\t 3nd arg[optional]: destination for the log file ");
    		return;
    	}
    		
    	if (args.length > 1)
    		Report.createLog(args[2]);
    	else
    		Report.createLog();
    	
    	//String sourcePath = "/Users/plsek/workspace/jsr302-april/scj/specsrc";
    	//String targetPath = "/Users/plsek/_work/workspaceSCJ/scj-L0/ri/spec/common/src/";
    	
    	//String sourcePath = "/Users/plsek/_work/workspaceSCJ/TCK-Comparator/input/spec";
    	//String targetPath = "/Users/plsek/_work/workspaceSCJ/TCK-Comparator/input/oSCJ";
    	
    	String sourcePath = args[0];
    	String targetPath = args[1];
    	
    	
    	check(sourcePath,targetPath);
    	
    	//check2files(sourcePath,targetPath,"/javax/safetycritical/","ManagedSchedulable.java");    	
    	
    	
    	
    	if (total > 0) {
    		System.out.println("Check SUCCESFULL: \n\t\t"  + total  +" errors found.");
    		System.out.println("\t\tSee \"" + Report.log +  "\" file for more details.");
    		
    		report("Check SUCCESFULL: \n\t\t"  + total  +" errors found.");
    		report("\t\tSee \"" + Report.log +  "\" file for more details.");
    		
    	}
    	else {
    		System.out.println("Check SUCCESSFULL.\n\t\t 0 errors found.");
    		report("Check SUCCESSFULL.\n\t\t 0 errors found.");
    	}
    	
    	
    	Report.close();	
    	
    	
    	
    }
    
    public static void check(String sourcePath, String targetPath) {
    	for (int i=0; i < Constants.packages.length; i++) {
			
    		Report.reportStart(Constants.packages[i] + "\n");
    		Report.increase();
    		
    		checkPackage(sourcePath  + "/" + Constants.packages[i],
    				targetPath  + "/" + Constants.packages[i],Constants.packages[i]);
    		
    		Report.decrease();
    		
    		
			
		}
    }
  
    
    
    
    public static void checkPackage(String sourcePath, String targetPath, String pkg)  {
    	
    	Reader r1 = new Reader(sourcePath);
    	List<String> sources = r1.getFiles();
    	
    	
    	for (String file : sources) {
    		Clazz source = parseFile(sourcePath,file);
    		source.setPackage(pkg);
    		Clazz target = parseFile(targetPath,file);
    		//target.setPackage(pkg);
    		
    		if (source != null && target != null) {
    			int err = source.compare(target);
    			total += err;
    			//Report.report("Errors:" + err);
    			Report.report("\n");
    		}
    		if (target == null) {
    			Report.reportStart("Target Class missing: " + pkg +"/" +  file);
    			Report.report("\n");
    		}
    	}
    }
    
    private static Clazz parseFile(String path, String file) {
    	try {
			
    		System.out.println("parse:" + path + "/" + file);
    		
    		FileInputStream in = new FileInputStream(path + "/" + file);
			CompilationUnit cu = JavaParser.parse(in);
			in.close();

			Clazz source = new Clazz();
			cu.accept(new MethodVisitor(source), null);
			
			return source;
    	
    	} catch (FileNotFoundException e) {
			System.err.println("File not found:" + path + file);
		} catch (ParseException e) {
			System.err.println("File could not be parsed:" + path + file);
		} catch (IOException e) {
			System.err.println("IO error:" + path + file);
		}
		return null;
		
    }
    
   
    
    public static void check2files(String sourcePath, String targetPath,
		 String pkg, String file)  {
    	
    		Clazz source = parseFile(sourcePath+pkg,file);
    		source.setPackage(pkg);
    		Clazz target = parseFile(targetPath+pkg,file);
    		//target.setPackage(pkg);
    		
    		if (source != null && target != null) {
    			int err = source.compare(target);
    			total += err;
    			Report.report("Errors:" + err);
    			Report.report("\n");
    		}
    		if (target == null) {
    			Report.reportStart("Target Class missing: " + pkg +"/" +  file);
    			Report.report("\n");
    		}
    }
    
    

    
    
    
    
    
    
    
}
