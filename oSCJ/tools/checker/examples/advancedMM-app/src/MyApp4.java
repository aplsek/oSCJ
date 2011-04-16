

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
 *   @authors  Lei Zhao, Ales Plsek
 */

import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.CyclicExecutive;
import javax.safetycritical.CyclicSchedule;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

@SCJAllowed(members=true)
@Scope("APP")
@DefineScope(name="APP", parent=IMMORTAL)
public class MyApp4 extends CyclicExecutive {

 	@Override
 	@SCJAllowed(SUPPORT)
    public CyclicSchedule getSchedule(PeriodicEventHandler[] handlers) {
		return new CyclicSchedule(new CyclicSchedule.Frame[]{new CyclicSchedule.Frame(
		        new RelativeTime(5,0),handlers)});
	}

    @SCJRestricted(INITIALIZATION)
	public MyApp4() {
		super(new PriorityParameters(18),
		        new StorageParameters(1000L, 1000L, 1000L));
	}

	@Override
	@SCJRestricted(INITIALIZATION)
	@SCJAllowed(SUPPORT)
    public void initialize() {
		new MyPEH4();
	}

	/**
	 * A method to query the maximum amount of memory needed by this
	 * mission.
	 *
	 * @return the amount of memory needed
	 */
	@Override
    public long missionMemorySize() {
		return 1420;   // MIN without printing is 430  bytes.
	}

	@SCJRestricted(INITIALIZATION)
	@SCJAllowed(SUPPORT)
	public void setUp() {
	}

	@SCJRestricted(CLEANUP)
	@SCJAllowed(SUPPORT)
	public void tearDown() {
	}

	@Override
    @SCJRestricted(CLEANUP)
    @SCJAllowed(SUPPORT)
	public void cleanUp() {
	}

}
