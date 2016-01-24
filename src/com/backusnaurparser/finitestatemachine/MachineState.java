package com.backusnaurparser.finitestatemachine;

import java.util.*;

public class MachineState {
	private Map<String[], MachineState> outs;
	private boolean doesTerminate;

	public MachineState() {
		this(false);
	}

	public MachineState(boolean doesTerminate) {
		this.outs = new HashMap<>();
		this.doesTerminate = doesTerminate;
	}

	public void addOut(MachineState state, String... outStrings) {
		this.outs.put(outStrings, state);
	}
}
