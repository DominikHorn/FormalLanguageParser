package com.backusnaurparser.finitestatemachine;

import java.util.*;

public class MachineState {
	private Map<String[], MachineState> outs;
	private int stateNumber;

	protected MachineState(int stateNumber) {
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
}
