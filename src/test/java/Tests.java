import koziakov.compillers.notdetstatemachine.exceptions.FileParseException;
import koziakov.compillers.notdetstatemachine.model.State;
import koziakov.compillers.notdetstatemachine.model.StateMachine;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class Tests {

    @Test
    public void testState() {
        State state = new State(0, false);
        Assert.assertFalse(state.isTerminal());
        Assert.assertEquals(0, state.getNumber());
    }

    @Test
    public void testTransition() {
        State initial = new State(0, false);
        State next1 = new State(1, true);
        State next2 = new State(2, true);
        initial.setTransition('a', next1);
        initial.setTransition('a', next2);
        List<State> nextStates = initial.next('a');
        Assert.assertEquals(2, nextStates.size());
        Assert.assertEquals(nextStates.get(0), next1);
        Assert.assertEquals(nextStates.get(1), next2);
    }

    @Test
    public void testMachine() throws FileParseException {
        String description = "1\n3\n1 a 2\n1 a 3\n1 b 1";
        String text = "a";
        StateMachine machine = new StateMachine();
        machine.load(new StringReader(description));
        machine.parse(new StringReader(text));
        Set<State> states = machine.getCurrentStates();
        Assert.assertEquals(2, states.size());
        Assert.assertTrue(states.contains(new State(2, false)));
        Assert.assertTrue(states.contains(new State(3, true)));
    }
}
