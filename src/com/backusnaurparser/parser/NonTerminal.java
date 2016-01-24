package com.backusnaurparser.parser;

import java.util.*;

import com.backusnaurparser.helper.LanguageParseException;

public class NonTerminal {
	/* All Subobjects in the order from left to right */
	private List<NonTerminal> subObjects;

	/* TerminalString */
	private String terminal;

	/* Rule name */
	private String name;

	/* private to neighbor Object on the syntax-tree */
	private boolean isLinearRelation;

	/* Whether or not this SyntaxObject is to be repeated */
	private boolean isRepeating;

	/* Whether or not this SyntaxObject is optional */
	private boolean isOptional;

	public NonTerminal(String name) {
		this(name, true, false, false);
	}

	public NonTerminal(String name, boolean isLinearRelation, boolean isRepeating, boolean isOptional) {
		this.isLinearRelation = isLinearRelation;
		this.isRepeating = isRepeating;
		this.isOptional = isOptional;
		this.name = name;
		this.terminal = "";
		this.subObjects = new ArrayList<>();
	}

	public boolean isRelationLinear() {
		return this.isLinearRelation;
	}

	public boolean isRepeating() {
		return this.isRepeating;
	}

	public boolean isOptional() {
		return this.isOptional;
	}

	public boolean isTerminal() {
		return !this.terminal.isEmpty();
	}

	public String getTerminal() {
		return this.terminal;
	}

	public void setTerminal(String terminal) {
		if (this.isOptional || this.isRepeating)
			throw new LanguageParseException("Can't have a nonTerminal that has operators and a terminal!");
		this.terminal = terminal;
	}

	public void addSubobject(NonTerminal object) {
		this.subObjects.add(object);
	}

	public List<NonTerminal> getSubobjects() {
		return this.subObjects;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		String output = this.terminal.isEmpty() ? "" : "\"" + terminal + "\" ";
		for (NonTerminal nonTerminal : this.subObjects)
			output += nonTerminal.toString();

		if (this.isRepeating())
			output = "{ " + output + "} ";
		if (this.isOptional())
			output = "[ " + output + "] ";
		if (!this.isRelationLinear())
			output = output + "| ";

		return output;
	}

	public int getNonTerminalCount() {
		int objCount = 1;
		for (NonTerminal object : this.subObjects)
			objCount += object.getNonTerminalCount();
		return objCount;
	}
}
