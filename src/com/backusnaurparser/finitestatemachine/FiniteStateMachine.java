package com.backusnaurparser.finitestatemachine;

import java.util.*;

import com.backusnaurparser.helper.LanguageVerificationDevice;
import com.backusnaurparser.parser.NonTerminal;
import com.backusnaurparser.parser.SyntaxTree;

/**
 * Working implementation of LanguageVerificationDevice using finite state
 * machines
 * 
 * @author Dominik Horn
 *
 */
public class FiniteStateMachine implements LanguageVerificationDevice {
	private MachineState startState;
	private MachineStateProvider machProvider;

	/**
	 * Creates finiteStateMachine from syntaxTree
	 * 
	 * @param syntaxTree
	 */
	public FiniteStateMachine(SyntaxTree syntaxTree) {
		this.machProvider = new MachineStateProvider();
		this.startState = this.machProvider.getMachineState(0);
		List<MachineState> statesToClose = new ArrayList<>();
		statesToClose.add(this.startState);
		this.transformNonTerminal(statesToClose, syntaxTree.getStartObject(),
				syntaxTree.getStartObject().isRepeating(), syntaxTree
						.getStartObject().isOptional());
	}

	/**
	 * method that transforms a NonTerminal (holding EBNF/Backus-Naur rules)
	 * recursively into a finiteStateMachine
	 * 
	 * @param statesToBeClosed
	 *            current states of the finiteStateMachine that have to be
	 *            closed next
	 * @param currentObject
	 *            NonTerminal object that we're parsing atm
	 * @param isRepeating
	 *            is this a repeating structure?
	 * @param isOptional
	 *            is this an optional structure?
	 * @return
	 */
	private List<MachineState> transformNonTerminal(
			List<MachineState> statesToBeClosed, NonTerminal currentObject,
			boolean isRepeating, boolean isOptional) {
		int highestMachineStateNumber = 0;
		for (MachineState endPoint : statesToBeClosed) {
			if (endPoint.getStateNumber() > highestMachineStateNumber)
				highestMachineStateNumber = endPoint.getStateNumber();
		}
		MachineState newState = this.machProvider
				.getMachineState(highestMachineStateNumber + 1);

		for (NonTerminal nterminal : currentObject.getSubobjects()) {
			if (nterminal.isTerminal()) {
				// Add connection to every open endpoint
				List<MachineState> endPointsToRemove = new ArrayList<>();

				// Update newState
				highestMachineStateNumber = 0;
				for (MachineState endPoint : statesToBeClosed) {
					if (endPoint.getStateNumber() > highestMachineStateNumber)
						highestMachineStateNumber = endPoint.getStateNumber();
				}
				newState = this.machProvider
						.getMachineState(highestMachineStateNumber + 1);

				for (MachineState currentState : statesToBeClosed) {
					currentState.addOut(newState, nterminal.getTerminal());

					// Relation is linear & not optional -> remove Endpoint
					// from endpoints
					if (nterminal.isRelationLinear())
						endPointsToRemove.add(currentState);
				}

				if (isRepeating) {
					newState.addOut(newState, nterminal.getTerminal());
				}

				for (MachineState state : endPointsToRemove)
					statesToBeClosed.remove(state);
			} else {
				// Non terminal, try to resolve it on a lower level
				List<MachineState> newEndPoints = this.transformNonTerminal(
						new ArrayList<>(statesToBeClosed), nterminal,
						nterminal.isRepeating() || isRepeating,
						nterminal.isOptional() || isOptional);
				for (MachineState endPoint : newEndPoints)
					if (!statesToBeClosed.contains(endPoint)) {
						statesToBeClosed.add(endPoint);
					}

				continue;
			}
			if (!isRepeating && newState != null) {
				if (nterminal.isRelationLinear())
					if (!statesToBeClosed.contains(newState))
						statesToBeClosed.add(newState);
				newState = this.machProvider.getMachineState(newState
						.getStateNumber() + 1);

			}
		}
		if (isRepeating && newState != null)
			if (!statesToBeClosed.contains(newState))
				statesToBeClosed.add(newState);

		return statesToBeClosed;
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

			// If we found no matching out from our current state the input must
			// be invalid
			if (matchingOut.isEmpty())
				return null;

			previousInput = previousInput.substring(matchingOut.length());

			if (previousInput.isEmpty()) {
				// Stuck at this state, return all possible outs
				List<String> possibleOuts = new ArrayList<>();
				Map<String[], MachineState> currentOuts = currentState
						.getOuts();
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
}
