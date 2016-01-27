package com.backusnaurparser.finitestatemachine;

import java.util.*;

/**
 * Each MachineState is an instance uniquely identifiable by its number. Only
 * one state with a given number is allowed to exist per FiniteStateMachine.
 * This class remembers all created MachineStates and only creates a new one if
 * it's number has not been used already
 * 
 * @author Dominik Horn
 *
 */
public class MachineStateProvider {

	/**
	 * All MachineStates that came into existence through this
	 * MachineStateProvider instance
	 */
	private List<MachineState> machineStates;

	public MachineStateProvider() {
		this.machineStates = new ArrayList<>();
	}

	/**
	 * Looks through all MachineStates this MachineStateProvider has stored and
	 * retrieves that one with the specified machineStateNumber
	 * 
	 * @param machineStateNumber
	 * @return either the existing MachineState with this number or a new
	 *         instance, should none with this number exist
	 */
	public MachineState getMachineState(int machineStateNumber) {
		MachineState returnState = null;
		for (MachineState state : machineStates)
			if (machineStateNumber == state.getStateNumber()) {
				returnState = state;
				break;
			}

		if (returnState == null) {
			returnState = new MachineState(machineStateNumber);
			machineStates.add(returnState);
		}

		return returnState;
	}
}
