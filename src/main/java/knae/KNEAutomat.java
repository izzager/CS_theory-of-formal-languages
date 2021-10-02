package knae;

import kna.utils.StatesForSymbol;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
public class KNEAutomat {

    private List<Character> alphabet;
    private HashMap<String, List<StatesForSymbol>> stateTable;
    private List<String> states;
    private String startState;
    private Set<String> finalStates;
    private Set<String> currentStates;

    public boolean work(String input) {
        currentStates = new LinkedHashSet<>();
        currentStates.add(startState);
        for (int i = 0; i < input.length(); i++) {
            changeState((input.charAt(i)));
        }
        Set<String> result =  new HashSet<>(currentStates);
        result.retainAll(finalStates);
        return !result.isEmpty();
    }

    private void changeState(Character currentSymbol) {
        Set<String> previousStates = new LinkedHashSet<>(currentStates);
        currentStates = new LinkedHashSet<>();
        previousStates.forEach(previousState -> {
            List<String> statesForSymb = stateTable.get(previousState)
                    .stream()
                    .filter(statesForSymbol -> statesForSymbol.getSymbol().equals(currentSymbol))
                    .map(StatesForSymbol::getStates)
                    .findFirst()
                    .get();
            currentStates.addAll(statesForSymb);
            if (stateTable.get(previousState).stream()
                    .filter(statesForSymbol -> statesForSymbol.getSymbol().equals('e'))
                    .findFirst()
                    .get().getStates().size() > 0) {
                epsilonTransit(currentSymbol, previousState);
            }
        });
        System.out.println("Текущий символ: " + currentSymbol + ". " +
                "Состояния " + previousStates + " -> " + currentStates);
    }

    private void epsilonTransit(Character currentSymbol, String previousState) {
        StatesForSymbol e = stateTable.get(previousState).stream()
                .filter(statesForSymbol -> statesForSymbol.getSymbol().equals('e'))
                .findFirst()
                .get();
        System.out.println("e-переход для символа " + currentSymbol + ": " +
                previousState + " -> " + e.getStates());
        currentStates.remove(previousState);
        currentStates.addAll(e.getStates());
    }

//    private void epsilonTransit(String previousState) {
//        Set<String> previousStates = new LinkedHashSet<>();
//        previousStates.add(previousState);
//        Set<String> newStates = new LinkedHashSet<>();
//        while (!newSt.equals(prev)) {
//            prev = newSt;
//            stateTable.get(newSt).stream()
//                    .filter(statesForSymbol -> statesForSymbol.getSymbol().equals('e'))
//                    .findFirst()
//                    .isPresent();
//            System.out.println("e-переход: " + prev + " -> " + newSt);
//        }
//        currentStates.add(newSt);
//    }

}
