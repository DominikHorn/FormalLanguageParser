package com.backusnaurparser.finitestatemachine;

import java.util.*;

import com.backusnaurparser.helper.LanguageVerificationDevice;
import com.backusnaurparser.parser.NonTerminal;
import com.backusnaurparser.parser.SyntaxTree;

public class FiniteStateMachine implements LanguageVerificationDevice {
	private MachineState startState;

	/**
	 * Creates finiteStateMachine from syntaxTree
	 * 
	 * @param syntaxTree
	 */
	public FiniteStateMachine(SyntaxTree syntaxTree) {
		this.startState = MachineState.getMachineState(0);
		List<MachineState> statesToClose = new ArrayList<>();
		statesToClose.add(this.startState);
		this.transformNonTerminal(statesToClose, syntaxTree.getStartObject(), syntaxTree.getStartObject().isRepeating(),
				syntaxTree.getStartObject().isOptional(), 0);
	}

	private List<MachineState> transformNonTerminal(List<MachineState> endPoints, NonTerminal startObject,
			boolean isRepeating, boolean isOptional, int recursionLevel) {
		int highestMachineStateNumber = 0;
		for (MachineState endPoint : endPoints) {
			if (endPoint.getStateNumber() > highestMachineStateNumber)
				highestMachineStateNumber = endPoint.getStateNumber();
		}
		MachineState newState = MachineState.getMachineState(highestMachineStateNumber + 1);

		for (NonTerminal nterminal : startObject.getSubobjects()) {
			if (nterminal.isTerminal()) {
				// Add connection to every open endpoint
				List<MachineState> endPointsToRemove = new ArrayList<>();

				// Update newState
				highestMachineStateNumber = 0;
				for (MachineState endPoint : endPoints) {
					if (endPoint.getStateNumber() > highestMachineStateNumber)
						highestMachineStateNumber = endPoint.getStateNumber();
				}
				newState = MachineState.getMachineState(highestMachineStateNumber + 1);
				
				for (MachineState currentState : endPoints) {						
					currentState.addOut(newState, nterminal.getTerminal());
					System.out.println("(" + recursionLevel + ") " + currentState + " -> " + newState + ": "
							+ nterminal.getTerminal());

					// Relation is linear & not optional -> remove Endpoint
					// from endpoints
					if (nterminal.isRelationLinear())
						endPointsToRemove.add(currentState);
				}

				if (isRepeating) {
					newState.addOut(newState, nterminal.getTerminal());
					System.out.println("(" + recursionLevel + ") " + newState + " -> " + newState + ": "
							+ nterminal.getTerminal());
				}

				for (MachineState state : endPointsToRemove)
					endPoints.remove(state);
			} else {
				// Non terminal, try to resolve it on a lower level
				System.out.println("Entering recursion " + nterminal.getName() + "(\"" + nterminal + "\")");
				List<MachineState> newEndPoints = this.transformNonTerminal(new ArrayList<>(endPoints), nterminal,
						nterminal.isRepeating() || isRepeating, nterminal.isOptional() || isOptional,
						recursionLevel + 1);
				System.out.println("Exiting recursion " + nterminal.getName() + "(\"" + nterminal + "\")" + " NewEndPoints: " + newEndPoints);
				for (MachineState endPoint : newEndPoints)
					if (!endPoints.contains(endPoint)) {
						endPoints.add(endPoint);
						System.out.println("(" + recursionLevel + ") Adding endpoint " + endPoint);
					}
				
				continue;
			}
			if (!isRepeating && newState != null) {
				if (!endPoints.contains(newState))
					endPoints.add(newState);
				if (nterminal.isRelationLinear())
					newState = MachineState.getMachineState(newState.getStateNumber() + 1);
			}
		}
		if (isRepeating && newState != null)
			if (!endPoints.contains(newState))
				endPoints.add(newState);

		return endPoints;
	}

	@Override
	public String[] getNextAllowedInputs(String previousInput) {
		MachineState currentState = this.startState;

		while (currentState != null) {
			Map<String[], MachineState> outs = currentState.getOuts();
			String matchingOut = "";
			for (String[] out : outs.keySet()) {
				for (String string : out) {
					if (previousInput.startsWith(string)) {
						// Found match
						matchingOut = string;
						currentState = outs.get(out);
						break;
					}
				}
				if (!matchingOut.isEmpty())
					break;
			}

			if (matchingOut.isEmpty())
				return null;

			previousInput = previousInput.substring(matchingOut.length());

			if (previousInput.isEmpty()) {
				// Stuck at this state, return all possible outs
				List<String> possibleOuts = new ArrayList<>();
				Map<String[], MachineState> currentOuts = currentState.getOuts();
				for (String[] out : currentOuts.keySet())
					for (String string : out)
						possibleOuts.add(string);

				String[] returnValues = new String[possibleOuts.size()];
				for (int i = 0; i < possibleOuts.size(); i++)
					returnValues[i] = possibleOuts.get(i);

				return returnValues;
			}
		}

		return null;
	}

	public MachineState[] getNextMachineStates(String previousInput) {
		MachineState currentState = this.startState;

		while (currentState != null) {
			Map<String[], MachineState> outs = currentState.getOuts();

			String matchingOut = "";
			for (String[] out : outs.keySet()) {
				for (String string : out) {
					if (previousInput.startsWith(string)) {
						matchingOut = string;
						currentState = outs.get(out);
						break;
					}
				}
				if (!matchingOut.isEmpty())
					break;
			}
			if (matchingOut.isEmpty())
				return null;

			previousInput = previousInput.substring(matchingOut.length());

			if (previousInput.isEmpty()) {
				// Stuck at this state, return all possible outs (machineState
				List<MachineState> possibleOuts = new ArrayList<>();
				Map<String[], MachineState> currentOuts = currentState.getOuts();
				for (String[] out : currentOuts.keySet())
					possibleOuts.add(currentOuts.get(out));

				MachineState[] returnValues = new MachineState[possibleOuts.size()];
				for (int i = 0; i < possibleOuts.size(); i++)
					returnValues[i] = possibleOuts.get(i);

				return returnValues;
			}
		}

		return null;
	}
}
