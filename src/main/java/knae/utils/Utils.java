package knae.utils;

import kna.utils.StatesForSymbol;
import knae.KNEAutomat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    public static KNEAutomat readAutomateFromFile(String filename) {
        List<Character> alphabet = new ArrayList<>();
        List<String> states = new ArrayList<>();
        String startState = "";
        Set<String> finalStates = new HashSet<>();
        HashMap<String, List<StatesForSymbol>> stateTable = new LinkedHashMap<>();

        try {
            Scanner scanner = new Scanner(new File(filename));
            alphabet = getAlphabet(scanner);
            states = getStates(scanner);
            startState = scanner.nextLine();
            finalStates = getFinalStates(scanner);
            stateTable = getStateTable(states, stateTable, scanner);

        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании файла. Формат файла:" +
                    "1 строка - символы алфавита через пробел" +
                    "2 строка - состояния через пробел" +
                    "3 строка - стартовое состоние" +
                    "4 строка - набор финальных состояний через пробел" +
                    "последующие строки вида - <состоние символ набор_состояний_перехода>");
        }

        return KNEAutomat.builder()
                .alphabet(alphabet)
                .stateTable(stateTable)
                .states(states)
                .startState(startState)
                .finalStates(finalStates)
                .build();
    }

    private static HashMap<String, List<StatesForSymbol>> getStateTable(List<String> states,
                                      HashMap<String, List<StatesForSymbol>> stateTable,
                                      Scanner scanner) {
        for (String s : states) {
            stateTable.put(s, new ArrayList<>());
        }
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(" ");
            String curState = line[0];
            Character curSymb = line[1].charAt(0);
            List<String> statesForSymb = Arrays.asList(Arrays.copyOfRange(line, 2, line.length));
            if (curSymb == 'e' && statesForSymb.size() == 1 && statesForSymb.get(0).equals("-")) {
                statesForSymb = new ArrayList<>();
            }
            stateTable.get(curState).add(new StatesForSymbol(curSymb, statesForSymb));
        }
        return stateTable;
    }

    private static Set<String> getFinalStates(Scanner scanner) {
        Set<String> finalStates;
        finalStates = Arrays.stream(scanner.nextLine()
                        .split(" "))
                .collect(Collectors.toSet());
        return finalStates;
    }

    private static List<String> getStates(Scanner scanner) {
        List<String> states;
        states = Arrays.stream(scanner.nextLine()
                        .split(" "))
                .collect(Collectors.toList());
        return states;
    }

    private static List<Character> getAlphabet(Scanner scanner) {
        List<Character> alphabet;
        alphabet = Arrays.stream(scanner.nextLine()
                        .split(" "))
                .map(s -> s.charAt(0))
                .collect(Collectors.toList());
        return alphabet;
    }

}
