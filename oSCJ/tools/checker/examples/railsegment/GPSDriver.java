/**
 *  Name: Railsegment
 *  Author : Kelvin Nilsen, <kelvin.nilsen@atego.com>
 *
 *  Copyright (C) 2011  Kelvin Nilsen
 *
 *  Railsegment is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  Railsegment is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with Railsegment; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package railsegment;

import static javax.safetycritical.annotate.Phase.INITIALIZATION;

import javax.realtime.AbsoluteTime;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

import railsegment.clock.TrainClock;

@Scope("D")
@DefineScope(name="D:GPSD", parent="D")
public class GPSDriver extends PeriodicEventHandler
{
    // Determined by VM-specific static analysis tools
    private static final long BackingStoreRequirements = 500;
    private static final long NativeStackRequirements = 2000;
    private static final long JavaStackRequirements = 300;

    private NavigationService nav_mission;
    private TrainClock train_clock;

    // This periodic task runs every 1 ms
    @SCJRestricted(INITIALIZATION)
    public GPSDriver(NavigationService nav_mission,
            TrainClock train_clock, int priority) {
        super(new PriorityParameters(priority),
                new PeriodicParameters(null, new RelativeTime(1L, 0)),
                new StorageParameters(BackingStoreRequirements,
                        NativeStackRequirements,
                        JavaStackRequirements));
        this.nav_mission = nav_mission;
        this.train_clock = train_clock;
    }

    @Override
    @RunsIn("D:GPSD")
    public void handleAsyncEvent() {

        // Let's just assume that this is not interrupt driven.

        // every time i'm called, i'll update the global lat and long, and
        // post a timestamp according to the global distributed clock

        // assume these are fetched from the GPS hardware
        int longitude = 0;
        int lattitude = 0;

        AbsoluteTime now = train_clock.getPrivateTime();
        nav_mission.updatePosition(now, longitude, lattitude);
    }

    @Override
    public StorageParameters getThreadConfigurationParameters() {
        // TODO Auto-generated method stub
        return null;
    }
}