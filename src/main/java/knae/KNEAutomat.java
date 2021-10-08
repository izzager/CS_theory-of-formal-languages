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
        System.out.println("\nТекущий символ: " + currentSymbol + ". ");

        previousStates.forEach(previousState -> {
            List<String> statesForSymb = stateTable.get(previousState)
                    .stream()
                    .filter(statesForSymbol -> statesForSymbol.getSymbol().equals(currentSymbol))
                    .map(StatesForSymbol::getStates)
                    .findFirst()
                    .get();
            currentStates.addAll(statesForSymb);
            if (isAbleEpsTransition(statesForSymb.get(0))) {
                System.out.println("Состояния " + previousStates + " -> " + currentStates);
                System.out.println("Начинаем е-переход: ");
                epsilonTransit(currentSymbol, statesForSymb.get(0));
            }
        });

        System.out.println("Состояния " + previousStates + " -> " + currentStates);
    }

    private boolean isAbleEpsTransition(String previousState) {
        return getStatesForEps(previousState).getStates().size() > 0;
    }

    private void epsilonTransit(Character currentSymbol, String previousState) {
        StatesForSymbol e = getStatesForEps(previousState);
        printInfoAndChangeState(currentSymbol, e, previousState);

        boolean endOfEpsTransition = false;
        Set<String> statesSet = new HashSet<>(); //поиск циклов
        while (!endOfEpsTransition) {
            for (String curSt : currentStates) {
                e = getStatesForEps(curSt);
                if (e.getStates().size() > 0) {
                    printInfoAndChangeState(currentSymbol, e, curSt);
                }
                boolean isAdded = statesSet.addAll(e.getStates());
                System.out.println(statesSet);
                if (!isAdded) {
                    endOfEpsTransition = true;
                    break;
                }
            }
            if (endOfEpsTransition) {
                break;
            }
            endOfEpsTransition = checkIfEndOfEpsTransition();
        }
    }

    private boolean checkIfEndOfEpsTransition() {
        StatesForSymbol e;
        boolean endOfEpsTransition = false;
        for (String st : currentStates) {
            e = getStatesForEps(st);
            if (e.getStates().size() == 0) {
                endOfEpsTransition = true;
            } else {
                endOfEpsTransition = false;
                break;
            }
        }
        return endOfEpsTransition;
    }

    private void printInfoAndChangeState(Character currentSymbol, StatesForSymbol e, String curSt) {
        System.out.println("e-переход для символа " + currentSymbol + ": " +
                curSt + " -> " + e.getStates());
        currentStates.remove(curSt);
        currentStates.addAll(e.getStates());
    }

    private StatesForSymbol getStatesForEps(String curSt) {
        return stateTable.get(curSt).stream()
                .filter(statesForSymbol -> statesForSymbol.getSymbol().equals('e'))
                .findFirst()
                .get();
    }

}
