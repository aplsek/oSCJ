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
package comp.cdx;

import java.util.HashMap;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;

import cdx.CallSign;
import cdx.IStateTable;

import statetable.Vector3d;

/**
 * The instance lives and the constructor runs in the persistent detector scope. The put method and the get method are
 * called from the transient detector scope - see below. previous_state is map call signs to 3D vectors - the call signs
 * are in persistent detector scope - the vectors are in persistent detector scope (allocated here)
 */
@Component(name = "StateTable", provides = @Interface(name = "iStateTable", signature = cdx.IStateTable.class))
public class StateTable implements IStateTable {

    final private static int MAX_AIRPLANES = 10000;

    private Vector3d[]       allocatedVectors;
    private int              usedVectors;

    /** Mapping Aircraft -> Vector3d. */
    final private HashMap    motionVectors = new HashMap();

    public StateTable() {
        allocatedVectors = new Vector3d[MAX_AIRPLANES];
        for (int i = 0; i < allocatedVectors.length; i++)
            allocatedVectors[i] = new Vector3d();

        usedVectors = 0;
    }


    public void put(final CallSign callsign, final float x, final float y, final float z) {
        /*
    	System.out.println("[StateTable-put]");
    	MemoryArea mem = MemoryArea.getMemoryArea(callsign);
    	System.out.println("[StateTable-put] , mem area cs:" + mem.toString());
    	System.out.println("[StateTable-put] ,current area:" + RealtimeThread.getCurrentMemoryArea());
    	System.out.println("[StateTable-put] , mem area this:" + MemoryArea.getMemoryArea(this));
        */
        
        Vector3d v = (Vector3d) motionVectors.get(callsign);
        if (v == null) {
            v = allocatedVectors[usedVectors++]; // FIXME: What if we exceed MAX?
            motionVectors.put(callsign, v);
        }
        v.x = x;
        v.y = y;
        v.z = z;
    }

    public Vector3d get(final CallSign callsign) {
        return (Vector3d) motionVectors.get(callsign);
    }
}
