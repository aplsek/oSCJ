package comp.cdx;

public interface ICollDetectToImmEntry {

	public void beforeRun (long time, boolean reportedMiss);
	public void afterRun (long timeBefore, long timeAfter);
}
