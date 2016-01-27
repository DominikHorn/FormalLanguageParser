package com.backusnaurparser.parser;

import java.util.*;

import com.backusnaurparser.helper.LanguageParseException;

/**
 * SyntaxTree node that stores either subobjects or if it's a terminal node
 * (leaf) it stores a terminal String
 *
 * @author Dominik Horn
 * 
 */
public class NonTerminal {
	/** All Subobjects in the order from left to right */
	private List<NonTerminal> subObjects;

	/** Terminal String */
	private String terminal;

	/** Rule name */
	private String name;

	/**
	 * relation to neighbor Object in the syntax-tree on the same layer ( either
	 * " " -> linear relation [Logical AND], as in
	 * "I come first, then my neighbor" or "|", as in
	 * "Either me or my neighbor next" [Logical OR]
	 */
	private boolean isLinearRelation;

	/**
	 * Whether or not this SyntaxObject is to be repeated ( "{}" - Backus-Naur
	 * repeat )
	 */
	private boolean isRepeating;

	/**
	 * Whether or not this NonTerminal is optional ( "[]" - Backus-Naur optional
	 * )
	 */
	private boolean isOptional;

	/**
	 * Constructor for NonTerminal, which only possesses a name and defaults to
	 * having a linear, not-repeating and not-optional
	 * 
	 * @param name
	 *            see attribute "name"
	 */
	public NonTerminal(String name) {
		this(name, true, false, false);
	}

	/**
	 * Constructor for NonTerminal
	 * 
	 * @param name
	 *            see attribute "name"
	 * @param isLinearRelation
	 *            see attribute "isLinearRelation"
	 * @param isRepeating
	 *            see attribute "isRepeating"
	 * @param isOptional
	 *            see attribute "isOptional"
	 */
	public NonTerminal(String name, boolean isLinearRelation,
			boolean isRepeating, boolean isOptional) {
		this.isLinearRelation = isLinearRelation;
		this.isRepeating = isRepeating;
		this.isOptional = isOptional;
		this.name = name;
		this.terminal = "";
		this.subObjects = new ArrayList<>();
	}

	/** adds a subobject to this nonTerminal */
	public void addSubobject(NonTerminal object) {
		if (this.isTerminal())
			throw new LanguageParseException(
					"Can't have a nonTerminal be a terminal and have subobjects at the same time!");
		this.subObjects.add(object);
	}

	public List<NonTerminal> getSubobjects() {
		return this.subObjects;
	}

	/**
	 * Recursively counts how many NonTerminal Objects are beneath this object
	 * (including this object). If called on the syntaxTree root-Node this will
	 * give back how many NonTerminal objects there are in this tree in total
	 * 
	 * @return
	 */
	public int getNonTerminalCount() {
		int objCount = 1;
		for (NonTerminal object : this.subObjects)
			objCount += object.getNonTerminalCount();
		return objCount;
	}

	/**
	 * Don't!!!! Alter this method as it is currently needed to flatten the
	 * tree, so that finiteStateMachine can parse it!
	 */
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

	/**
	 * Accessors
	 */

	public String getName() {
		return this.name;
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
			throw new LanguageParseException(
					"Can't have a nonTerminal that has any operator other than \"isLinearRelation\" set and is a terminal!");
		this.terminal = terminal;
	}
}
