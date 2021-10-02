package kna;

import kna.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        KNAutomat knAutomat = Utils.readAutomateFromFile("src/main/java/kna/files/kna.txt");

        System.out.println("Алфавит: " + knAutomat.getAlphabet());
        System.out.println("Состояния: " + knAutomat.getStates());
        System.out.println("Стартовое состояние: " + knAutomat.getStartState());
        System.out.println("Финальные состояния: " + knAutomat.getFinalStates());
        knAutomat.getStateTable().forEach((s, statesForSymbols) ->
                System.out.println(s + "=" + statesForSymbols));

        System.out.print("Введите строку, которую автомат будет анализировать: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        boolean isAccepted = knAutomat.work(input);
        System.out.println("Множество состояний в конце работы: " + knAutomat.getCurrentStates());
        if (isAccepted) {
            System.out.println("Строка принимается");
        } else {
            System.out.println("Строка не принимается");
        }

        writeToFile(knAutomat, input, isAccepted);
    }

    private static void writeToFile(KNAutomat knAutomat,
                                    String input,
                                    boolean isAccepted) throws IOException {
        File output = new File("src/main/java/kna/files/output.txt");
        FileWriter writer = new FileWriter(output);
        writer.write("Алфавит: " + knAutomat.getAlphabet() + "\n");
        writer.write("Состояния: " + knAutomat.getStates() + "\n");
        writer.write("Стартовое состояние: " + knAutomat.getStartState() + "\n");
        writer.write("Финальные состояния: " + knAutomat.getFinalStates() + "\n");
        writer.write("Анализируемая строка: " + input + "\n");
        writer.write("Множество состояний в конце работы: " + knAutomat.getCurrentStates() + "\n");
        if (isAccepted) {
            writer.write("Строка принимается");
        } else {
            writer.write("Строка не принимается");
        }
        writer.flush();
        writer.close();
    }

}
