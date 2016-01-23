package com.backusnaurparser.parser;

import com.backusnaurparser.helper.SyntaxTree;

public class Parser {
	private SyntaxTree syntaxTree;
	
	public Parser(String startSymbol, String... rules) {
		this.syntaxTree = new SyntaxTree(startSymbol, rules);
	}

}
