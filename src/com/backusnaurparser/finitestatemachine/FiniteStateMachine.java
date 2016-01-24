package com.backusnaurparser.finitestatemachine;

import com.backusnaurparser.helper.LanguageVerificationDevice;

public class FiniteStateMachine implements LanguageVerificationDevice {
	private MachineState startState;

	public FiniteStateMachine(MachineState startState) {
		this.startState = startState;
	}

}
