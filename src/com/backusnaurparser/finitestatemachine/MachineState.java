package com.backusnaurparser.finitestatemachine;

import java.util.*;

public class MachineState {
	private Map<String[], MachineState> outs;
	private int stateNumber;

	private MachineState(int stateNumber) {
		this.outs = new HashMap<>();
		this.stateNumber = stateNumber;
	}

	public void addOut(MachineState state, String... outStrings) {
		this.outs.put(outStrings, state);
	}

	public void clearOuts() {
		this.outs.clear();
	}

	public Map<String[], MachineState> getOuts() {
		return this.outs;
	}

	public int getStateNumber() {
		return this.stateNumber;
	}

	@Override
	public String toString() {
		return "Z" + this.stateNumber;
	}

	private static List<MachineState> machineStates;

	public static MachineState getMachineState(int i) {
		if (machineStates == null)
			machineStates = new ArrayList<>();

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
