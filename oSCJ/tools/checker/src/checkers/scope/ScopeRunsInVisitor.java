package checkers.scope;

import static checkers.scope.ScopeRunsInChecker.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

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

/**
 * This visitor is responsible for retrieving Scope and RunsIn annotations from
 * classes and methods and making sure they are valid. This information is
 * stored into a context object so that the ScopeVisitor doesn't have to
 * deal with retrieving this information.
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
     *     class, unless the effective scope name is CURRENT
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
        Scope scopeAnn = t.getAnnotation(Scope.class);
        String scope = scopeAnn != null ? scopeAnn.value() : Scope.CURRENT;

        if (!scopeTree.hasScope(scope) && !scope.equals(Scope.CURRENT)) {
            report(Result.failure(ERR_BAD_SCOPE_NAME), node, errNode);
        }

        TypeElement s = Utils.superType(t);
        if (s != null) {
            String superScope = ctx.getClassScope(s);
            if (superScope == null) {
                checkClassScope(s, trees.getTree(s), node);
                superScope = ctx.getClassScope(s);
            }
            if (Scope.CURRENT.equals(superScope)) {
                ctx.setClassScope(scope, t);
            } else if (scope.equals(superScope) || Scope.CURRENT.equals(scope)) {
                ctx.setClassScope(superScope, t);
            } else {
                // Set the scope to something anyway, in case the processing
                // continues past the current class
                ctx.setClassScope(scope, t);
                report(Result.failure(ERR_ILLEGAL_SCOPE_OVERRIDE), node,
                        errNode);
            }
        } else {
            // t is java.lang.Object, just add the type in
            ctx.setClassScope(scope, t);
        }
        // Ensure that the class doesn't change any Scope annotations on its
        // implemented interfaces. This shouldn't require the retrieval of
        // all interfaces implemented by superclasses and interfaces, since
        // they should be visited as well prior to this point.
        for (TypeMirror i : t.getInterfaces()) {
            TypeElement iElem = Utils.getTypeElement(i);
            String iScope = ctx.getClassScope(iElem);
            if (iScope == null) {
                checkClassScope(iElem, trees.getTree(iElem), node);
                iScope = ctx.getClassScope(iElem);
            }
            if (!Scope.CURRENT.equals(iScope) && !iScope.equals(scope)) {
                report(Result.failure(ERR_ILLEGAL_SCOPE_OVERRIDE), node,
                        errNode);
            }
        }

        List<ExecutableElement> methods =
            ElementFilter.methodsIn(t.getEnclosedElements());
        for (ExecutableElement m : methods) {
            MethodTree mTree = trees.getTree(m);
            Tree mErr = null;
            if (node == errNode) {
                mErr = mTree;
            } else if (mTree == null) {
                mErr = node;
            }
            checkMethodScope(m, mTree, mErr);
            checkMethodRunsIn(m, mTree, mErr);
        }

        List<VariableElement> fields =
            ElementFilter.fieldsIn(t.getEnclosedElements());
        for (VariableElement f : fields) {
            Tree fTree = trees.getTree(f);
            Tree fErr = null;
            if (node == errNode) {
                fErr = fTree;
            } else if (fTree == null) {
                fErr = node;
            }
            checkFieldScope(f, fTree, fErr);
        }
        // We don't allow RunsIn annotations on classes anymore. Instead, the
        // default scope is always the same as the Scope annotation and methods
        // may override the default if they so choose.
        RunsIn runsInAnn = t.getAnnotation(RunsIn.class);
        if (runsInAnn != null) {
            report(Result.warning(ERR_RUNS_IN_ON_CLASS), node, errNode);
        }
    }

    /**
     * Check that a field has a valid Scope annotation, if any. Four kinds of
     * fields are considered:
     * <ol>
     * <li>Primitive fields have no scope
     * <li>Primitive array fields are CURRENT if not annotated, and S if
     *     annotated Scope(S)
     * <li>Object fields are CURRENT if not annotated, Scope(S) if the type of
     *     the field is annotated Scope(S), or S if the type of the field is
     *     not annotated and the field is annotated Scope(S).
     * <li>Object arrays follow the same rules as object fields based on the
     *     type of their basic element type.
     */
    void checkFieldScope(VariableElement f, Tree node, Tree errNode) {
        TypeMirror fMir = f.asType();
        Scope s = f.getAnnotation(Scope.class);
        String scope = Scope.CURRENT;
        if (s != null && s.value() != null) {
            scope = s.value();
        }
        // Arrays reside in the same scope as their element types, so if this
        // field is an array, reduce it to its base component type.
        while (fMir.getKind() == TypeKind.ARRAY) {
            fMir = ((ArrayType) fMir).getComponentType();
        }
        if (fMir.getKind() != TypeKind.DECLARED) {
            // The field type in here is either a primitive or a primitive
            // array. Only store a field scope if the field was an array.
            if (fMir != f.asType()) {
                ctx.setFieldScope(scope, f);
            }
        } else {
            TypeElement t = Utils.getTypeElement(fMir);
            String tScope = ctx.getClassScope(t);
            if (scope == null) {
                checkClassScope(t, trees.getTree(t), errNode);
            }
            tScope = ctx.getClassScope(t);
            if (tScope == Scope.CURRENT) {
                ctx.setFieldScope(scope, f);
            } else {
                ctx.setFieldScope(tScope, f);
                if (scope != null && !scope.equals(tScope)) {
                    report(Result.warning(ERR_ILLEGAL_FIELD_SCOPE_OVERRIDE),
                            node, errNode);
                }
            }
        }
    }

    /**
     * Check that a method has a valid RunsIn annotation. A method's RunsIn
     * annotation is valid as long as it exists in the scope tree, or is
     * UNKNOWN or CURRENT. It is also illegal to change the RunsIn of an
     * overridden method, unless it is annotated SUPPORT.
     */
    void checkMethodRunsIn(ExecutableElement m, MethodTree node,
            Tree errNode) {
        RunsIn ann = m.getAnnotation(RunsIn.class);
        String runsIn = ann != null ? ann.value() : Scope.CURRENT;

        if (!scopeTree.hasScope(runsIn) && !runsIn.equals(Scope.CURRENT) &&
                !runsIn.equals(Scope.UNKNOWN)) {
            report(Result.failure(ERR_BAD_SCOPE_NAME, runsIn), node, errNode);
        }

        Map<AnnotatedDeclaredType, ExecutableElement> overrides =
            ats.overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            String eRunsIn = ctx.getMethodRunsIn(e);
            SCJAllowed eLevelAnn = e.getAnnotation(SCJAllowed.class);
            Level eLevel = eLevelAnn != null ? eLevelAnn.value() : null;
            if (!eRunsIn.equals(runsIn) && eLevel != Level.SUPPORT) {
                report(Result.failure(ERR_ILLEGAL_RUNS_IN_OVERRIDE), node,
                        errNode);
            }
        }
        ctx.setMethodRunsIn(runsIn, m);
    }

    /**
     * Check that a method has a valid Scope annotation. A method's Scope
     * annotation is valid as long as it exists in the scope tree, or is
     * UNKNOWN or CURRENT. It is also illegal to change the RunsIn of an
     * overridden method, unless it is annotated SUPPORT.
     */
    void checkMethodScope(ExecutableElement m, MethodTree node,
            Tree errNode) {
        Scope ann = m.getAnnotation(Scope.class);
        String scope = ann != null ? ann.value() : Scope.CURRENT;

        if (!scopeTree.hasScope(scope) && !scope.equals(Scope.CURRENT) &&
                !scope.equals(Scope.UNKNOWN)) {
            report(Result.failure(ERR_BAD_SCOPE_NAME, scope), node, errNode);
        }
        Map<AnnotatedDeclaredType, ExecutableElement> overrides =
            ats.overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            String eScope = ctx.getMethodScope(e);
            SCJAllowed eLevelAnn = e.getAnnotation(SCJAllowed.class);
            Level eLevel = eLevelAnn != null ? eLevelAnn.value() : null;
            if (!eScope.equals(scope) && eLevel != Level.SUPPORT) {
                report(Result.failure(ERR_ILLEGAL_SCOPE_OVERRIDE), node,
                        errNode);
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
     * <li>A method in the currently visited class. We analyze this in full
     *     and report any errors we find.
     * <li>A method in a parent class or interface of the currently visited
     *     class, which is also in user code (aka not a library). We don't
     *     report any errors here, because the class will eventually be
     *     visited.
     * <li>A method in a parent class or interface of the currently visited
     *     class, which is in a library. Since there is no source for these,
     *     it is impossible to report errors on the AST of these classes.
     *     Therefore, we report that a library is badly annotated, rather than
     *     the actual error.
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
}