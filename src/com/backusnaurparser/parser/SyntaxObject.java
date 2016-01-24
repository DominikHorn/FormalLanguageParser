package com.backusnaurparser.parser;

public abstract class SyntaxObject {
	/* private to neighbor Object on the syntax-tree */
	protected boolean isAdditiveRelation;

	/* Whether or not this SyntaxObject is to be repeated */
	private boolean isRepeating;

	/* Whether or not this SyntaxObject is optional */
	private boolean isOptional;

	public SyntaxObject() {
		this(true, false, false);
	}

	public SyntaxObject(boolean isAdditiveRelation, boolean isRepeating, boolean isOptional) {
		this.isAdditiveRelation = isAdditiveRelation;
		this.isRepeating = isRepeating;
		this.isOptional = isOptional;
	}

	public boolean isRelationAdditive() {
		return this.isAdditiveRelation;
	}

	public boolean isRepeating() {
		return this.isRepeating;
	}

	public boolean isOptional() {
		return this.isOptional;
	}

	public abstract int getObjectCount();
}
