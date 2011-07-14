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


import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static checker.Report.report;
import static checker.Report.reportINbuffer;
import static checker.Report.reportStart;
import static checker.Report.reportBuffer;


public class Clazz extends Element {

	String pkg;
	public List<Clazz> classes;
	List<Method> methods;
	
	public List<ClassOrInterfaceType> extnds;

	public Clazz() {
		classes = new ArrayList<Clazz>();
		methods = new ArrayList<Method>();
	}

	public void addMethod(Method m) {
		methods.add(m);
	}

	public void addClass(Clazz m) {
		classes.add(m);
	}

	public void addAnnotation(String str) {
		annotations.add(str);
	}



	public void print() {
		System.out.println("Declaration: " + declaration);
		System.out.println("package: " + pkg);

		System.out.println("Methods: ");
		printMethods();

		System.out.println("Annotations: ");
		Iterator<String> iter = annotations.iterator();
		while (iter.hasNext()) {
			String annot = iter.next();
			System.out.println("\t"+annot.toString());
		}

		printSubclasses();


	}

	public void printMethods() {
		Iterator<Method> iter = methods.iterator();
		while (iter.hasNext()) {
			Method m = iter.next();
			System.out.println("\t"+m.declaration);
			m.printAnnotations();
		}
	}

	private void printSubclasses() {
		//System.out.println("Sub-classes:");
		Iterator<Clazz> iter = classes.iterator();
		while (iter.hasNext()) {
			Clazz cl = iter.next();
			cl.print();
		}


	}

	public void setPackage(String name) {
		pkg = name;

	}

	int errors = 0;

	public int compare (Clazz target) {
		errors = 0;

		if (pkg != null)
			reportStart("" + pkg + "/" +name);
		else
			reportStart("Sub-Class : " + name);
		//because otherwise its a subclass

		// scj check
		if (!isSCJ()) {
			report(" - class ignored [SCJ annotation missing?].");
			return errors;
		}

		
		//name
		errors += compareDeclare(target);
		if (errors > 0)
			reportBuffer();
		
		
		//System.out.println("declaration" + errors);
		//System.out.println("dec  " + declaration);
		//System.out.println("target  " + target.declaration);
		
		//package name
		if ( pkg != null && target.pkg!=null && !pkg.equals(target.pkg) ) {
			report("different packages:");
			errors++;
		}



		//annotations
		int annot = super.compareAnnotations(target);
		if (annot != 0) {
			errors += annot;
			reportBuffer();
		}

		
		//methods
		errors += compareMethods(target);

		
		//classes
		errors += compareSubClasses(target);

		
		return errors;

	}

	private int compareDeclare(Clazz target) {
		int err = 0;
		String report = "";
		
		if (extnds == null) {
			return err;
		}
		
		for (ClassOrInterfaceType ex : extnds) {
			if (!target.extnds.contains(ex)) {
				err++;
				report += ex.toString() + ", ";
				//System.out.println(">>>>>> missing:" +ex);
			}
		}
		
		if (err > 0) {
			reportINbuffer("\tMissing Exception: " + report);
		}
		//else
		//	System.out.println("no error");
			
		return err;
	}


	/**
	 * 
	 * 
	 * 
	 * returns number of methods that do not match
	 * 
	 * @param target
	 * @return
	 */
	private int compareMethods(Clazz target) {
		int err = 0;

		Iterator<Method> iter = methods.iterator();
		while (iter.hasNext()) {
			Method meth = iter.next();
			if (!meth.isSCJ())
				continue;

			boolean match = false;
			boolean anno_match = false;
			boolean name_match = false;
			boolean declaration_error = false;
			int line_err = 0;

			//is there the method?
			Iterator<Method> iter2 = target.methods.iterator();
			while (iter2.hasNext()) {
				Method mt = iter2.next();
				
				
				if (meth.compareDeclaration(mt)) {
					match = true;

					int e = meth.compareAnnotations(mt);
					line_err = mt.line;
					err += e;
					if (e == 0) 
						anno_match = true;

					e = meth.compareMethod(mt);
					if (e > 0)
						declaration_error = true;
					err += e;
					
					break;
				} else 
					if (meth.name.equals(mt.name))
						name_match = true;
				/*
				if (meth.getDeclaration().equals(mt.getDeclaration())) {	//  if (meth.declaration.equals(mt.declaration)) {
					//System.out.println("method:" + mt.getDeclaration());

					match = true;
					int e = meth.compareAnnotations(mt);
					line_err = mt.line;
					err += e;
					if (e == 0) {
						anno_match = true;
						break;
					}
				}
				else if (!match && meth.compareDeclaration(mt))  {
					//System.out.println("method:" + mt.toString());


					match = true;

					int e = meth.compareAnnotations(mt);
					line_err = mt.line;
					err += e;
					if (e == 0) 
						anno_match = true;

					e = meth.compareMethod(mt);
					if (e > 0)
						declaration_error = true;
					err += e;

					break;
				}
				else
					name_match = true;
*/
			}

			if (!match) {
				err++;
				report(meth.declaration);
				if (name_match)
					report("\t Method not found. [however, a method with the same name found! Check the declarations!]");
				else
					report("\t Method not found.");
			}
			else if (!anno_match || declaration_error) {
				report(meth.declaration + ":" + line_err + ":");
				reportBuffer();
			}
		}

		return err;
	}




	private int compareSubClasses(Clazz target) {
		int err = 0;

		if (classes.isEmpty())
			return err;

		Report.increase();

		boolean match = false;
		boolean err_equal = false;

		Iterator<Clazz> iter = classes.iterator();
		while (iter.hasNext()) {
			Clazz cl = iter.next();

			//report("SubClass:" + cl.declaration);
			Report.increase();

			//is there the method?
			Iterator<Clazz> iter2 = target.classes.iterator();
			while (iter2.hasNext()) {
				Clazz cl2 = iter2.next();
				if (cl.declaration.equals(cl2.declaration)) {
					match = true;
					int tmp = cl.compare(cl2) ;
					if (tmp != 0 ) { 
						err += tmp;
						err_equal = true;
					}
				}
			}


			if (!match) {
				err++;
				report("\t Class not found.");
			}
			//else if (err_equal) 
			//	report("\t Sub-Classes are not equal.");

			report("\t Sub-Classes : " + err + " errors.");

			Report.decrease();
		}


		Report.decrease();

		return err;
	}
}
