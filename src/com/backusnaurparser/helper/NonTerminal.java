package com.backusnaurparser.helper;

import java.util.*;

public class NonTerminal extends SyntaxObject {
	/* All Subobjects in the order from left to right */
	private List<SyntaxObject> subObjects;

	private String name;

	public NonTerminal(String name) {
		this(name, true, false, false);
	}

	public NonTerminal(String name, boolean isAdditiveRelation, boolean isRepeating, boolean isOptional) {
		super(isAdditiveRelation, isRepeating, isOptional);

		this.name = name;
		this.subObjects = new ArrayList<>();
	}

	public void addSubobject(SyntaxObject object) {
		this.subObjects.add(object);
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
