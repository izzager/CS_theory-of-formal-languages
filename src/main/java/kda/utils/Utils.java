package kda.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Utils {

    public static List<Character> readAlphabet() {
        try {
            List<Character> states = new ArrayList<>();
            Scanner scanner = new Scanner(new File("src/main/java/kda/files/alphabet.txt"));
            while (scanner.hasNext()) {
                states.add(scanner.next().charAt(0));
            }
            return states;
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании алфавита");
            return new ArrayList<>();
        }
    }

    public static HashMap<String, List<SymbolStatePair>> readStateTable(List<Character> alphabet,
                                                                        List<String> states) {
        try {
            Scanner scanner = new Scanner(new File("src/main/java/kda/files/stateTable.txt"));
            HashMap<String, List<SymbolStatePair>> stateTable = new HashMap<>();
            if (states.contains("-")) {
                states.remove("-");
                stateTable.put("-", new ArrayList<>());
                for (Character symbol : alphabet) {
                    stateTable.get("-").add(new SymbolStatePair(symbol, "-"));
                }
            }
            for (String state : states) {
                stateTable.put(state, new ArrayList<>());
                for (Character symbol : alphabet) {
                    String readedState = scanner.next();
                    stateTable.get(state).add(new SymbolStatePair(symbol, readedState));
                }
            }
            return stateTable;
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании данных таблицы состояний");
            return new HashMap<>();
        }
    }

    public static List<String> readStates() {
        try {
            List<String> states = new ArrayList<>();
            Scanner scanner = new Scanner(new File("src/main/java/kda/files/states.txt"));
            while (scanner.hasNext()) {
                states.add(scanner.next());
            }
            return states;
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании данных для состояний");
            return new ArrayList<>();
        }
    }

    public static String readStartState() {
        try {
            Scanner scanner = new Scanner(new File("src/main/java/kda/files/startState.txt"));
            return scanner.next();
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании данных для начального состояния");
            return "";
        }
    }

    public static Set<String> readFinalStates() {
        try {
            Set<String> finalStates = new HashSet<>();
            Scanner scanner = new Scanner(new File("src/main/java/kda/files/finalStates.txt"));
            while (scanner.hasNext()) {
                finalStates.add(scanner.next());
            }
            return finalStates;
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка при считывании данных для конечных состояний");
            return new HashSet<>();
        }
    }

}
