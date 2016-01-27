package com.backusnaurparser.main;

import org.junit.*;

import com.backusnaurparser.helper.LanguageVerificationDevice;
import com.backusnaurparser.parser.Parser;

/**
 * Main class that was used for JUnit testing. Left in as it provides examples
 * how to use this codebase
 *
 * @author Dominik Horn
 */
public class Main {
	@Test
	public void test0LangugageVerificationDevice() {
		Parser parser = new Parser("Test",
				"Test = Subdomain [ \".\"  Subdomain ] \"@\"",
				"Subdomain = Char { Char | Num }", "Char = \"a\"",
				"Num = \"0\"");
		System.out
				.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(dev.getNextAllowedInputs("") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("a") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("0") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("a0") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0.") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0.a") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0.a0") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0.a0a") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("aa0@") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("a0@") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("a@") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("@") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("a@a@") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("a@a.0") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("a@a0.a000.aa") == null);
	}

	@Test
	public void test1LanguageVerificationDevice() {
		Parser parser = new Parser("Syntax",
				"Syntax = [ { \"a\" | \"b\" } ] \"c\" | \"d\" \"e\" [ \"f\" ]");
		System.out
				.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(dev.getNextAllowedInputs("abaaaabababababab") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("babababababab") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("c") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("d") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("e") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("abcde") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("abde") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("abce") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("abcef") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("abdef") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("abdeff") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("abceff") == null);
		Assert.assertTrue(dev
				.getNextAllowedInputs("abbbbbbbbbbbbbbbbbbbaaaaaaaaaaababababababace") != null);
		Assert.assertTrue(dev
				.getNextAllowedInputs("abbbbbbbbbbbbbbbbbbbaaaaaaaaaaababababababade") != null);
	}

	@Test
	public void test2LanguageVerificationDevice() {
		Parser parser = new Parser(
				"Double",
				"Double = Num [ \".\" Num ] [ Calc ]",
				"Num = { \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" }",
				"Calc = { \" \" } CalcOperator { \" \" } Num [ \".\" Num ]",
				"CalcOperator = \"+\" | \"-\" | \"*\" | \"/\"");
		System.out
				.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(dev.getNextAllowedInputs("") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("12312..213123") == null);
		Assert.assertTrue(dev.getNextAllowedInputs("000000") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("01010101001") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("12312314124") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("12312.564378") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("3*3") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("2+2") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("1-5") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("10*3") != null);
		Assert.assertTrue(dev.getNextAllowedInputs("123.321+") != null);
	}
}
