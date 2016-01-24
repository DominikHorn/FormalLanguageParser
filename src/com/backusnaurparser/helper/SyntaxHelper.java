package com.backusnaurparser.helper;

public class SyntaxHelper {
	public static boolean isTerminal(String ruleComponent) {
		return ruleComponent.startsWith("\"");
	}

	public static boolean isOptionalOperator(String ruleComponent) {
		return ruleComponent.equals("|");
	}

	public static boolean isOperator(String ruleComponent) {
		return ruleComponent.equals("|") || ruleComponent.equals("(") || ruleComponent.equals(")")
				|| ruleComponent.equals("[") || ruleComponent.equals("]") || ruleComponent.equals("{")
				|| ruleComponent.equals("}");
	}

	public static boolean isOpeningOperator(String ruleComponent) {
		return ruleComponent.equals("(") || ruleComponent.equals("[") || ruleComponent.equals("{");
	}

	public static int getMatchingClosingOperator(String[] ruleComponents, int startIndex) {
		int bracketCount = 1;
		String openingOperator = ruleComponents[startIndex].trim();
		String closingOperator = getClosingOperator(openingOperator);

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

	public static String getClosingOperator(String openingOperator) {
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
}
