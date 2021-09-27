package kda;

import kda.utils.SymbolStatePair;
import kda.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Character> alphabet = Utils.readAlphabet();
        List<String> states = Utils.readStates();
        HashMap<String, List<SymbolStatePair>> stateTable = Utils.readStateTable(alphabet, states);
        String startState = Utils.readStartState();
        Set<String> finalStates = Utils.readFinalStates();
        KDAutomat kdAutomat = KDAutomat.builder()
                .alphabet(alphabet)
                .stateTable(stateTable)
                .states(states)
                .startState(startState)
                .finalStates(finalStates)
                .build();

        System.out.println("Алфавит: " + alphabet);
        System.out.println("Состояния: " + states);
        System.out.println("Стартовое состояние: " + startState);
        System.out.println("Финальные состояния: " + finalStates);

        System.out.print("Введите строку, которую автомат будет анализировать: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        boolean isAccepted = kdAutomat.work(input);
        if (isAccepted) {
            System.out.println("Строка принимается");
        } else {
            System.out.println("Строка не принимается");
        }

        //запись в файл
        File output = new File("output.txt");
        FileWriter writer = new FileWriter(output);
        writer.write("Алфавит: " + alphabet + "\n");
        writer.write("Состояния: " + states + "\n");
        writer.write("Стартовое состояние: " + startState + "\n");
        writer.write("Финальные состояния: " + finalStates + "\n");
        writer.write("Анализируемая строка: " + input + "\n");
        if (isAccepted) {
            writer.write("Строка принимается");
        } else {
            writer.write("Строка не принимается");
        }
        writer.flush();
        writer.close();
    }

}
