package com.backusnaurparser.main;

import org.junit.*;

import com.backusnaurparser.finitestatemachine.MachineState;
import com.backusnaurparser.helper.LanguageVerificationDevice;
import com.backusnaurparser.parser.Parser;

public class Main {
	public static void main(String[] argv) {
		System.out.println("USE JUNIT GOD DAMNIT!!!");
	}

	public static String[] printTmp(String input, LanguageVerificationDevice dev) {
		String[] returnValue = dev.getNextAllowedInputs(input);
		MachineState[] states = dev.getNextMachineStates(input);
		if (returnValue == null || states == null) {
			// System.out.println("Error for input: \"" + input + "\"");
			return returnValue;
		}

		// System.out.println("\"" + input + "\":");
		// for (int i = 0; i < returnValue.length; i++) {
		// System.out.println("\t" + returnValue[i] + " -> " + states[i]);
		// }

		return returnValue;
	}

	@Test
	public void test0LangugageVerificationDevice() {
		Parser parser = new Parser("Test", "Test = Subdomain [ \".\"  Subdomain ] \"@\"",
				"Subdomain = Char { Char | Num }", "Char = \"a\"", "Num = \"0\"");
		System.out.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(printTmp("", dev) == null);
		Assert.assertTrue(printTmp("a", dev) != null);
		Assert.assertTrue(printTmp("0", dev) == null);
		Assert.assertTrue(printTmp("a0", dev) != null);
		Assert.assertTrue(printTmp("aa0", dev) != null);
		Assert.assertTrue(printTmp("aa0.", dev) != null);
		Assert.assertTrue(printTmp("aa0.a", dev) != null);
		Assert.assertTrue(printTmp("aa0.a0", dev) != null);
		Assert.assertTrue(printTmp("aa0.a0a", dev) != null);
		Assert.assertTrue(printTmp("aa0@", dev) != null);
		Assert.assertTrue(printTmp("a0@", dev) != null);
		Assert.assertTrue(printTmp("a@", dev) != null);
		Assert.assertTrue(printTmp("@", dev) == null);
		Assert.assertTrue(printTmp("a@a@", dev) == null);
		Assert.assertTrue(printTmp("a@a.0", dev) == null);
		Assert.assertTrue(printTmp("a@a0.a000.aa", dev) == null);
	}

	@Test
	public void test1LanguageVerificationDevice() {
		Parser parser = new Parser("Syntax", "Syntax = [ { \"a\" | \"b\" } ] \"c\" | \"d\" \"e\" [ \"f\" ]");
		System.out.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(printTmp("abaaaabababababab", dev) != null);
		Assert.assertTrue(printTmp("babababababab", dev) != null);
		Assert.assertTrue(printTmp("c", dev) != null);
		Assert.assertTrue(printTmp("d", dev) != null);
		Assert.assertTrue(printTmp("e", dev) == null);
		Assert.assertTrue(printTmp("abcde", dev) == null);
		Assert.assertTrue(printTmp("abde", dev) != null);
		Assert.assertTrue(printTmp("abce", dev) != null);
		Assert.assertTrue(printTmp("abcef", dev) != null);
		Assert.assertTrue(printTmp("abdef", dev) != null);
		Assert.assertTrue(printTmp("abdeff", dev) == null);
		Assert.assertTrue(printTmp("abceff", dev) == null);
		Assert.assertTrue(printTmp("abbbbbbbbbbbbbbbbbbbaaaaaaaaaaababababababace", dev) != null);
		Assert.assertTrue(printTmp("abbbbbbbbbbbbbbbbbbbaaaaaaaaaaababababababade", dev) != null);
	}

	@Test
	public void test2LanguageVerificationDevice() {
		Parser parser = new Parser("Double", "Double = Num [ \".\" Num ] [ Calc ]",
				"Num = { \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" }",
				"Calc = { \" \" } CalcOperator { \" \" } Num [ \".\" Num ]",
				"CalcOperator = \"+\" | \"-\" | \"*\" | \"/\"");
		System.out.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		Assert.assertTrue(printTmp("", dev) == null);
		Assert.assertTrue(printTmp("12312..213123", dev) == null);
		Assert.assertTrue(printTmp("000000", dev) != null);
		Assert.assertTrue(printTmp("01010101001", dev) != null);
		Assert.assertTrue(printTmp("12312314124", dev) != null);
		Assert.assertTrue(printTmp("12312.564378", dev) != null);
		Assert.assertTrue(printTmp("3*3", dev) != null);
		Assert.assertTrue(printTmp("2+2", dev) != null);
		Assert.assertTrue(printTmp("1-5", dev) != null);
		Assert.assertTrue(printTmp("10*3", dev) != null);
		Assert.assertTrue(printTmp("123.321+", dev) != null);
	}
}
