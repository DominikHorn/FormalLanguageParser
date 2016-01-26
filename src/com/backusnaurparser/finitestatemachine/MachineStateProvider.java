package com.backusnaurparser.finitestatemachine;

import java.util.*;

public class MachineStateProvider {

	private List<MachineState> machineStates;

	public MachineStateProvider() {
		this.machineStates = new ArrayList<>();
	}

	public MachineState getMachineState(int i) {
		MachineState returnState = null;
		for (MachineState state : machineStates)
			if (i == state.getStateNumber()) {
				returnState = state;
				break;
			}

		if (returnState == null) {
			returnState = new MachineState(i);
			machineStates.add(returnState);
		}

		return returnState;
	}
}
