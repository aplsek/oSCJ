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
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 *  USA
 */

package railsegment.clock;

import static javax.safetycritical.annotate.Level.LEVEL_2;
import static javax.safetycritical.annotate.Level.SUPPORT;
import static javax.safetycritical.annotate.Phase.INITIALIZATION;

import javax.realtime.AbsoluteTime;
import javax.realtime.PeriodicParameters;
import javax.realtime.PriorityParameters;
import javax.realtime.RelativeTime;
import javax.safetycritical.PeriodicEventHandler;
import javax.safetycritical.StorageParameters;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;
import javax.safetycritical.annotate.Scope;

@SCJAllowed(value=LEVEL_2, members=true)
@Scope("TM.C")
@DefineScope(name="TM.C.B", parent="TM.C")
public class TimerTick extends PeriodicEventHandler
{
  // Determined by VM-specific static analysis tools
  private static final long BackingStoreRequirements = 500;
  private static final long StackRequirements = 5000;

  private TimeService time_mission;
  private TrainClock train_clock;

  // This periodic task runs every 250 microseconds
  @SCJRestricted(INITIALIZATION)
  public TimerTick(TimeService time_mission,
                   TrainClock train_clock, int priority) {
    super(new PriorityParameters(priority),
          new PeriodicParameters(null, new RelativeTime(0L, 250000)),
          new StorageParameters(BackingStoreRequirements, storageArgs(), 0, 0));

    this.time_mission = time_mission;
    this.train_clock = train_clock;
  }

  private static long[] storageArgs() {
    long[] storage_args = {StackRequirements};
    return storage_args;
  }

  @Override
  @RunsIn("TM.C.B")
  @SCJAllowed(SUPPORT)
  public void handleAsyncEvent() {
    AbsoluteTime t = new AbsoluteTime(0L, 0, train_clock);
    time_mission.getGlobalTime(t);
    train_clock.updateTime(t);
  }
}
