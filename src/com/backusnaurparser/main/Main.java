package com.backusnaurparser.main;

import com.backusnaurparser.parser.Parser;

public class Main {
	public static void main(String[] argv) {
//		Parser parser = new Parser("Email",
//				"Email = MString [ \".\" MString ] \"@\" Subdomain { \".\" Subdomain } TopLevelDomain",
//				"MString = StringPossibleNum { StringPossibleNum }", 
//				"Subdomain = StringPossibleNum",
//				"TopLevelDomain = Char Char",
//				"Char = \"a\" | \"b\" | \"c\" | \"d\" | \"e\" | \"f\" | \"g\" | \"h\" | \"i\" | \"j\" | \"k\" | \"l\" | \"m\" | \"n\" | \"o\" | \"p\" | \"q\" | \"r\" | \"s\" | \"t\" | \"u\" | \"v\" | \"w\" | \"x\" | \"y\" | \"z\" | \"A\" | \"B\" | \"C\" | \"D\" | \"E\" | \"F\" | \"G\" | \"H\" | \"I\" | \"J\" | \"K\" | \"L\" | \"M\" | \"N\" | \"O\" | \"P\" | \"Q\" | \"R\" | \"S\" | \"T\" | \"U\" | \"V\" | \"W\" | \"X\" | \"Y\" | \"Z\" | ",
//				"StringPossibleNum = Char { Char | Num }",
//				"Num = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"");
		Parser parser = new Parser("Syntax", "Syntax = [ Test ] | \"Test2\"", "Test = \"Hallo Welt\" | \"Hello World\"");

	}
}
