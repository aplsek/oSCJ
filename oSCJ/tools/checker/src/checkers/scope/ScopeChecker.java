package checkers.scope;

import java.util.Properties;

import javax.annotation.processing.SupportedOptions;

import checkers.SinglePassChecker;
import checkers.source.SourceVisitor;

import com.sun.source.tree.CompilationUnitTree;


@SupportedOptions("debug")
public class ScopeChecker extends SinglePassChecker {
    // Please keep alphabetized
    public static final String ERR_BAD_ALLOCATION = "bad.allocation";
    public static final String ERR_BAD_ASSIGNMENT_SCOPE = "bad.assignment.scope";
    public static final String ERR_BAD_ASSIGNMENT_PRIVATE_MEM = "bad.assignment.private.mem";
    public static final String ERR_BAD_ENTER_PARAM = "bad.enter.param";
    public static final String ERR_BAD_ENTER_PRIVATE_MEM_RUNS_IN_NO_MATCH = "bad.enterPrivateMem.runsIn.no.match";
    public static final String ERR_BAD_ENTER_TARGET = "bad.enter.target";
    public static final String ERR_BAD_EXECUTE_IN_AREA_OR_ENTER = "bad.executeInArea.or.enter";
    public static final String ERR_BAD_EXECUTE_IN_AREA_TARGET = "bad.executeInArea.target";
    public static final String ERR_BAD_GUARD_ARGUMENT = "bad.guard.argument";
    public static final String ERR_BAD_GUARD_NO_FINAL = "bad.guard.no.final";
    public static final String ERR_BAD_METHOD_INVOKE = "bad.method.invoke";
    public static final String ERR_BAD_NEW_INSTANCE = "bad.newInstance";
    public static final String ERR_BAD_RETURN_SCOPE = "bad.return.scope";
    public static final String ERR_BAD_RUNS_IN_METHOD = "bad.runs.in.method";
    public static final String ERR_BAD_VARIABLE_SCOPE = "bad.variable.scope";
    public static final String ERR_DEFAULT_BAD_ENTER_PARAMETER = "default.bad.enter.parameter";
    public static final String ERR_ESCAPING_NONANNOTATED_FIELD = "escaping.nonannotated.field";
    public static final String ERR_INTERFACE_ANNOTATION_MISMATCH = "interface.annotation.mismatch";
    // TODO: Remove
    public static final String ERR_RUNNABLE_WITHOUT_RUNSIN = "runnable.without.runsin";
    public static final String ERR_SCOPE_RUNS_IN_DISAGREEMENT = "scope.runs.in.disagreement";
    public static final String ERR_STATIC_NOT_IMMORTAL = "static.not.immortal";
    public static final String ERR_TYPE_CAST_BAD_ENTER_PARAMETER = "type.cast.bad.enter.parameter";

    private ScopeCheckerContext ctx;

    public ScopeChecker(ScopeCheckerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected SourceVisitor<?, ?> createSourceVisitor(CompilationUnitTree root) {
        return new ScopeVisitor<Void>(this, root, ctx);
    }

    @Override
    public Properties getMessages() {
        Properties p = new Properties();
        // Please keep alphabetized
        p.put(ERR_BAD_ALLOCATION,
                "Object allocation in a context (%s) other than its designated scope (%s).");
        p.put(ERR_BAD_ASSIGNMENT_PRIVATE_MEM,
                "Cannot assign to a private memory with a different @DefineScope.");
        p.put(ERR_BAD_ASSIGNMENT_SCOPE,
                "Cannot assign expression in scope %s to variable in scope %s.");
        p.put(ERR_BAD_ENTER_TARGET, "enter() must target a child scope.");
        p.put(ERR_BAD_ENTER_PRIVATE_MEM_RUNS_IN_NO_MATCH,
                "The Runnable's @RunsIn must be a child scope of the CurrentScope\n\t @RunsIn: %s\n\t Current Scope: %s");
        p.put(ERR_BAD_EXECUTE_IN_AREA_OR_ENTER,
                "Runnable and PrivateMemory scopes disagree.");
        p.put(ERR_BAD_EXECUTE_IN_AREA_TARGET,
                "executeInArea() must target a parent scope.");
        p.put(ERR_BAD_GUARD_ARGUMENT,
                "Only single variables can be passed as arguments into guards, no other expressions are allowed, eg. %s.");
        p.put(ERR_BAD_GUARD_NO_FINAL,
                "The variables passed into the GUARD statement must be final. The argument %s is not.");
        p.put(ERR_BAD_METHOD_INVOKE,
                "Illegal invocation of method of object in scope %s while in scope %s.");
        p.put(ERR_BAD_NEW_INSTANCE,
                "Cannot allocate objects of type %s inside scope %s.");
        p.put(ERR_BAD_RETURN_SCOPE,
                "Cannot return expression in scope %s in a method that has @Scope annotation: %s.");
        p.put(ERR_BAD_RUNS_IN_METHOD,
                "Methods must run in the same scope or a child scope of their owning type.");
        p.put(ERR_BAD_VARIABLE_SCOPE,
                "Variables of type %s are not allowed in this allocation context (%s).");
        p.put(ERR_ESCAPING_NONANNOTATED_FIELD, ERR_ESCAPING_NONANNOTATED_FIELD);
        p.put(ERR_INTERFACE_ANNOTATION_MISMATCH,
                "One or more interfaces has a mismatching @%s annotation.");
        p.put(ERR_RUNNABLE_WITHOUT_RUNSIN,
                "Runnable used with executeInArea() without @RunsIn.");
        p.put(ERR_SCOPE_RUNS_IN_DISAGREEMENT,
                "@RunsIn annotations must be a sub-scope of @Scope annotations.");
        p.put(ERR_STATIC_NOT_IMMORTAL,
                "Static fields types must be @Scope(IMMORTAL) or nothing at all.");

        return p;
    }
}
