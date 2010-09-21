
public class Main {

	
	public static void main(final String[] w) {
		MicroBenchVanilla vanilla = new MicroBenchVanilla();
		
		for (int i=0 ; i < Constants.RUNS;i ++) {
			vanilla.handleEvent();
		}
	}
	
}
