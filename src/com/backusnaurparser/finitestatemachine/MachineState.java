package com.backusnaurparser.finitestatemachine;

import java.util.*;

/**
 * Object's of this class each represent one state of a finite state machine
 * 
 * @author Dominik Horn
 *
 */
public class MachineState {
	/**
	 * Outs are outgoing connections from this state to another state. Each
	 * connection can have several different out-Strings
	 */
	private Map<String[], MachineState> outs;

	/**
	 * Number that identifies this state. Each state may only exist once. This
	 * attribute prevents multiple states with the same number from being
	 * created (Look at MachineStateProvider class for information on how states
	 * are created and why this attribute affects MachineState in said way)
	 */
	private int stateNumber;

	/**
	 * Creates a new MachineState. This Method may only be invoked by
	 * MachineStateProvider
	 * 
	 * @param stateNumber
	 */
	protected MachineState(int stateNumber) {
		this.outs = new HashMap<>();
		this.stateNumber = stateNumber;
	}

	/**
	 * Adds an out. See "outs" attribute definition for further information
	 * 
	 * @param state
	 *            State that these out-Strings transition to
	 * @param outStrings
	 *            out-Strings that connect to state
	 */
	public void addOut(MachineState state, String... outStrings) {
		this.outs.put(outStrings, state);
	}

	/** removes all previously stored outs */
	public void clearOuts() {
		this.outs.clear();
	}

	/** returns all outs that this machineState has */
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
