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
    public static final String ERR_BAD_ALLOCATION_ARRAY = "bad.allocation.array";
    public static final String ERR_BAD_ALLOCATION_CONTEXT_ASSIGNMENT = "bad.allocation.context.assignment";
    public static final String ERR_BAD_ASSIGNMENT_SCOPE = "bad.assignment.scope";
    public static final String ERR_BAD_ASSIGNMENT_PRIVATE_MEM = "bad.assignment.private.mem";
    public static final String ERR_BAD_ENTER_PRIVATE_MEMORY_RUNS_IN_NO_MATCH = "bad.enter.private.memory.runs.in.no.match";
    public static final String ERR_BAD_ENTER_PRIVATE_MEMORY_TARGET = "bad.enter.private.memory.bad.target";
    public static final String ERR_BAD_EXECUTE_IN_AREA_RUNS_IN = "bad.execute.in.area.runs.in";
    public static final String ERR_BAD_EXECUTE_IN_AREA_TARGET = "bad.execute.in.area.target";
    public static final String ERR_BAD_GET_CURRENT_MANAGED_MEMORY = "bad.get.current.managed.memory";
    public static final String ERR_BAD_GET_MEMORY_AREA = "bad.get.memory.area";
    public static final String ERR_BAD_GUARD_ARGUMENT = "bad.guard.argument";
    public static final String ERR_BAD_METHOD_INVOKE = "bad.method.invoke";
    public static final String ERR_BAD_NEW_ARRAY = "bad.new.array";
    public static final String ERR_BAD_NEW_ARRAY_TYPE = "bad.new.array.type";
    public static final String ERR_BAD_NEW_INSTANCE = "bad.new.instance";
    public static final String ERR_BAD_NEW_INSTANCE_TYPE = "bad.new.instance.type";
    public static final String ERR_BAD_NEW_INSTANCE_REPRESENTED_SCOPE = "bad.new.instance.represented.scope";

    public static final String ERR_BAD_RETURN_SCOPE = "bad.return.scope";
    public static final String ERR_BAD_RUNS_IN_METHOD = "bad.runs.in.method";
    public static final String ERR_BAD_VARIABLE_SCOPE = "bad.variable.scope";
    public static final String ERR_BAD_VARIABLE_SCOPE_OVER = "bad.variable.scope.over";
    public static final String ERR_BAD_CONTEXT_CHANGE_CALLER = "bad.context.change.in.caller";
    public static final String ERR_ESCAPING_NONANNOTATED_FIELD = "escaping.nonannotated.field";
    public static final String ERR_MEMORY_AREA_NO_DEFINE_SCOPE_ON_VAR = "err.mem.area.no.def.scope.on.var";
    public static final String ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT = "err.mem.type.def.scope.not.consistent";
    public static final String ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT_WITH_SCOPE = "err.mem.type.def.scope.not.consistent.with.scope";

    // Uncomment this to enforce @SCope on the runnable classes.
    //public static final String ERR_SCJ_RUNNABLE_BAD_SCOPE = "bad.scjrunnable.bad.scope";
    public static final String ERR_SCOPE_RUNS_IN_DISAGREEMENT = "scope.runs.in.disagreement";

    public static final String ERR_BAD_UPCAST = "err.bad.upcast";

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
        p.put(ERR_BAD_ALLOCATION_ARRAY,
                "Invalid array allocation (must be allocated in scope %s or a child).");
        p.put(ERR_BAD_ALLOCATION_CONTEXT_ASSIGNMENT,
                "Assignment of AllocationContext objects must retain @DefineScope information.");
        p.put(ERR_BAD_ASSIGNMENT_PRIVATE_MEM,
                "Cannot assign to a private memory with a different @DefineScope.");
        p.put(ERR_BAD_ASSIGNMENT_SCOPE,
                "Cannot assign expression in scope %s to variable in scope %s.");
        p.put(ERR_BAD_ENTER_PRIVATE_MEMORY_RUNS_IN_NO_MATCH,
                "The Runnable's @RunsIn must be a child scope of the current scope: \n\t @RunsIn: %s\n\t Current Scope: %s");
        p.put(ERR_BAD_ENTER_PRIVATE_MEMORY_TARGET,
                "The Runnable's @RunsIn must be a child scope of the scope represented by the @DefineScope annotation on the memory area variable.\n\t @RunsIn: %s\n\t Target Scope: %s");
        p.put(ERR_BAD_CONTEXT_CHANGE_CALLER,
        "Changing allocation context using enterPrivateMemory()/executeInArea() in scope CALLER is not allowed.");
        p.put(ERR_BAD_EXECUTE_IN_AREA_RUNS_IN,
                "Runnable and PrivateMemory scopes disagree. Target is %s, while Runnable's @RunsIn is %s.");
        p.put(ERR_BAD_EXECUTE_IN_AREA_TARGET,
                "executeInArea() must target a parent scope. Current scope is %s, while we target %s.");
        p.put(ERR_BAD_GET_CURRENT_MANAGED_MEMORY,
                "getCurrentManagedMemory may only be called from a concrete, non-IMMORTAL scope.");
        p.put(ERR_BAD_GET_MEMORY_AREA,
                "getMemoryArea may only be passed objects of known scopes.");

        p.put(ERR_BAD_GUARD_ARGUMENT,
                "Only final variables may be passed as arguments into guards.");
        p.put(ERR_BAD_METHOD_INVOKE,
                "Illegal invocation of method of object in scope %s while in scope %s. The @RunsIn annotation is not set properly.");
        p.put(ERR_BAD_NEW_ARRAY,
                "Cannot allocate objects of type %s inside scope %s.");
        p.put(ERR_BAD_NEW_ARRAY_TYPE,
                "Type %s is a bad argument to newArray.");
        p.put(ERR_BAD_NEW_INSTANCE,
                "Cannot allocate objects of type %s inside scope %s.");
        p.put(ERR_BAD_NEW_INSTANCE_TYPE,
                "Type %s is a bad argument to newInstance.");
        p.put(ERR_BAD_NEW_INSTANCE_REPRESENTED_SCOPE,
            "Bad MemoryArea instance: Cannot invoke newInstance() call on a MemoryArea object that does not represent an ancestor of the current scope.");

        p.put(ERR_BAD_RETURN_SCOPE,
                "Cannot return expression in scope %s in a method that has @Scope annotation: %s.");
        p.put(ERR_BAD_RUNS_IN_METHOD,
                "Methods must run in the same scope or a child scope of their owning type.");
        p.put(ERR_BAD_VARIABLE_SCOPE_OVER,
                "Types's  @Scope(%s) cannot be overriden by an explicit @Scope(%s).");
        p.put(ERR_BAD_VARIABLE_SCOPE,
        "Variables of type %s are not allowed in this allocation context (%s).");
        p.put(ERR_ESCAPING_NONANNOTATED_FIELD, ERR_ESCAPING_NONANNOTATED_FIELD);
        p.put(ERR_MEMORY_AREA_NO_DEFINE_SCOPE_ON_VAR,
                "Local Variable of a type that implements AllocationContext interface must have a @DefineScope annotation.");
        p.put(ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT,
                "MemoryArea variable's @DefineScope annotation is not consistent with the @DefineScope annotations on classes.");
        p.put(ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT_WITH_SCOPE,
                "MemoryArea variable @DefineScope annotation is not consistent with the @Scope annotations of the field. (Field's scope is %s, @DefineScope requires %s) ");
        //p.put(ERR_SCJ_RUNNABLE_BAD_SCOPE,
        //        "The Runnable used for enterPrivateMemory()/executeInArea() must have a @Scope annotation.");
        p.put(ERR_SCOPE_RUNS_IN_DISAGREEMENT,
                "@RunsIn annotations must be a sub-scope of @Scope annotations.");

        p.put(ERR_BAD_UPCAST,
            "Illegal Cast : Class %s overriding a @RunsIn annotation cannot be casted to the %s.");

        return p;
    }
}
