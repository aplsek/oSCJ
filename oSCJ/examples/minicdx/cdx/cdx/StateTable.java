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
package cdx;

import java.util.HashMap;
import javax.realtime.MemoryArea;
import javax.safetycritical.ManagedMemory;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;
import statetable.Vector3d;
import static javax.safetycritical.annotate.Scope.THIS;
import static javax.safetycritical.annotate.Level.SUPPORT;

/**
 * The instance lives and the constructor runs in the persistent detector scope. The put method and the get method are
 * called from the transient detector scope - see below. previous_state is map call signs to 3D vectors - the call signs
 * are in persistent detector scope - the vectors are in persistent detector scope (allocated here)
 */
@SCJAllowed(members=true)
@Scope("cdx.Level0Safelet")
public class StateTable {

    final private static int MAX_AIRPLANES = 10000;

    private Vector3d[]       allocatedVectors;
    private int              usedVectors;

    /** Mapping Aircraft -> Vector3d. */
    final private HashMap    motionVectors = new HashMap();

    StateTable() {
        allocatedVectors = new Vector3d[MAX_AIRPLANES];
        for (int i = 0; i < allocatedVectors.length; i++)
            allocatedVectors[i] = new Vector3d();

        usedVectors = 0;
    }

    @SCJAllowed(members=true)
    @Scope("cdx.Level0Safelet")
    private class R implements Runnable {
        CallSign callsign;
        float    x, y, z;

        @RunsIn("cdx.Level0Safelet")
        public void run() {
            Vector3d v = (Vector3d) motionVectors.get(callsign);
            if (v == null) {
                v = allocatedVectors[usedVectors++]; // FIXME: What if we exceed MAX?
                motionVectors.put(callsign, v);
            }
            v.x = x;
            v.y = y;
            v.z = z;
        }
    }
    private final R r = new R();

    @RunsIn("cdx.CollisionDetectorHandler")
    public void put(@Scope("cdx.Level0Safelet") final CallSign callsign, final float x, final float y, final float z) {
        r.callsign = callsign;
        r.x = x;
        r.y = y;
        r.z = z;
        ((ManagedMemory) MemoryArea.getMemoryArea(this)).executeInArea(r);
    }

    @RunsIn("cdx.CollisionDetectorHandler") @Scope(THIS)
    public Vector3d get(final CallSign callsign) {
        return (Vector3d) motionVectors.get(callsign);
    }
}
