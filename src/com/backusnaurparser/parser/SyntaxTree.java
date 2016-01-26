package com.backusnaurparser.parser;

import java.util.*;

import com.backusnaurparser.helper.LanguageParseException;
import com.backusnaurparser.helper.SyntaxHelper;

public class SyntaxTree {
	private NonTerminal startObject;
	private Map<String, String> rules;

	public SyntaxTree(String startSymbol, String... rules) {
		this.startObject = null;
		this.rules = null;

		this.parseRules(startSymbol, rules);

		// Flatten our graph until it can not be flattened anymore to remove
		// uneccesary complexity. TODO: Optimize flatten method by flattening
		// first, then parsing. This can be done by replacing every subrule
		// within the master rule and then parsing only that
		int previousObjectCount;
		do {
			previousObjectCount = this.getNonTerminalCount();

			// Do the flatten
			this.flattenGraph();
		} while (previousObjectCount > this.getNonTerminalCount());
	}

	public NonTerminal getStartObject() {
		return this.startObject;
	}

	/**
	 * Creates a SyntaxTree from given rules
	 * 
	 * @param startRuleName
	 *            start symbol
	 * @param stringRules
	 *            collection of all rules in backus naur-syntax that define your
	 *            language
	 * 
	 * @return start object of this syntax tree
	 */
	private void parseRules(String startRuleName, String... stringRules) {
		if (stringRules.length <= 0)
			throw new LanguageParseException("No Rules were given");

		// Sort all rules
		this.getRules(stringRules);

		if (rules.isEmpty())
			throw new LanguageParseException("No Rules could be parsed");

		// For each rule, parse
		this.startObject = this.parseRule(new NonTerminal(startRuleName), this.rules.get(startRuleName));
	}

	/**
	 * Sorts through all rules and puts them into a more comprehensive format
	 * 
	 * @return
	 */
	private void getRules(String... stringRules) {
		this.rules = new HashMap<>();

		// Get individual rules
		for (String rule : stringRules) {
			// Syntax verification
			if (!rule.contains("=") || rule.split("=").length != 2)
				throw new LanguageParseException("Invalid Rule: " + rule + ".\nNumber of \"=\" does not match 1");

			String[] ruleComponents = rule.split("=");
			this.rules.put(ruleComponents[0].trim(), ruleComponents[1]);
		}
	}

	/**
	 * Recursive method that parses the rule for this nonTerminal, parsing
	 * subrules on it's journey
	 * 
	 * @param currentSObject
	 */
	private NonTerminal parseRule(NonTerminal currentSObject, String rule) {
		String[] ruleComponents = rule.trim().split(" ");

		for (int i = 0; i < ruleComponents.length; i++) {
			String ruleComponent = ruleComponents[i].trim();
			boolean additiveRelation = ruleComponents.length > i + 1
					? !SyntaxHelper.isOptionalOperator(ruleComponents[i + 1].trim()) : true;
			boolean repeating = false;
			boolean optional = false;

			if (SyntaxHelper.isTerminal(ruleComponent)) {
				// is our terminal a whitespace?
				if (ruleComponent.equals("\""))
					ruleComponent += " " + ruleComponents[i++];

				// Does our terminal contain whitespaces? Check by looking at
				// the end of our terminal
				while (!ruleComponent.endsWith("\"") && i + 1 < ruleComponents.length)
					ruleComponent += " " + ruleComponents[++i];

				// reset additiveRelation since we might have moved i
				additiveRelation = ruleComponents.length > i + 1
						? !SyntaxHelper.isOptionalOperator(ruleComponents[i + 1].trim()) : true;

				if (!ruleComponent.endsWith("\""))
					throw new LanguageParseException("String " + ruleComponent + "\" was never closed");

				NonTerminal packageTerminal = new NonTerminal("__TERMINAL__", additiveRelation, false, false);
				packageTerminal.setTerminal(ruleComponent.substring(1, ruleComponent.length() - 1));
				currentSObject.addSubobject(packageTerminal);
			} else if (SyntaxHelper.isOperator(ruleComponent)) {
				if (SyntaxHelper.isOpeningOperator(ruleComponent)) {
					// Find matching closing operator
					int closingIndex = SyntaxHelper.getMatchingClosingOperator(ruleComponents, i);

					// Which type of bracket?
					if (ruleComponent.equals("["))
						optional = true;
					if (ruleComponent.equals("{"))
						repeating = true;

					// Additive? Has to be relative to bracket, thus recalibrate
					additiveRelation = ruleComponents.length > closingIndex + 1
							? !SyntaxHelper.isOptionalOperator(ruleComponents[closingIndex + 1].trim()) : true;

					// Group elements in that bracket together into one element
					String bracketComponents = "";
					for (int j = i + 1; j < closingIndex; j++)
						bracketComponents += ruleComponents[j].trim() + " ";

					// Add to our current Object (Parse bracket recursively)
					currentSObject.addSubobject(
							this.parseRule(new NonTerminal("__BRACKET__", additiveRelation, repeating, optional),
									bracketComponents.trim()));

					// Set our i behind the bracket
					i = closingIndex;
				}

				// We will reach this line of code in case of a "|" operator. We
				// probably won't have to do anything here
			} else if (!ruleComponent.isEmpty() && !ruleComponent.startsWith(" ")) {
				// must be a non-terminal. Look @ rule in this.rules where this
				// nonterminal is defined
				String nonTerminalRule = this.rules.get(ruleComponent);
				if (nonTerminalRule == null)
					throw new LanguageParseException("Symbol (" + ruleComponent + ") not defined");

				nonTerminalRule = nonTerminalRule.trim();
				// Parse recursivly
				currentSObject.addSubobject(this
						.parseRule(new NonTerminal(ruleComponent, additiveRelation, false, false), nonTerminalRule));
			}
		}

		return currentSObject;
	}

	private void flattenGraph() {
		this.parseRules("__RULE__", "__RULE__ = " + this);
	}

	public int getNonTerminalCount() {
		return this.startObject.getNonTerminalCount();
	}

	@Override
	public String toString() {
		return this.startObject.toString();
	}
}
