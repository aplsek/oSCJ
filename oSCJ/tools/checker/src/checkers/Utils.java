package checkers;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.safetycritical.annotate.SCJRestricted;

import checkers.types.AnnotatedTypes;
import checkers.util.TypesUtils;

public final class Utils {
    /**
     * debugging flag
     */
    public static boolean DEBUG = true;

    public static void debugPrint(String msg) {
        if (DEBUG) System.err.print(indent + msg);
    }

    public static void debugPrintln(String msg) {
        if (DEBUG) System.err.println(indent + msg);
    }

    public static void debugPrintException(Exception e) {
        System.err.println("\n\n Exception: " + e.getMessage());
    }

    public static String annotationValue(Collection<? extends AnnotationMirror> annotations, String annotation) {
        AnnotationMirror a = getAnnotation(annotations, annotation);
        if (a != null) { return (String) a.getElementValues().values().iterator().next().getValue(); }
        return null;
    }

    public static AnnotationMirror getAnnotation(Collection<? extends AnnotationMirror> annotations, String annotation) {
        for (AnnotationMirror anno : annotations) {
            if (anno.getAnnotationType().toString().equals(annotation)) { return anno; }
        }
        return null;
    }

    public static boolean isStatic(Collection<Modifier> modifiers) {
        return modifiers.contains(Modifier.STATIC);
    }

    public static boolean isFinal(Collection<Modifier> modifiers) {
        return modifiers.contains(Modifier.FINAL);
    }

    public static boolean isAllocFree(ExecutableElement methodElement, AnnotatedTypes ats) {
        SCJRestricted r;
        if ((r = methodElement.getAnnotation(SCJRestricted.class)) != null && r.value() != null) {
            return !r.mayAllocate();
        }
        return false;
    }

    public static String scope(Collection<? extends AnnotationMirror> annotations) {
        return annotationValue(annotations, "javax.safetycritical.annotate.Scope");
    }

    public static String runsIn(Collection<? extends AnnotationMirror> annotations) {
        return annotationValue(annotations, "javax.safetycritical.annotate.RunsIn");
    }

    public static HashSet<TypeElement> getAllInterfaces(TypeElement type) {
        HashSet<TypeElement> ret = new LinkedHashSet<TypeElement>();
        for (TypeMirror iface : type.getInterfaces()) {
            TypeElement ifaceElement = (TypeElement) ((DeclaredType) iface).asElement();
            ret.add(ifaceElement);
            ret.addAll(getAllInterfaces(ifaceElement));
        }
        if (type.getKind() == ElementKind.CLASS && !TypesUtils.isObject(type.asType())) {
            ret.addAll(getAllInterfaces((TypeElement) ((DeclaredType) type.getSuperclass()).asElement()));
        }
        return ret;
    }

    public static TypeElement superType(TypeElement type) {
        //Utils.debugPrintln("superType: " + TypesUtils.isObject(type.asType()) );
        //Utils.debugPrintln("as type: " + type.asType() );
        //Utils.debugPrintln("super : " + type.getSuperclass());

        if (TypesUtils.isObject(type.asType())) { return null; }
        if (type.getSuperclass().toString().equals("<none>")) return null;

        return (TypeElement) ((DeclaredType) type.getSuperclass()).asElement();
    }


    private static String indent = "";

    public static void increaseIndent() {
        indent += " ";
    }

    public static void decreaseIndent() {
        indent = indent.substring(1);
    }
}
