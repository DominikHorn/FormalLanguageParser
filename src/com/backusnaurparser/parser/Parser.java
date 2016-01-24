package com.backusnaurparser.parser;

import com.backusnaurparser.finitestatemachine.FiniteStateMachine;
import com.backusnaurparser.helper.LanguageVerificationDevice;

public class Parser {
	private SyntaxTree syntaxTree;

	public Parser(String startSymbol, String... rules) {
		this.syntaxTree = new SyntaxTree(startSymbol, rules);
	}

	public LanguageVerificationDevice getVerificationDevice() {
		// Create a finiteState machine to verify language stuff	
		return new FiniteStateMachine(this.syntaxTree);
	}

	public int getObjectCount() {
		return this.syntaxTree.getNonTerminalCount();
	}

	@Override
	public String toString() {
		return this.syntaxTree.toString();
	}
}
