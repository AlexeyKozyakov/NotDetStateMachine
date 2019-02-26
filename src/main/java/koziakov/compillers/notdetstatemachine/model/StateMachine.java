package koziakov.compillers.notdetstatemachine.model;

import koziakov.compillers.notdetstatemachine.exceptions.FileParseException;

import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class StateMachine {

    private State initialState;
    private Set<State> currentStates = new HashSet<>();

    private Map<Integer, State> states = new HashMap<>();

    private int lineNum;

    public void load(Reader reader) throws FileParseException {
        states.clear();
        lineNum = 0;
        try(Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                loadLine(line);
            }
        }
    }

    public void parse(Reader reader) {
        currentStates.add(initialState);
        try(Scanner scanner = new Scanner(reader)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                parseLine(line);
            }
        }
        boolean reached = false;
        for (State state : currentStates) {
            if (state.isTerminal()) {
                reached = true;
                break;
            }
        }
        if (reached) {
            System.out.println("Valid text");
        } else {
            System.out.println("Invalid text");
        }
    }

    private void parseLine(String line) {
        for (int i = 0; i < line.length(); i++) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                nextStates.addAll(state.next(line.charAt(i)));
            }
            currentStates = nextStates;
        }
    }

    private void loadLine(String line) throws FileParseException {
        String [] words = line.split(" ");
        if (lineNum == 0) {
            int number = parseInt(words[0], 0);
            initialState = new State(number, false);
            states.put(number, initialState);
        } else if (lineNum == 1) {
            for (int wordNum = 0; wordNum < words.length; wordNum++) {
                int number = parseInt(words[wordNum], wordNum);
                if (states.containsKey(number)) {
                    throw new FileParseException(lineNum, wordNum);
                }
                states.put(number, new State(number, true));
            }
        } else {
            if (words.length < 3) {
                throw new FileParseException(lineNum, words.length - 1);
            }
            int stateNum = parseInt(words[0], 0);
            int nextNum = parseInt(words[2], 2);
            if (!states.containsKey(stateNum)) {
                states.put(stateNum, new State(stateNum, false));
            }
            if (!states.containsKey(nextNum)) {
                states.put(nextNum, new State(nextNum, false));
            }
            if (words[1].isEmpty()) {
                throw new FileParseException(lineNum, 1);
            }
            states.get(stateNum).setTransition(words[1].charAt(0),
                    states.get(nextNum));
        }
        ++lineNum;
    }

    private int parseInt(String str, int wordNum) throws FileParseException {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new FileParseException(lineNum, wordNum);
        }
    }

    public Set<State> getCurrentStates() {
        return currentStates;
    }

    public State getInitialState() {
        return initialState;
    }

    public Map<Integer, State> getStates() {
        return states;
    }
}
