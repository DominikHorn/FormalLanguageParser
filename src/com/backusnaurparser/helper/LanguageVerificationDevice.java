package com.backusnaurparser.helper;

/**
 * LanguageVerificationDevice is the interface used to provide functionality for
 * receiving output out of this codebase
 * 
 * @author Dominik Horn
 *
 */
public interface LanguageVerificationDevice {
	/**
	 * returns all input options that are allowed next
	 * 
	 * @param inputToVerify
	 *            Everything that was entered by the user
	 * @return String[] array of every allowed next input. If this array is null
	 *         the input was illegal. If this array is empty "input" is complete
	 *         and no further input is necessary/allowed
	 */
	public String[] getNextAllowedInputs(String inputToVerify);
}
