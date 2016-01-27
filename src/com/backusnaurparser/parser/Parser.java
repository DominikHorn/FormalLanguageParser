package com.backusnaurparser.parser;

import com.backusnaurparser.finitestatemachine.FiniteStateMachine;
import com.backusnaurparser.helper.LanguageVerificationDevice;

/**
 * Interface for this codebase (takes in EBNF/Backus Naur grammar and spits out
 * a finiteStateMachine that can verify this grammar)
 * 
 * @author Dominik Horn
 *
 */
public class Parser {
	/**
	 * SyntaxTree that basically stores the rules in a tree-data structure (to
	 * make it more computer-friendly)
	 */
	private SyntaxTree syntaxTree;

	/**
	 * Creates a new Parser for a formal language defined by parameters as
	 * follows:
	 * 
	 * @param startSymbol
	 *            Start symbol of your language. (Name of the first rule)
	 * @param rules
	 *            Array of Rules that each specify a Nonterminal as follows
	 *            "<RuleName> = <RuleComponent> <Operator>... <RuleComponent>..."
	 *            (See examples in Main Class). These Rules have to be in no
	 *            particular order as the parser will reorganize/sort them
	 *            anyways
	 */
	public Parser(String startSymbol, String... rules) {
		this.syntaxTree = new SyntaxTree(startSymbol, rules);
	}

	/**
	 * Returns a working implementation of LanguageVerificationDevice that can
	 * verify this Parser's assigned language
	 * 
	 * @return new LangugageVerificationDevice implementation
	 */
	public LanguageVerificationDevice getVerificationDevice() {
		// Create a finiteState machine to verify language stuff
		return new FiniteStateMachine(this.syntaxTree);
	}

	/**
	 * Get amount of NonTerminal objects (Tree nodes) in this Parser's syntax
	 * tree
	 * 
	 * @return
	 */
	public int getObjectCount() {
		return this.syntaxTree.getNonTerminalCount();
	}

	@Override
	public String toString() {
		return this.syntaxTree.toString();
	}
}
