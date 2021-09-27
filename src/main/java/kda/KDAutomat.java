package kda;

import kda.utils.SymbolStatePair;
import lombok.Builder;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Builder
public class KDAutomat {

    private List<Character> alphabet;
    private HashMap<String, List<SymbolStatePair>> stateTable;
    private List<String> states;
    private String startState;
    private Set<String> finalStates;
    private String currentState;

    public boolean work(String input) {
        currentState = startState;
        for (int i = 0; i < input.length(); i++) {
            changeState((input.charAt(i)));
        }
        return finalStates.contains(currentState);
    }

    private void changeState(Character currentSymbol) {
        String previousState = currentState;
        currentState = stateTable.get(currentState)
                .stream()
                .filter(symbolStatePair -> symbolStatePair.getSymbol().equals(currentSymbol))
                .map(SymbolStatePair::getState)
                .findFirst()
                .get();
        if (currentState == "-") {
            currentState = previousState;
        }
        System.out.println("Текущий символ: " + currentSymbol + ". Состояние " + previousState + " -> " + currentState);
    }

}
