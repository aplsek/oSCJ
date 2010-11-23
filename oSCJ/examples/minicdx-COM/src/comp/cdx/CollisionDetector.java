
package comp.cdx;

import org.objectweb.fractal.fraclet.annotations.Component;
import org.objectweb.fractal.fraclet.annotations.Interface;
import org.objectweb.fractal.fraclet.annotations.Requires;

import cdx.ITransientDetector;
import cdx.NanoClock;

@Component(name = "CollisionDetector", provides = @Interface(name = "r", signature = java.lang.Runnable.class))
public class CollisionDetector implements Runnable {

	public boolean stop = false;

	@Requires(name="iTransientDetector")
	private ITransientDetector iTransientDetector;
	
	@Requires(name="iCdToIe")
	private ICollDetectToImmEntry iCdToIe;

	public void run() {

		long now = System.nanoTime();
// The direct accesses to ImmortalEntry fields are replaced by a method call via
// an interface
//		ImmortalEntry.detectorReleaseTimes[ImmortalEntry.recordedDetectorReleaseTimes] = now;
//		ImmortalEntry.detectorReportedMiss[ImmortalEntry.recordedDetectorReleaseTimes] = false;
//		ImmortalEntry.recordedDetectorReleaseTimes++;
		iCdToIe.beforeRun(now, false);

		long timeBefore = NanoClock.now();
		iTransientDetector.runDetectorInScope();
		long timeAfter = NanoClock.now();

// The direct accesses to ImmortalEntry fields are replaced by a method call via
// an interface
//		if (ImmortalEntry.recordedRuns < ImmortalEntry.maxDetectorRuns) {
//			ImmortalEntry.timesBefore[ ImmortalEntry.recordedRuns ] = timeBefore;
//			ImmortalEntry.timesAfter[ ImmortalEntry.recordedRuns ] = timeAfter;
//			//ImmortalEntry.heapFreeBefore[ ImmortalEntry.recordedRuns ] = heapFreeBefore;
//			//ImmortalEntry.heapFreeAfter[ ImmortalEntry.recordedRuns ] = heapFreeAfter;
//			ImmortalEntry.recordedRuns++;
//		}
		iCdToIe.afterRun(timeBefore, timeAfter);

	}
	
}
