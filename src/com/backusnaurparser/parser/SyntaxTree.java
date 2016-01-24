package com.backusnaurparser.parser;

import java.util.*;

import com.backusnaurparser.helper.LanguageParseException;

public class SyntaxTree {
	private SyntaxObject start;
	private Map<String, String> rules;

	public SyntaxTree(String startSymbol, String... rules) {
		this.start = null;
		this.rules = null;

		this.parseRules(startSymbol, rules);
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
		this.start = this.parseRule(new NonTerminal(startRuleName), this.rules.get(startRuleName));
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
	private SyntaxObject parseRule(NonTerminal currentSObject, String rule) {
		String ruleName = currentSObject.getName();

		String[] ruleComponents = rule.trim().split(" ");
		for (int i = 0; i < ruleComponents.length; i++) {
			String ruleComponent = ruleComponents[i].trim();
			boolean additiveRelation = ruleComponents.length > i + 1
					? !this.isOptionalOperator(ruleComponents[i + 1].trim()) : true;
			boolean repeating = false;
			boolean optional = false;

			if (this.isTerminal(ruleComponent)) {
				// Does our terminal contain whitespaces? Check by looking at
				// the end of our terminal
				while (!ruleComponent.endsWith("\"") && i + 1 < ruleComponents.length)
					ruleComponent += " " + ruleComponents[++i];

				// reset additiveRelation since we might have moved i
				additiveRelation = ruleComponents.length > i + 1 ? !this.isOptionalOperator(ruleComponents[i + 1].trim())
						: true;

				if (!ruleComponent.endsWith("\""))
					throw new LanguageParseException("String " + ruleComponent + "\" was never closed");

				currentSObject.addSubobject(new Terminal(ruleComponent.substring(1, ruleComponent.length() - 1),
						additiveRelation, false, false));
			} else if (this.isOperator(ruleComponent)) {
				if (this.isOpeningOperator(ruleComponent)) {
					// Find matching closing operator
					int closingIndex = getMatchingClosingOperator(ruleComponents, i);

					// Which type of bracket?
					if (ruleComponent.equals("["))
						optional = true;
					else if (ruleComponent.equals("{"))
						repeating = true;

					// Additive? Has to be relative to bracket, thus recalibrate
					additiveRelation = ruleComponents.length > closingIndex + 1
							? !this.isOptionalOperator(ruleComponents[closingIndex + 1].trim()) : true;

					// Group elements in that bracket together into one element
					String bracketComponents = " ";
					for (int j = i + 1; j < closingIndex; j++)
						bracketComponents += ruleComponents[j] + " ";

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

				// Parse recursivly
				currentSObject.addSubobject(this.parseRule(
						new NonTerminal(ruleComponent, additiveRelation, false, false), nonTerminalRule.trim()));
			}
		}

		return currentSObject;
	}

	private boolean isTerminal(String ruleComponent) {
		return ruleComponent.startsWith("\"");
	}

	private boolean isOptionalOperator(String ruleComponent) {
		return ruleComponent.equals("|");
	}

	private boolean isOperator(String ruleComponent) {
		return ruleComponent.equals("|") || ruleComponent.equals("(") || ruleComponent.equals(")")
				|| ruleComponent.equals("[") || ruleComponent.equals("]") || ruleComponent.equals("{")
				|| ruleComponent.equals("}");
	}

	private boolean isOpeningOperator(String ruleComponent) {
		return ruleComponent.equals("(") || ruleComponent.equals("[") || ruleComponent.equals("{");
	}

	private int getMatchingClosingOperator(String[] ruleComponents, int startIndex) {
		int bracketCount = 1;
		String openingOperator = ruleComponents[startIndex].trim();
		String closingOperator = this.getClosingOperator(openingOperator);

		for (int i = startIndex + 1; i < ruleComponents.length; i++) {
			if (ruleComponents[i].trim().equals(closingOperator))
				bracketCount--;
			else if (ruleComponents[i].trim().equals(openingOperator))
				bracketCount++;

			if (bracketCount == 0)
				return i;
		}

		return -1;
	}

	private String getClosingOperator(String openingOperator) {
		switch (openingOperator) {
		case "(":
			return ")";
		case "[":
			return "]";
		case "{":
			return "}";
		default:
			return "";

		}
	}
	
	public int getObjectCount() {
		return this.start.getObjectCount();
	}

	@Override
	public String toString() {
		return this.start.toString();
	}
}
