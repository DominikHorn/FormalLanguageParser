package com.backusnaurparser.helper;

import java.util.Map;

import com.backusnaurparser.finitestatemachine.MachineState;

public interface LanguageVerificationDevice {
	/**
	 * returns all input options that are allowed next
	 * 
	 * @param previousInput
	 * @return
	 */
	public String[] getNextAllowedInputs(String previousInput);

	/**
	 * TMP
	 * 
	 * @param previousInput
	 * @return
	 */
	public MachineState[] getNextMachineStates(String previousInput);
}
