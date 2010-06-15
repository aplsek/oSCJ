package javax.safetycritical.annotate;

@SCJAllowed
public enum Restrict
{
	@SCJAllowed
	MAY_ALLOCATE { @Override public int value() { return 0; } },

	@SCJAllowed
	MAY_BLOCK { @Override public int value() { return 1; } },

	@SCJAllowed
	BLOCK_FREE { @Override public int value() { return 2; } },

	@SCJAllowed
	ALLOCATE_FREE { @Override public int value() { return 3; } },

	@SCJAllowed
	INITIALIZATION { @Override public int value() { return 4; } },

	@SCJAllowed
	CLEANUP { @Override public int value() { return 5; } },
	
	@SCJAllowed
    EXECUTION { @Override public int value() { return 6; } },

	@SCJAllowed
	ANY_TIME { @Override public int value() { return 7; } };

	public int value() {
		return 0;
	}

}
