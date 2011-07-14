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

package filereader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	File dir;
	String path;
	
	List<String> files = new ArrayList<String>();
	
	public Reader(String p) {
		path = p; 
	}
	
	public List<String> getFiles() {
		
		dir = new File(path);
		addFiles(dir);
		
		return files;
	}
	
	
	public void addFiles(File dir) {
		String[] children = dir.list(); 
		if (children == null) 
			return;

		for (int i=0; i<children.length; i++) {
			if (children[i].contains(".java"))
				files.add(children[i]);
		}
	}
	
}
