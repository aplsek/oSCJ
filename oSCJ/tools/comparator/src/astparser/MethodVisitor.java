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

package astparser;

import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import checker.Clazz;
import checker.Method;

/**
 * Simple visitor implementation for visiting MethodDeclaration nodes.
 */
public class MethodVisitor extends VoidVisitorAdapter<Object> {

	Clazz source = null;

	Clazz current = null;
	Clazz last = null;

	
	
	public MethodVisitor(Clazz src) {
		source = src;
		// current = curr;
		// /last = curr;
	}

	public void visit(MethodDeclaration n, Object arg) {
		// System.out.println("\n\nMETHOD VISIT:" + n.getName());
		// System.out.println("\thash  : " + n.toString());

		Method method = new Method();
		method.setDeclaration(getMethodDeclaration(method,n));
		method.setLine(n.getBeginLine());
		method.modifiers = n.getModifiers();
		
		
		
		if (n.getThrows() != null) {
			method.throwables = n.getThrows();
			System.out.println("throws:"+method.throwables);
		}
		method.name = n.getName();
		method.setAnnotations(n.getAnnotations());
		current.addMethod(method);
		
		

		// System.out.println();
	}

	public void visit(PackageDeclaration n, Object arg) {
		// System.out.println("package" + n.getName());
		// current.setPackage(n.getName().toString());

		super.visit(n, arg);
		// super.visit(n, arg);
		// System.out.println("class declar");
	}

	public void visit(ClassOrInterfaceDeclaration n, Object arg) {

		if (current == null) {
			// System.out.println("current is null: ");

			current = source;
			last = source;
		} else {
			last = current;
			current = new Clazz();
			last.addClass(current);
		}

		current.name = n.getName();
		current.setDeclaration(getClassDeclaration(n));
		current.setAnnotations(n.getAnnotations());


		current.extnds =  n.getExtends();
		//System.out.println("Exxx:" + n.getExtends());
		
		super.visit(n, arg);

		current = last;
		last = null;
	}

	private String getClassDeclaration(ClassOrInterfaceDeclaration n) {
		String result = n.toString();

		// method.
		/*
		 * int i = method.indexOf('{'); if (i != -1) result =
		 * method.substring(0, i); else { i = method.lastIndexOf(';'); result =
		 * method.substring(0, i); }
		 * 
		 * int line = result.lastIndexOf('\n');
		 * 
		 * System.out.println("\t\t met  : " + result);
		 * 
		 * if (line == -1) return result; else { return result.substring(line +
		 * 1, i); }
		 */

		// System.out.println (n.getName());

		// System.out.println ("type par : " + n.getTypeParameters());

		// System.out.println ("extends: " +n.getExtends());
		// System.out.println ("implements: " + n.getImplements());

		// System.out.println ("javadoc:" + n.getJavaDoc());

		int i = result.indexOf("{\n");
		if (i != -1)
			result = result.substring(0, i);
		else {
			i = result.lastIndexOf(';');
			result = result.substring(0, i);
		}

		int line = result.lastIndexOf('\n');

		if (line != -1)
			result = result.substring(line + 1, i);

		// System.out.println("\t\t met  : "
		// + result + "|");

		return result;
	}

	private String getMethodDeclaration(Method method,MethodDeclaration n) {
		String result = n.toString();
		if (n.getBody() != null) {
			String body = n.getBody().toString();
			result = n.toString().substring(0, n.toString().indexOf(body));
		}
		int line = result.lastIndexOf('\n');
		if (line != -1)
			result = result.substring(line + 1, result.length());

		//System.out.println("\n\n\n met  : " + result);

		///System.out.println("params:" + n.getParameters());
		//System.out.println("throws:" + n.getThrows());
		//System.out.println("type: " + n.getType());
		//System.out.println("type params:" + n.getTypeParameters());

		if (n.getParameters() != null && !n.getParameters().isEmpty()) {
			for (Parameter param : n.getParameters()) {
				String p = param.toString();
				//System.out.println("param:" + p);

				int i = p.indexOf(' ');
				String type = p.substring(0, i);
				//System.out.println("type only:" + type);

				result = result.replace(param.toString(), type);
				
				if (method.parameters == null)
					method.parameters =type;
				else
					method.parameters = method.parameters + ", " + type;
				//System.out.println("modified....:" + result);
			}
		}
		//else
			//System.out.println("NOT modified>>>:" + result);
		
		//System.out.println("TOTAL:" + result);
		//System.out.println("params:" + method.parameters);
		return result;

	}

}
