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

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.expr.NameExpr;
import static checker.Report.reportINbuffer;

import java.util.Iterator;
import java.util.List;

public class Method extends Element {
	
	int line;
	MethodDeclaration method;
	public String name = "";
	
	public List<NameExpr> throwables;
	
	
	public String parameters = "" ;
	public int modifiers = 0;
	
	public Method() {
	}
	
	public Method(String dec) {
		declaration = dec;
	}
	
	public void setLine(int l) {
		line = l;
	}

	public String getName() {
		return name;
	}
	
	public String getDeclaration() {
		return declaration;
	}
	
	
	
	public boolean compareDeclaration (Method mt) {
		if (name.equals(mt.name))  
			if (parameters.equals(mt.parameters)) 
				return true;
		return false;	
	}

	public int compareMethod(Method mt) {
		int e = 0;
		
		e += compareThrows(mt.throwables);
		
		if (modifiers != mt.modifiers) {
			e++;
			reportINbuffer("\tModifiers do not match!");
		}
		
	
		
		return e;
	}
	
	
	private int compareThrows(List<NameExpr> throwables2) {
		int err = 0;
		String report= "";
		if (this.throwables == null)
			return err;
			
			
		
		for (NameExpr thr : this.throwables) {
			if (!throwables2.contains(thr)) {
				err++;
				report += thr.toString() + ", ";
				//System.out.println(">>>>>> missing:" +thr);
				//System.out.println("impl:" + throwables2);
			}
		}
		
		if (err > 0) {
			reportINbuffer("\tMissing Exception: " + report);
		}
		else
			System.out.println("no error");
		
		
		return err;
	}
	
}
