package com.backusnaurparser.main;

import org.junit.*;

import com.backusnaurparser.finitestatemachine.MachineState;
import com.backusnaurparser.helper.LanguageVerificationDevice;
import com.backusnaurparser.parser.Parser;

public class Main {
	public static void main(String[] argv) {
		// Parser parser = new Parser("Email",
		// "Email = StringPossibleNum [ \".\" StringPossibleNum ] \"@\"
		// StringPossibleNum { \".\" StringPossibleNum } \".\" Tld",
		// "Tld = Char Char",
		// "Char = \"a\" | \"b\" | \"c\" | \"d\" | \"e\" | \"f\" | \"g\" | \"h\"
		// | \"i\" | \"j\" | \"k\" | \"l\" | \"m\" | \"n\" | \"o\" | \"p\" |
		// \"q\" | \"r\" | \"s\" | \"t\" | \"u\" | \"v\" | \"w\" | \"x\" | \"y\"
		// | \"z\"",
		// "StringPossibleNum = Char { Char | Num }",
		// "Num = \"0\" \"1\" \"2\" \"3\" \"4\" \"5\" \"6\" \"7\" \"8\" \"9\"");

		Parser parser = new Parser("Test", "Test = \"a\" { \"a\" | \"0\" } [ \".\"  \"a\" { \"a\" | \"0\" } ] \"@\"");
		System.out.println("Parser(" + parser.getObjectCount() + "): " + parser);
		LanguageVerificationDevice dev = parser.getVerificationDevice();

		printTmp("", dev);
		printTmp("a", dev);
		printTmp("0", dev);
		printTmp("a0", dev);
		printTmp("aa0", dev);
		printTmp("aa0.", dev);
		printTmp("aa0.a", dev);
		printTmp("aa0@", dev);
		printTmp("a0@", dev);
		printTmp("a@", dev);
		printTmp("@", dev);
		printTmp("a@a@", dev);
		printTmp("a@a.0", dev);
		printTmp("a@a0.a000.aa", dev);
	}

	public static String[] printTmp(String input, LanguageVerificationDevice dev) {
		String[] returnValue = dev.getNextAllowedInputs(input);
		MachineState[] states = dev.getNextMachineStates(input);
		if (returnValue == null || states == null) {
			System.out.println("Error for input: \"" + input + "\"");
			return returnValue;
		}

		System.out.println("\"" + input + "\":");
		for (int i = 0; i < returnValue.length; i++) {
			System.out.println("\t" + returnValue[i] + " -> " + states[i]);
		}

		return returnValue;
	}

	@Test
	public void testLanguageVerificationDevice() {
		Parser parser = new Parser("Syntax", "Syntax = [ Start ] \"c\" | \"d\" \"e\" [ \"f\" ]",
				"Start = { \"a\" | \"b\" }");
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
}
