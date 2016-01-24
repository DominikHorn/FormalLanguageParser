package com.backusnaurparser.parser;

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
		String output = this.subObjects.size() > 0 ? this.subObjects.get(0).toString() : "-";
		for (int i = 1; i < this.subObjects.size(); i++) {
			output += " " + this.subObjects.get(i);
		}

		output = output.trim();
		if (this.isOptional())
			output = " [ " + output + " ]";
		if (this.isRepeating())
			output = " { " + output + " }";
		if (!this.isRelationAdditive())
			output = output + " |";

		return output;
	}

	@Override
	public int getObjectCount() {
		int objCount = 1;
		for (SyntaxObject object : this.subObjects)
			objCount += object.getObjectCount();
		return objCount;
	}
}
