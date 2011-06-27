/**
 *  This file is part of miniCDx benchmark of oSCJ.
 *
 *   miniCDx is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   miniCDx is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with miniCDx.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *   Copyright 2009, 2010 
 *   @authors  Daniel Tang, Ales Plsek
 *
 *   See: http://sss.cs.purdue.edu/projects/oscj/
 */
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import javax.safetycritical.Safelet;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.Scope;
import static javax.safetycritical.annotate.Scope.*;
import cdx.Constants;
import cdx.Level0Safelet;


public class Launcher {
    
    @SCJRestricted(INITIALIZATION)
    @RunsIn(IMMORTAL)
    public static void main(final String[] args) {
        if (args.length > 0)
            Constants.NUMBER_OF_PLANES = Integer.parseInt(args[0]);
        if (args.length > 1)
            Constants.DETECTOR_PERIOD = Integer.parseInt(args[1]);
        if (args.length > 2)
            Constants.MAX_FRAMES = Integer.parseInt(args[2]);
        
        // TODO: this Launcher is not used.
    }
}
