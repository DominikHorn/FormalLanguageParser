package com.backusnaurparser.parser;

import com.backusnaurparser.helper.LanguageVerificationDevice;

public class Parser {
	private SyntaxTree syntaxTree;

	public Parser(String startSymbol, String... rules) {
		this.syntaxTree = new SyntaxTree(startSymbol, rules);
	}

	public LanguageVerificationDevice getVerificationDevice() {
		return null;
	}

	public int getObjectCount() {
		return this.syntaxTree.getObjectCount();
	}

	@Override
	public String toString() {
		return this.syntaxTree.toString();
	}
}
