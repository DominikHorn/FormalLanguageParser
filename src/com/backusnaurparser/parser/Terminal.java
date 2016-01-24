package com.backusnaurparser.parser;

public class Terminal extends SyntaxObject {
	private String terminal;

	public Terminal(String terminal) {
		this(terminal, true, false, false);
	}

	public Terminal(String terminal, boolean isAdditiveRelation, boolean isRepeating, boolean isOptional) {
		super(isAdditiveRelation, isRepeating, isOptional);

		this.terminal = terminal;
	}

	@Override
	public String toString() {
		String output = "\"" + this.terminal + "\"";
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
		return 1;
	}
}
