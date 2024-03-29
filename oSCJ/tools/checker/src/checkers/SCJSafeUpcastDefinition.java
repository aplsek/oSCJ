package checkers;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Special Cases for UPCASTS:
 *
 * Defines a set pairs <class,method> that are implicitly safe to be upcasted to anything.
 *
 */
public enum SCJSafeUpcastDefinition {
    DEFAULT(null, null),

    GET_NEXT_MISSION("javax.safetycritical.MissionSequencer",
            "getNextMission()"),

    MSEQ_GET_INSTANCE("javax.safetycritical.MissionSequencer",
            "getInstance()"),

    SAFELET_GET_SEQUENCER("javax.safetycritical.Safelet", "getSequencer()");

    private final String clazz;
    private final String signature;

    SCJSafeUpcastDefinition(String clazz, String signature) {
        this.clazz = clazz;
        this.signature = signature;
    }

    @Override
    public String toString() {
        return clazz + "." + signature;
    }

    public static SCJSafeUpcastDefinition fromMethod(ExecutableElement m,
            Elements elements, Types types) {
        String signature = Utils.buildSignatureString(m);
        TypeMirror t = Utils.getMethodClass(m).asType();

        for (SCJSafeUpcastDefinition sm : SCJSafeUpcastDefinition.values()) {
            if (sm.equals(DEFAULT))
                continue;
            TypeMirror s = Utils.getTypeMirror(elements, sm.clazz);
            if (signature.equals(sm.signature)) {
                if (types.isSubtype(t, s))
                    return sm;
            }
        }
        return DEFAULT;
    }
}