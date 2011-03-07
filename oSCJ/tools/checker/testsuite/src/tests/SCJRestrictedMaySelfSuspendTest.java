package tests;

import java.io.File;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

public class SCJRestrictedMaySelfSuspendTest extends ParameterizedCheckerTest {

    public SCJRestrictedMaySelfSuspendTest(File testFile) {
        super(testFile, "checkers.SCJChecker", "framework", "-Anomsgtext");
    }

    @Parameters
    public static Collection<Object[]> data() {
        Collection<Object[]> tests = testFiles("scjRestricted/maySelfSuspend");
        return tests;
    }
}
