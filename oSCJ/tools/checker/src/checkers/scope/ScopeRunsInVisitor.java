package checkers.scope;

import static checkers.scope.ScopeRunsInChecker.*;
import static javax.safetycritical.annotate.Level.SUPPORT;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.safetycritical.annotate.DefineScope;
import javax.safetycritical.annotate.Level;
import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;

import checkers.SCJVisitor;
import checkers.Utils;
import checkers.source.Result;
import checkers.source.SourceChecker;
import checkers.types.AnnotatedTypeFactory;
import checkers.types.AnnotatedTypeMirror.AnnotatedDeclaredType;
import checkers.types.AnnotatedTypes;
import checkers.util.InternalUtils;
import checkers.util.TreeUtils;
import checkers.util.TypesUtils;

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.Tree.Kind;
import com.sun.source.tree.VariableTree;

/**
 * This visitor is responsible for retrieving Scope and RunsIn annotations from
 * classes and methods and making sure they are valid. This information is
 * stored into a context object so that the ScopeVisitor doesn't have to deal
 * with retrieving this information.
 */
public class ScopeRunsInVisitor extends SCJVisitor<Void, Void> {
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
        checkClassScope(t, node, node, true);
        return super.visitClass(node, p);
    }

    /**
     * from type declaration "class W <T extends V>", this method visits the <T extends V> part.
     */
    @Override
    public Void visitTypeParameter(TypeParameterTree node, Void p) {
        debugIndentIncrement("visitTypeParameter:" + node.toString());

        //ClassTree cll = TreeUtils.enclosingClass(getCurrentPath());
        //String encClass = TreeUtils.elementFromDeclaration(cll).getQualifiedName().toString();
        //ScopeInfo enc = ctx.getClassScope(encClass);
        //ScopeInfo scope = getScope(node.getAnnotations(), node);
        //ctx.setClassScope(scope, encClass+"."+node.getName());
        //
        // for (Tree t : node.getBounds()) {
        //     // TODO: check the bounds.
        // }

        debugIndentDecrement();
        return super.visitTypeParameter(node, p);
    }

    /**
     * To determine Scope information from parameteric types. Not finished yet.
     */
    public ScopeInfo getScope(List<? extends AnnotationTree> annotations, Tree node) {
        for (AnnotationTree at : annotations) {
            // TODO: improve this:
            if (at.toString().startsWith("@Scope")) {
                String name = at.getArguments().get(0).toString();
                return new ScopeInfo(name.replace("\"", ""));
            } else {
                fail(ERR_BAD_ANNOTATE,node, node,at.toString());
            }
        }
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierTree node, Void p) {
        // Visiting the types and super types of the provided sources is not
        // enough. Therefore, if we see any type names in the source, we
        // should check them out.
        Element elem = TreeUtils.elementFromUse(node);
        if (elem.getKind() == ElementKind.CLASS
                || elem.getKind() == ElementKind.INTERFACE) {
            TypeElement t = (TypeElement) elem;
            checkClassScope(t, trees.getTree(t), node, false);
        }
        return super.visitIdentifier(node, p);
    }

    @Override
    public Void visitMemberSelect(MemberSelectTree node, Void p) {
        Element elem = TreeUtils.elementFromUse(node);
        TypeMirror mirror = InternalUtils.typeOf(node.getExpression());
        // This hack is necessary because if we have an expression such as
        // "byte.class" and we convert this to a VariableElement, byte is
        // considered a class type. Trying to call checkClassScope explodes.
        if (!mirror.getKind().isPrimitive()
                && elem.getKind() == ElementKind.FIELD) {
            VariableElement f = (VariableElement) elem;
            TypeElement t = Utils.getFieldClass(f);
            if (node.getExpression().getKind() != Kind.PRIMITIVE_TYPE) {
                checkClassScope(t, trees.getTree(t), node, false);
            }
        }
        return super.visitMemberSelect(node, p);
    }

    @Override
    public Void visitMethodInvocation(MethodInvocationTree node, Void p) {
        // Visiting the types and super types of the provided sources is not
        // enough. In addition to visitIdentifier, we need to check the
        // return types of method invocations, because it's possible to use
        // a type without ever having its type name mentioned through method
        // chaining.
        TypeMirror mirror = InternalUtils.typeOf(node);
        if (mirror.getKind() == TypeKind.DECLARED) {
            TypeElement t = Utils.getTypeElement(mirror);
            checkClassScope(t, trees.getTree(t), node, false);
        }
        // Also visit types that the methods belong to. This covers the case
        // when a a method of a type is used without the type itself being
        // mentioned. It also covers a peculiar case when array methods are
        // used. For example, new byte[] { 1 }.clone() would crash without
        // this, because Array (note the lack of package) does not extend
        // java.lang.Object.
        ExecutableElement m = TreeUtils.elementFromUse(node);
        TypeElement t = Utils.getMethodClass(m);
        checkClassScope(t, trees.getTree(t), node, false);
        return super.visitMethodInvocation(node, p);
    }

    @Override
    public Void visitPrimitiveType(PrimitiveTypeTree node, Void p) {
        TypeMirror m = InternalUtils.typeOf(node);
        TypeElement boxed = types.boxedClass((PrimitiveType) m);
        checkClassScope(boxed, null, node, false);
        return super.visitPrimitiveType(node, p);
    }

    @Override
    public Void visitVariable(VariableTree node, Void p) {
        VariableElement v = TreeUtils.elementFromDeclaration(node);
        if (v.getKind() == ElementKind.LOCAL_VARIABLE) {
            Scope s = v.getAnnotation(Scope.class);
            if (s != null) {
                ScopeInfo si = new ScopeInfo(s.value());
                if (!si.isValidVariableScope(v, scopeTree))
                    fail(ERR_BAD_SCOPE_NAME, node, si);
            }
        }
        return super.visitVariable(node, p);
    }

    /**
     * Check that a class has a valid Scope annotation.
     * <ul>
     * <li>The scope name must exist in the scope tree or be CALLER
     * <li>The scope name must match the effective scope name of its parent
     * class, unless the effective scope name is CALLER
     * </ul>
     * <p>
     * The effective scope name of a class C which is annotated Scope(S) is:
     * <ul>
     * <li>S, if S is not CALLER
     * <li>The effective scope name of D, if C extends D and S is CALLER
     * </ul>
     * <p>
     * If C has no explicit Scope annotation, it is assumed to be annotated as
     * Scope(CALLER).
     */
    void checkClassScope(TypeElement t, ClassTree node, Tree errNode,
            boolean forceVisit) {
        debugIndentIncrement("checkClassScope: " + t);

        if (!(ctx.getClassScope(t) == null || forceVisit)) {
            // Already visited or is in the process of being visited
            debugIndentDecrement();
            return;
        }
        ScopeInfo scope = scopeOfClassDefinition(t);
        if (!scopeTree.hasScope(scope) && !scope.isCaller())
            fail(ERR_BAD_SCOPE_NAME, node, errNode, scope);

        TypeElement p = Utils.superType(t);
        if (p == null)
            ctx.setClassScope(scope, t); // t == java.lang.Object
        else {
            ScopeInfo parent = getClassScopeAndVisit(p, errNode);
            ScopeInfo enclosed = getEnclosingClassScopeRecursive(t);

            ScopeInfo sc = null;
            if (parent.isCaller())
                sc = scope;
            else if (scope.equals(parent))
                sc = parent;
            else {
                // Set the scope to something, in case processing continues past this class
                //ctx.setClassScope(scope, t);
                sc = scope;
                fail(ERR_ILLEGAL_CLASS_SCOPE_OVERRIDE, node, errNode, t, p);
            }

            if (enclosed != null) {
                if (Utils.isStatic(t)) {
                    // static inner class is independent from the enclosing class
                    ctx.setClassScope(sc, t);
                } else {
                    if (enclosed.isCaller()) {
                        if (sc.isCaller()) {
                            ctx.setClassScope(sc, t);
                        }
                        else {
                            ctx.setClassScope(sc, t);
                            // if enclosed is CALLER, we are not allowed to override it
                            fail(ERR_BAD_INNER_SCOPE_NAME, node, errNode, sc, enclosed);
                        }
                    } else {
                        if (sc.isCaller()) {
                            // if enclosed is some named-scope, we need to restate it also
                            // for the innter class
                            ctx.setClassScope(enclosed, t);
                            fail(ERR_BAD_INNER_SCOPE_NAME, node, errNode, sc, enclosed);
                        }
                        else if (!sc.equals(enclosed)) {
                            // if non-static inner class changes the @Scope then its an error
                            ctx.setClassScope(sc, t);
                            fail(ERR_BAD_INNER_SCOPE_NAME, node, errNode, sc, enclosed);
                        } else
                            ctx.setClassScope(sc, t);
                    }
                }
            } else
                ctx.setClassScope(sc, t);
        }

        // Ensure that the class doesn't change any Scope annotations on its
        // implemented interfaces. This shouldn't require the retrieval of
        // all interfaces implemented by superclasses and interfaces, since
        // they should be visited as well prior to this point.
        for (TypeMirror i : t.getInterfaces()) {
            TypeElement ie = Utils.getTypeElement(i);
            ScopeInfo is = getClassScopeAndVisit(ie, errNode);
            if (!is.isCaller() && !is.equals(scope))
                fail(ERR_ILLEGAL_CLASS_SCOPE_OVERRIDE, node, errNode, t, ie);
        }

        for (ExecutableElement c : Utils.constructorsIn(t)) {
            MethodTree mTree = trees.getTree(c);
            Tree mErr = mTree != null ? mTree : errNode;
            checkConstructor(c, mTree, mErr, true);
        }

        for (ExecutableElement m : Utils.methodsIn(t)) {
            MethodTree mTree = trees.getTree(m);
            Tree mErr = mTree != null ? mTree : errNode;
            checkMethod(m, mTree, mErr, true);
        }

        for (VariableElement f : Utils.fieldsIn(t)) {
            Tree fTree = trees.getTree(f);
            Tree fErr = fTree != null ? fTree : errNode;
            ScopeInfo fScope = checkField(f, fTree, fErr);
            if (fScope != null)
                ctx.setFieldScope(fScope, f);
        }
        // We don't allow RunsIn annotations on classes anymore.
        if (t.getAnnotation(RunsIn.class) != null)
            fail(ERR_RUNS_IN_ON_CLASS, node, errNode);

        debugIndentDecrement();
    }

    /**
     *
     * @return
     *  - null - the class is not an inner class
     *  - CALLER - the enclosing class has no annotation
     *  - named-scope - enclosing class has an annotation.
     */
    private ScopeInfo getEnclosingClassScopeRecursive(Element elem) {
        ScopeInfo scope = null;
        String clazz = elem.toString();
        while (elem != null) {
            if ( clazz.lastIndexOf(".") == -1)
                return null;
            clazz = clazz.substring(0, clazz.lastIndexOf("."));
            ScopeInfo enclosed = ctx.getClassScope(clazz);
            if (enclosed == null && scope == null)
                return null;
            else if (enclosed == null )
                return scope;
            else
                scope = enclosed;

            if (!scope.isCaller())
                break;
        }
        return scope;
    }

    private void checkConstructor(ExecutableElement m, MethodTree mTree,
            Tree mErr, boolean forceVisit) {
        RunsIn runsIn = m.getAnnotation(RunsIn.class);
        if (runsIn != null) {
            String msg = "\n\t ERROR class is :" + Utils.getMethodClass(m)
                    + "." + m;
            fail(ERR_RUNS_IN_ON_CONSTRUCTOR, mTree, mErr, msg);
        }

        checkMethod(m, mTree, mErr, forceVisit);
    }

    void checkMethod(ExecutableElement m, MethodTree mTree, Tree errNode,
            boolean forceVisit) {
        debugIndentIncrement("checkMethod: " + m);

        if (!(ctx.getMethodScope(m) == null || forceVisit)) {
            // Already visited or in the process of being visited
            debugIndentDecrement();
            return;
        }
        // The RunsIn must be checked first, because checkMethodScope uses it.
        checkMethodRunsIn(m, mTree, errNode);
        checkMethodScope(m, mTree, errNode);
        checkMethodParameters(m, mTree, errNode);

        debugIndentDecrement();
    }

    void checkMethodParameters(ExecutableElement m, MethodTree mTree,
            Tree errNode) {
        List<? extends VariableElement> params = m.getParameters();
        List<? extends VariableTree> paramTrees = mTree != null ? mTree
                .getParameters() : null;
        for (int i = 0; i < params.size(); i++) {
            VariableElement p = params.get(i);
            VariableTree pTree = paramTrees != null ? paramTrees.get(i) : null;
            ScopeInfo scope = checkMethodParameter(p, pTree, i, m, errNode);

            ctx.setParameterScope(scope, i, m);
        }
    }

    ScopeInfo checkMethodParameter(VariableElement p, VariableTree tree, int i,
            ExecutableElement m, Tree errNode) {
        ScopeInfo scope = checkVariableScopeOverride(p, tree, errNode);
        ScopeInfo effectiveScope = scope;
        if (scope.isCaller())
            effectiveScope = ctx.getEffectiveMethodRunsIn(m,
                    getEnclosingClassScope(m), ScopeInfo.CALLER);
        scope = checkMemoryAreaVariable(p, effectiveScope, tree, errNode);

        return scope;
    }

    ScopeInfo checkField(VariableElement f, Tree node, Tree errNode) {
        ScopeInfo scope = checkVariableScopeOverride(f, node, errNode);
        ScopeInfo classScope = getEnclosingClassScope(f);

        if (Utils.isStatic(f)) {
            if (!scope.isValidStaticScope()) {
                fail(ERR_ILLEGAL_STATIC_FIELD_SCOPE, node, errNode, scope);
                // set the scope to something before the checker reports the error.
                scope = ScopeInfo.THIS;
            }

            if (scope.isCaller())
                scope = ScopeInfo.IMMORTAL;
        } else if (!scope.isValidInstanceFieldScope(classScope, scopeTree)) {
            fail(ERR_ILLEGAL_FIELD_SCOPE, node, errNode, scope, classScope);
            // set the scope to something before the checker reports the error.
            scope = ScopeInfo.THIS;
        }

        scope = checkMemoryAreaVariable(f, scope, node, errNode);
        return scope;
    }

    private ScopeInfo checkMemoryAreaVariable(VariableElement v,
            ScopeInfo effectiveVarScope, Tree node, Tree errNode) {
        if (!Utils.isUserLevel(v))
            return effectiveVarScope;
        if (v.asType().getKind() != TypeKind.DECLARED)
            return effectiveVarScope;
        if (!needsDefineScope(Utils.getTypeElement(v.asType())))
            return effectiveVarScope;

        DefineScope ds = v.getAnnotation(DefineScope.class);
        if (ds == null) {
            fail(ERR_MEMORY_AREA_NO_DEFINE_SCOPE, node, errNode);
            return effectiveVarScope.representing(ScopeInfo.INVALID);
        }

        ScopeInfo scope = new ScopeInfo(ds.name());
        ScopeInfo parent = new ScopeInfo(ds.parent());

        if (!scopeTree.hasScope(scope) || !scopeTree.isParentOf(scope, parent)) {
            fail(ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT, node, errNode);
        }

        if (!effectiveVarScope.equals(parent))
            fail(ERR_MEMORY_AREA_DEFINE_SCOPE_NOT_CONSISTENT_WITH_SCOPE, node,
                    errNode, effectiveVarScope, parent);

        return effectiveVarScope.representing(scope);
    }

    /**
     * Check that a variable has a valid Scope annotation, if any. Four kinds of
     * variables are considered:
     * <ol>
     * <li>Primitive variables have no scope
     * <li>Primitive array variables are CALLER if not annotated, and S if
     * annotated Scope(S)
     * <li>Object variables are CALLER if not annotated, Scope(S) if the type of
     * the variable is annotated Scope(S), or S if the type of the variable is
     * not annotated and the field is annotated Scope(S).
     * <li>Object arrays follow the same rules as object variables based on the
     * type of their basic element type.
     */
    ScopeInfo checkVariableScopeOverride(VariableElement v, Tree node,
            Tree errNode) {
        debugIndent("checkVariableScopeOverride: " + v);
        TypeMirror mv = v.asType();
        TypeMirror bmv = Utils.getBaseType(mv);
        ScopeInfo ret;

        if (bmv.getKind() == TypeKind.DECLARED)
            getClassScopeAndVisit(Utils.getTypeElement(bmv), errNode);

        ScopeInfo defaultScope = Utils.getDefaultVariableScope(v, ctx);

        if (!defaultScope.isValidVariableScope(v, scopeTree))
            fail(ERR_BAD_SCOPE_NAME, node, errNode, defaultScope);

        if (Utils.isPrimitive(mv))
            ret = ScopeInfo.PRIMITIVE;
        else if (Utils.isPrimitiveArray(mv))
            ret = defaultScope;
        else if (bmv.getKind() == TypeKind.TYPEVAR)
            ret = defaultScope;
        else if (mv.getKind() == TypeKind.DECLARED) {
            ScopeInfo stv = ctx.getClassScope(Utils.getTypeElement(bmv));
            if (stv.isCaller())
                ret = defaultScope;
            else {
                if (defaultScope.isUnknown() || !defaultScope.equals(stv))
                    fail(ERR_ILLEGAL_VARIABLE_SCOPE_OVERRIDE, node, errNode,
                            defaultScope, stv);
                ret = stv;
            }
        } else if (mv.getKind() == TypeKind.ARRAY) {
            ret = defaultScope;
            // If the array variable is a field in a parent scope of the base
            // element type, it's illegal and this is how it's caught.
            if (v.getKind() == ElementKind.FIELD) {
                ScopeInfo classScope = ctx
                        .getClassScope(Utils.getFieldClass(v));
                ScopeInfo elemScope = ctx.getClassScope(Utils
                        .getTypeElement(bmv));

                if (!elemScope.isCaller()
                        && scopeTree.isAncestorOf(elemScope, classScope))
                    ret = elemScope;
            }
        } else
            throw new RuntimeException("missing case");

        return ret;
    }

    /**
     * Check that a method has a valid RunsIn annotation. A method's RunsIn
     * annotation is valid as long as it exists in the scope tree, or is CALLER
     * or THIS. It is also illegal to change the RunsIn of an overridden method,
     * unless it is annotated SUPPORT.
     */
    void checkMethodRunsIn(ExecutableElement m, MethodTree node, Tree errNode) {
        RunsIn ann = m.getAnnotation(RunsIn.class);
        ScopeInfo runsIn = ann != null ? new ScopeInfo(ann.value()) : Utils
                .getDefaultMethodRunsIn(m);

        // UNKNOWN is no longer a valid RunsIn annotation.
        if (!runsIn.isValidRunsIn(scopeTree))
            fail(ERR_BAD_SCOPE_NAME, node, errNode, runsIn);
        if (Utils.isStatic(m) && runsIn.isThis())
            fail(ERR_BAD_SCOPE_NAME, node, errNode, runsIn);

        Map<AnnotatedDeclaredType, ExecutableElement> overrides = ats
                .overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            ScopeInfo eRunsIn = getOverrideRunsInAndVisit(e, errNode);

            if (!Utils.isUserLevel(m)) {
                // SCJ packages are not checked
                break;
            }

        }

        ctx.setMethodRunsIn(runsIn, m);
    }

    /**
     * Check that a method has a valid Scope annotation. A method's Scope
     * annotation is valid as long as it exists in the scope tree, or is
     * UNKNOWN, CALLER, or THIS. It is also illegal to change the RunsIn of an
     * overridden method, unless it is annotated SUPPORT.
     */
    void checkMethodScope(ExecutableElement m, MethodTree node, Tree errNode) {
        Scope ann = m.getAnnotation(Scope.class);
        ScopeInfo scope = ann != null ? new ScopeInfo(ann.value())
                : ScopeInfo.CALLER;

        if (!scopeTree.hasScope(scope) && !scope.isCaller() && !scope.isThis()
                && !scope.isUnknown())
            fail(ERR_BAD_SCOPE_NAME, node, errNode, scope);

        ScopeInfo runsIn = ctx.getMethodRunsIn(m);
        TypeMirror r = m.getReturnType();
        TypeKind k = r.getKind();

        if (runsIn.isThis() && scope.isCaller())
            scope = ScopeInfo.THIS;

        if ((k.isPrimitive() || k == TypeKind.VOID)) {
            if (ann != null)
                fail(ERR_SCOPE_ON_VOID_OR_PRIMITIVE_RETURN, node, errNode);
            scope = ScopeInfo.PRIMITIVE;
        } else if (r.getKind() == TypeKind.DECLARED) {
            TypeElement t = Utils.getTypeElement(r);
            ScopeInfo classScope = getClassScopeAndVisit(t, errNode);
            if (!classScope.isCaller())
                scope = classScope;
        }

        Map<AnnotatedDeclaredType, ExecutableElement> overrides = ats
                .overriddenMethods(m);
        for (ExecutableElement e : overrides.values()) {
            ScopeInfo eScope = getOverrideScopeAndVisit(e, errNode);
            SCJAllowed eLevelAnn = e.getAnnotation(SCJAllowed.class);
            Level eLevel = eLevelAnn != null ? eLevelAnn.value() : null;
            //if (!eScope.equals(scope) && eLevel != SUPPORT) {
            //    fail(ERR_ILLEGAL_METHOD_SCOPE_OVERRIDE, node, errNode);
           // }
        }
        ctx.setMethodScope(scope, m);
    }

    /**
     * Report the proper error from the context of the current check method.
     * Each check method may be invoked on two categories of items:
     * <ol>
     * <li>A code element that is in the compiled set of files (aka has source).
     * We analyze this in full and report any errors we find.
     * <li>A code element that is in a library. Since there is no source for
     * these, it is impossible to report errors on the AST of these classes.
     * Therefore, we report that a library is badly annotated, rather than the
     * actual error.
     * </ol>
     */
    void report(Result r, Tree node, Tree errNode) {
        if (node != null && trees.getPath(root, errNode) == null)
            // If the node is not in the current compilation unit, don't report
            // an error. It will be reported later when it is visited properly
            // by the checker.
            return;
        if (node != null)
            // Current item being visited. Report the error as usual.
            checker.report(r, errNode);
        else if (r.isFailure()) {
            // Current item is something from a library. Can't put an error on
            // it, so put an error on the node being visited stating that
            // something from the parent class or interface is broken. If the
            // result is a warning, we ignore it, since they are purely
            // informational.
            fail(ERR_BAD_LIBRARY_ANNOTATION, errNode);
        }
    }

    void fail(String msg, Tree src, Tree err, Object... msgParams) {
        report(Result.failure(msg, msgParams), src, err);
    }

    void warn(String msg, Tree src, Tree err, Object... msgParams) {
        report(Result.warning(msg, msgParams), src, err);
    }

    private ScopeInfo getClassScopeAndVisit(TypeElement p, Tree errNode) {
        ScopeInfo parent = ctx.getClassScope(p);
        if (parent == null) {
            checkClassScope(p, trees.getTree(p), errNode, false);
            parent = ctx.getClassScope(p);
        }
        return parent;
    }

    private ScopeInfo getOverrideScopeAndVisit(ExecutableElement m, Tree errNode) {
        ScopeInfo scope = ctx.getMethodScope(m);
        if (scope != null)
            return scope;
        checkMethod(m, trees.getTree(m), errNode, false);
        return ctx.getMethodScope(m);
    }

    private ScopeInfo getOverrideRunsInAndVisit(ExecutableElement m,
            Tree errNode) {
        ScopeInfo runsIn = ctx.getMethodRunsIn(m);
        if (runsIn != null)
            return runsIn;
        checkMethod(m, trees.getTree(m), errNode, false);
        return ctx.getMethodRunsIn(m);
    }

    /**
     * Get a method or field's owning class.
     */
    private ScopeInfo getEnclosingClassScope(Element e) {
        return ctx.getClassScope((TypeElement) e.getEnclosingElement());
    }
}
