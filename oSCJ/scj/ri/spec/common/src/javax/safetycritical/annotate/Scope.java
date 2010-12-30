package javax.safetycritical.annotate;

public @interface Scope {
	public static final String UNKNOWN = "UNKNOWN";
	public static final String CURRENT = "CURRENT";
	public static final String IMMORTAL = "IMMORTAL";

	public String value();
}
