package checkers.scope;

import static checkers.scope.ScopeRunsInChecker.ERR_BAD_LIBRARY_ANNOTATION;
import static checkers.scope.ScopeRunsInChecker.ERR_BAD_SCOPE_NAME;
import static checkers.scope.ScopeRunsInChecker.ERR_ILLEGAL_SCOPE_OVERRIDE;
import static checkers.scope.ScopeRunsInChecker.ERR_ILLEGAL_METHOD_RUNS_IN_OVERRIDE;
import static checkers.scope.ScopeRunsInChecker.ERR_ILLEGAL_METHOD_SCOPE_OVERRIDE;
import static checkers.scope.ScopeRunsInChecker.ERR_RUNS_IN_ON_CLASS;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import static javax.safetycritical.annotate.Level.*;
import javax.safetycritical.annotate.Scope;
import static javax.safetycritical.annotate.Scope.*;

import checkers.Utils;
import checkers.source.Result;
import checkers.source.SourceChecker;
import checkers.source.SourceVisitor;
import checkers.types.AnnotatedTypeFactory;
import checkers.types.AnnotatedTypeMirror.AnnotatedDeclaredType;
import checkers.types.AnnotatedTypes;
import checkers.util.TreeUtils;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

/**
 * This visitor is responsible for retrieving Scope and RunsIn annotations from
 * classes and methods and making sure they are valid. This information is
 * stored into a context object so that the ScopeVisitor doesn't have to deal
 * with retrieving this information.
 */
@SuppressWarnings("restriction")
public class ScopeRunsInVisitor extends SourceVisitor<Void, Void> {
    private ScopeCheckerContext ctx;
    private ScopeTree scopeTree;
    private AnnotatedTypeFactory atf;
    private AnnotatedTypes ats;

    public ScopeRunsInVisitor(SourceChecker checker, CompilationUnitTree root,
            ScopeCheckerContext ctx) {
        super(checker, root);
        this.ctx = ctx;
        scopeTree = ctx.getScopeTree();
        atf = checker.createFactory(root);
        ats = new AnnotatedTypes(checker.getProcessingEnvironment(), atf);
    }

    @Override
    public Void visitClass(ClassTree node, Void p) {
        TypeElement t = TreeUtils.elementFromDeclaration(node);
        checkClassScope(t, node, node);
        return super.visitClass(node, p);
    }

    /**
     * Check that a class has a valid Scope annotation.
     * <ul>
     * <li>The scope name must exist in the scope tree or be CURRENT
     * <li>The scope name must match the effective scope name of its parent
     * class, unless the effective scope name is CURRENT
     * </ul>
     * <p>
     * The effective scope name of a class C which is annotated Scope(S) is:
     * <ul>
     * <li>S, if S is not CURRENT
     * <li>The effective scope name of D, if C extends D and S is CURRENT
     * </ul>
     * <p>
     * If C has no explicit Scope annotation, it is assumed to be annotated as
     * Scope(CURRENT).
     */
    void checkClassScope(TypeElement t, ClassTree node, Tree errNode) {
        debugIndentIncrement("checkClassScope : " + node);
        debugIndent("class type: " + t);
        
        String scope = scopeOfClassDefinition(t);
        if (!scopeTree.hasScope(scope) && !scope.equals(CURRENT)) {
            fail(ERR_BAD_SCOPE_NAME, node, errNode);
        }

        TypeElement p = Utils.superType(t);
        if (p == null) {
            ctx.setClassScope(scope, t); // t == java.lang.Object
        } else {
            String parent = getParentScopeAndVisit(p, node);
            if (parent.equals(CURRENT)) {
                ctx.setClassScope(scope, t);
            } else if (scope.equals(parent)) {
                ctx.setClassScope(parent, t);
            } else {
                // Set the scope to something, in case processing continues past
                // this class
                ctx.setClassScope(scope, t);
                fail(ERR_ILLEGAL_METHOD_SCOPE_OVERRIDE, node, errNode);
            }
        }
        // Ensure that the class doesn't change any Scope annotations on its
        // implemented interfaces. This shouldn't require the retrieval of
        // all interfaces implemented by superclasses and interfaces, since
        // they should be visited as well prior to this point.
        for (TypeMirror i : t.getInterfaces()) {
            TypeElement ie = Utils.getTypeElement(i);
            String is = getParentScopeAndVisit(ie, node);
            if (!is.equals(CURRENT) && !is.equals(scope)) {
                fail(ERR_ILLEGAL_METHOD_SCOPE_OVERRIDE, node, errNode);
            }
        }

        for (ExecutableElement m : Utils.methodsIn(t)) {
            MethodTree mTree = trees.getTree(m);
            Tree mErr = (node == errNode) ? mTree : ((mTree == null) ? node
                    : null);
            checkMethod(m, mTree, mErr);
        }

        for (VariableElement f : Utils.fieldsIn(t)) {
            Tree fTree = trees.getTree(f);
            Tree fErr = (node == errNode) ? fTree : ((fTree == null) ? node
                    : null);
            String fScope = checkVariableScopeOverride(f, fTree, fErr);
            if (fScope != null) {
                ctx.setFieldScope(fScope, f);
            }
        }
        // We don't allow RunsIn annotations on classes anymore.
        if (t.getAnnotation(RunsIn.class) != null) {
            fail(ERR_RUNS_IN_ON_CLASS, node, errNode);
        }
        
        debugIndentDecrement();
    }

    void checkMethod(ExecutableElement m, MethodTree mTree, Tree mErr) {
        debugIndentIncrement("checkMethod : " + m);
        
        checkMethodScope(m, mTree, mErr);
        checkMethodRunsIn(m, mTree, mErr);
        List<? extends VariableElement> params = m.getParameters();
        List<? extends VariableTree> paramTrees = mTree != null ? mTree
                .getParameters() : null;
        for (int i = 0; i < params.size(); i++) {
            VariableElement param = params.get(i);
            VariableTree paramTree = paramTrees != null ? paramTrees.get(i)
                    : null;
            String scope = checkVariableScopeOverride(param, paramTree, mErr);
            if (scope != null) {
                ctx.setParameterScope(scope, i, m);
            }
        }
        debugIndentDecrement();
    }

    /**
     * Check that a variable has a valid Scope annotation, if any. Four kinds of
     * variables are considered:
     * <ol>
     * <li>Primitive variables have no scope
     * <li>Primitive array variables are CURRENT if not annotated, and S if
     * annotated Scope(S)
     * <li>Object variables are CURRENT if not annotated, Scope(S) if the type
     * of the variable is annotated Scope(S), or S if the type of the variable
     * is not annotated and the field is annotated Scope(S).
     * <li>Object arrays follow the same rules as object variables based on the
     * type of their basic element type.
     */
    String checkVariableScopeOverride(VariableElement f, Tree node, Tree errNode) {
        debugIndentIncrement("checkVariableScopeOverride : " + f);
        debugIndent("node : " + node);
        
        TypeMirror fMir = f.asType();
        Scope s = f.getAnnotation(Scope.class);
        String scope = CURRENT;
        String ret;
        if (s != null && s.value() != null) {
            scope = s.value();
        }
        // Arrays reside in the same scope as their element types, so if this
        // field is an array, reduce it to its base component type.
        fMir = getArrayBaseType(fMir);
        if (fMir.getKind() != TypeKind.DECLARED) {
            // The field type in here is either a primitive or a primitive
            // array. Only store a field scope if the field was an array.
            if (fMir != f.asType()) {
                ret = scope;
            } else {
                ret = null; // Primitives have no scope
            }
        } else {
            TypeElement t = Utils.getTypeElement(fMir);
            String tScope = ctx.getClassScope(t);
            if (tScope == null) {
                checkClassScope(t, trees.getTree(t), errNode);
            }
            tScope = ctx.getClassScope(t);
            if (tScope == CURRENT) {
                ret = scope;
            } else {
                ret = tScope;
                if (scope != null && !scope.equals(tScope)) {
                    report(Result.warning(ERR_ILLEGAL_SCOPE_OVERRIDE), node,
                            errNode);
                }
            }
        }
        debugIndentDecrement();
        return ret;
    }

    /**
     * Check that a method has a valid RunsIn annotation. A method's RunsIn
     * annotation is valid as long as it exists in the scope tree, or is UNKNOWN
     * or CURRENT. It is also illegal to change the RunsIn of an overridden
     * method, unless it is annotated SUPPORT.
     */
    void checkMethodRunsIn(ExecutableElement m, MethodTree node, Tree errNode) {
        RunsIn ann = m.getAnnotation(RunsIn.class);
        String runsIn = ann != null ? ann.value() : CURRENT;

        if (!scopeTree.hasScope(runsIn) && !runsIn.equals(CURRENT)
                && !runsIn.equals(UNKNOWN)) {
            report(Result.failure(ERR_BAD_SCOPE_NAME, runsIn), node, errNode);
        }

        Map<AnnotatedDeclaredType, ExecutableElement> overrides = ats
                .overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            String eRunsIn = ctx.getMethodRunsIn(e);
            SCJAllowed eLevelAnn = e.getAnnotation(SCJAllowed.class);
            Level eLevel = eLevelAnn != null ? eLevelAnn.value() : null;
            if (!eRunsIn.equals(runsIn) && eLevel != SUPPORT) {
                report(Result.failure(ERR_ILLEGAL_METHOD_RUNS_IN_OVERRIDE),
                        node, errNode);
            }
        }
        ctx.setMethodRunsIn(runsIn, m);
    }

    /**
     * Check that a method has a valid Scope annotation. A method's Scope
     * annotation is valid as long as it exists in the scope tree, or is UNKNOWN
     * or CURRENT. It is also illegal to change the RunsIn of an overridden
     * method, unless it is annotated SUPPORT.
     */
    void checkMethodScope(ExecutableElement m, MethodTree node, Tree errNode) {
        Scope ann = m.getAnnotation(Scope.class);
        String scope = ann != null ? ann.value() : CURRENT;

        if (!scopeTree.hasScope(scope) && !scope.equals(CURRENT)
                && !scope.equals(UNKNOWN)) {
            report(Result.failure(ERR_BAD_SCOPE_NAME, scope), node, errNode);
        }
        Map<AnnotatedDeclaredType, ExecutableElement> overrides = ats
                .overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            String eScope = ctx.getMethodScope(e);
            SCJAllowed eLevelAnn = e.getAnnotation(SCJAllowed.class);
            Level eLevel = eLevelAnn != null ? eLevelAnn.value() : null;
            if (!eScope.equals(scope) && eLevel != SUPPORT) {
                fail(ERR_ILLEGAL_METHOD_SCOPE_OVERRIDE, node, errNode);
            }
        }
        // TODO: Need to check that scopes agree on the method and the return
        // type
        ctx.setMethodScope(scope, m);
    }

    /**
     * Report the proper error from the context of the current check method.
     * Each check method may be invoked on three categories of methods:
     * <ol>
     * <li>A method in the currently visited class. We analyze this in full and
     * report any errors we find.
     * <li>A method in a parent class or interface of the currently visited
     * class, which is also in user code (aka not a library). We don't report
     * any errors here, because the class will eventually be visited.
     * <li>A method in a parent class or interface of the currently visited
     * class, which is in a library. Since there is no source for these, it is
     * impossible to report errors on the AST of these classes. Therefore, we
     * report that a library is badly annotated, rather than the actual error.
     * </ol>
     */
    void report(Result r, Tree node, Tree errNode) {
        if (node == errNode) {
            // Current item being visited. Report the error as usual.
            checker.report(r, errNode);
        } else if (node == null) {
            // Current item is something from a library. Can't put an error on
            // it, so put an error on the node being visited stating that
            // something from the parent class or interface is broken.
            checker.report(Result.failure(ERR_BAD_LIBRARY_ANNOTATION), errNode);
        }
        // The case where node != null is omitted. This is where the current
        // item is not what we're visiting, but still has an AST and is
        // therefore part of the code being compiled. The code will be visited
        // later on and the errors will be reported then.
    }

    void fail(String s, Tree n, Tree e) {
        report(Result.failure(s), n, e);
    }

    void warn(String s, ClassTree n, Tree e) {
        report(Result.warning(s), n, e);
    }

    private static String scopeOfClassDefinition(TypeElement t) {
        Scope scopeAnn = t.getAnnotation(Scope.class);
        return scopeAnn != null ? scopeAnn.value() : CURRENT;
    }

    private String getParentScopeAndVisit(TypeElement p, ClassTree node) {
        String parent = ctx.getClassScope(p);
        if (parent == null) { // check the parent, and ad it's scope to ctx
            checkClassScope(p, trees.getTree(p), node);
            parent = ctx.getClassScope(p);
        }
        return parent;
    }

    static TypeMirror getArrayBaseType(TypeMirror t) {
        while (t.getKind() == TypeKind.ARRAY) {
            t = ((ArrayType) t).getComponentType();
        }
        return t;
    }
    
    /*
     * Debug/helper methods
     */

    private String indent = "";

    private void debugIndentDecrement() {
        indent = indent.substring(1);
    }

    private void debugIndentIncrement(String method) {
        Utils.debugPrintln(indent + method);
        indent += " ";
    }

    private void debugIndent(String method) {
        Utils.debugPrintln(indent + method);
    }

    static private void pln(String s) {
        System.out.println(s);
    }
}
