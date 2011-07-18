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

package javax.safetycritical;

import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;
import static javax.safetycritical.annotate.Phase.CLEANUP;
import static javax.safetycritical.annotate.Level.SUPPORT;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;
import javax.safetycritical.annotate.RunsIn;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

/**
 * A safety-critical application consists of one or more missions, executed
 * concurrently or in sequence. Every safety-critical application is represented
 * by an implementation of Safelet which identifies the outer-most
 * MissionSequencer.
 * 
 * @author plsek
 * 
 */
@Scope(IMMORTAL)
@SCJAllowed
public interface Safelet<MissionLevel extends Mission>  {

    // @SCJAllowed
    // public Level getLevel();

    /**
     * Returns the MissionSequencer responsible for selecting the sequence of
     * Missions that represent this SCJ application. The infrastructure invokes
     * getSequencer to obtain the MissionSequencer that oversees execution of
     * Missions for this applica- tion. The returned MissionSequencer resides in
     * ImmortalMemoryArea. Note that MissionSequencer is an extension of
     * BoundAsynchronousEventHandler. The Stor- ageParameters resources for the
     * MissionSequencer’s bound thread are taken from the StorageParameters
     * resources for the Safelet’s initialization thread. The initial- ization
     * infrastructure arranges to start up the corresponding BoundAsynchronous-
     * EventHandler and causes its event handling code to execute in the
     * corresponding bound Thread. The event handling code, provided in the
     * MissionSequencer’s final handleAsyncEvent() method, begins executing with
     * ImmortalMemoryArea as its cur- rent allocation area.
     * 
     * @return The returned MissionSequencer resides in ImmortalMemoryArea.
     */
    @SCJAllowed(SUPPORT)
    @SCJRestricted(INITIALIZATION)
    public MissionSequencer getSequencer();

    @SCJAllowed(SUPPORT)
    public long immortalMemorySize();
}
