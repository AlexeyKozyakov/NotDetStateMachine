package koziakov.compillers.notdetstatemachine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class State {

    private int number;

    private boolean terminal;
    private final Map<Character, List<State>> transitions = new HashMap<>();

    public State(int number, boolean terminal) {
        this.number = number;
        this.terminal = terminal;
    }

    public void setTransition(Character symbol, State state) {
        if (!transitions.containsKey(symbol)) {
            transitions.put(symbol, new ArrayList<>());
        }
        transitions.get(symbol).add(state);
    }

    public List<State> next(char symbol) {
        return transitions.get(symbol);
    }

    public boolean isTerminal() {
        return terminal;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof State) && number == ((State) obj).number;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number);
    }
}
