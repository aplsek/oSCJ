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

import static checker.Report.report;
import static checker.Report.reportINbuffer;

import japa.parser.ast.expr.AnnotationExpr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Element {

	public String name;
	public String declaration = "";
	public List<String> annotations;
	
	public List<String> ignore;
	
	public Element () {
		annotations = new ArrayList<String>();
		ignore = new  ArrayList<String>();
		
		ignore.add("@Pure");
		ignore.add("@Allocate");
		ignore.add("@MemoryAreaEncloses");
		ignore.add("@MemoryAreaSame");
		ignore.add("@Immutable");
	}
	
	public void setDeclaration(String n) {
		declaration = n;
	}
	
	public void setAnnotations(List<AnnotationExpr> list) {
		if (list != null) {
			Iterator<AnnotationExpr> iter = list.iterator();
			while (iter.hasNext()) {
				AnnotationExpr annot = iter.next();
				
				if (isIgnored(annot.toString()))
					continue;
				
				//System.out.println("\t"+annot.toString());
				if (annot.toString().equals("@SCJAllowed(LEVEL_0)"))
					annotations.add("@SCJAllowed");
				else if (annot.toString().equals("@SCJAllowed()"))
					annotations.add("@SCJAllowed");
				else
					annotations.add(annot.toString());
			}
		}
	}
	
	private boolean isIgnored(String str) {
		for (String s : ignore) 
			if (str.contains(s))
				return true;
		return false;
	}
	
	public void printAnnotations() {
		Iterator<String> iter = annotations.iterator();
		while (iter.hasNext()) {
			String annot = iter.next();
			//System.out.println("\t\t"+annot);
		}
	}
	
	/**
	 * 
	 * @param target
	 * @return null if equal
	 */
	public int compareAnnotations(Element target) {
		
		List<String> tmp = new ArrayList<String>(annotations);
		tmp.removeAll(target.annotations);
		
		int err = 0;
		
		for (String msg : tmp) {
			reportINbuffer("\tAnnotation missing : " + msg);
		}
		return tmp.size();
	}
	
	
	public boolean isSCJ() {
		Iterator<String> iter = annotations.iterator();
		while (iter.hasNext()) {
			String annot = iter.next();
			
			if (annot.startsWith("@SCJAllowed") || annot.startsWith("@SCJProtected") ) 
				return true;
		}
		
		return false;
	}
	
}
