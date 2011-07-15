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

package checker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;


public class Report {

	static int indent = 0;

	static FileOutputStream out; // declare a file output object
	static PrintStream p; // declare a print stream object

	public static String log = "log.txt";
	
	private static String[] buffer = new String[100];

	public static void createLog()  {
		try {
			out = new FileOutputStream(log);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Connect print stream to the output stream
		p = new PrintStream( out );

		//p.println("Teste input ds.fasdflkasdfj a;lksdj ;lakj ;lakj ");
	}

	public static void createLog(String file)  {
		try {
			out = new FileOutputStream(file);
			log = file;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Connect print stream to the output stream
		p = new PrintStream( out );

		//p.println("Teste input ds.fasdflkasdfj a;lksdj ;lakj ;lakj ");
	}

	public static void report (String report) {
		for (int i=0;i < indent;i++)
			p.print('\t');
		p.println("\t"+report);
	}

	private static int buff = 0;
	
	public static void reportINbuffer(String report) {
		buffer[buff] = report;
		buff++;
	}
	
	public static void reportBuffer() {
		for (int i=0;i < buff; i++) {
			report(buffer[i]);
			buffer[i] = null;
		}
		buff = 0;
	}
	
	public static void reportStart (String report) {
		for (int i=0;i < indent;i++)
			p.print('\t');
		p.println(report);
	}

	public static void increase() {
		indent++;
	}

	public static void decrease() {
		indent--;
	}

	public static void close() {
		p.close();
	}

}
