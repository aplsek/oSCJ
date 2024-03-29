package checkers.scope;


import static checkers.scope.SchedulableChecker.ERR_CYCLIC_EXEC_INIT_RUNS_IN_MISMATCH;

import java.util.Properties;

import checkers.SinglePassChecker;
import checkers.source.SourceVisitor;

import com.sun.source.tree.CompilationUnitTree;

public class SchedulableChecker extends SinglePassChecker {
    public static final String ERR_NO_SCOPE = "no.scope";
    public static final String ERR_SCOPE_DEFINESCOPE_MISMATCH = "schedulable.scope.mismatch";
    public static final String ERR_SCHEDULABLE_NO_RUNS_IN = "schedulable.no.runsIn";
    public static final String ERR_SCHEDULABLE_RUNS_IN_MISMATCH = "schedulable.runsIn.mismatch";
    public static final String ERR_SCHED_INIT_OUT_OF_INIT_METH = "schedulable.ctor.out.of.init.method" ;
    public static final String ERR_MISSION_INIT_RUNS_IN_MISMATCH = "schedulable.err.mission.init.runsin" ;
    public static final String ERR_MISSION_SEQUENCER_RUNS_IN = "err.mission.sequencer.runsin";
    public static final String ERR_CYCLIC_EXEC_INIT_RUNS_IN_MISMATCH = "err.cyclic.exec.init.runsin.mismatch";
    public static final String ERR_SCHEDULABLE_MULTI_INIT = "err.schedulalbe.multi.init";

    public static final String ERR_SAFELET_RUNSIN = "err.safelet.runsIn";
    public static final String ERR_CYCLIC_EXEC_GET_SCHEDULE_RUNS_IN_MISMATCH = "err.cyclic.exec.get.schedule";

    private ScopeCheckerContext ctx;

    public SchedulableChecker(ScopeCheckerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected SourceVisitor<?, ?> createSourceVisitor(CompilationUnitTree root) {
        return new SchedulableVisitor(this, root, ctx);
    }

    @Override
    public Properties getMessages() {
        Properties p = new Properties();
        p.put(ERR_NO_SCOPE, "Missing @Scope: Class that implements Schedulable/MissionSequencer must have a @Scope annotation.");
        p.put(ERR_SCOPE_DEFINESCOPE_MISMATCH, "Illegal @Scope annotation: The @Scope annotation must refer to the parent-scope in the @DefineScope annotation of this Schedulable/MissionSequencer.");
        p.put(ERR_SCHEDULABLE_NO_RUNS_IN, "Missing @RunsIn: Schedulable must have a @RunsIn() annotation on the according method (e.g. handleAsyncEvent(), run()).");

        p.put(ERR_SCHEDULABLE_RUNS_IN_MISMATCH, "Bad @RunsIn: The @RunsIn annotation of the schedulable's method (@RusnIn(%s)) does not correspond to the @DefineScope annotation (@DefineScope(name=%s,...)).");
        p.put(ERR_SCHED_INIT_OUT_OF_INIT_METH, "A Schedulable object may be instantiated only in an initialization method of a Mission object.");

        p.put(ERR_MISSION_INIT_RUNS_IN_MISMATCH, "Illegal @RunsIn: Mission.initialize() method must have according @RunsIn. It is expected to have @RusnIn(%s) but the @RusnIn(%s) was detected.");

        p.put(ERR_SCHEDULABLE_MULTI_INIT, "Bad Schedulable Instantiation: A schedulable can be instantiated only once per a given mission.");


        p.put(ERR_MISSION_SEQUENCER_RUNS_IN, "Illegal @RunsIn: MissionSequencer.getNextMission() must have a @RunsIn(<named-scope>) corresponding to the @DefineScope annotation. The @RunsIn(\"%s\") is expected.");

        p.put(ERR_SAFELET_RUNSIN, "Illegal @RunsIn: Safelet methods getSequencer()/cleanUp()/tearDown() are expected to have the default annotation - @RunsIn(THIS). But, @RunsIn(\"%s\") detected.");


        p.put(ERR_CYCLIC_EXEC_INIT_RUNS_IN_MISMATCH, "Illegal @RunsIn: CyclicExecutive.initialize() must have a @RunsIn(<named-scope>) corresponding to the @DefineScope annotation. The @RunsIn(\"%s\") is expected.");
        p.put(ERR_CYCLIC_EXEC_GET_SCHEDULE_RUNS_IN_MISMATCH, "Illegal @RunsIn: CyclicExecutive.getSchedule() must have a @RunsIn(<named-scope>) corresponding to the MissionMemory of the CyclicExecutive. The @RunsIn(\"%s\") is expected.");

        return p;
    }
}
