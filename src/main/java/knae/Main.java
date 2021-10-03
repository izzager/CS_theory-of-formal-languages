package knae;

import knae.utils.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        KNEAutomat kneAutomat = Utils.readAutomateFromFile("src/main/java/knae/files/knae.txt");

        System.out.println("Алфавит: " + kneAutomat.getAlphabet());
        System.out.println("Состояния: " + kneAutomat.getStates());
        System.out.println("Стартовое состояние: " + kneAutomat.getStartState());
        System.out.println("Финальные состояния: " + kneAutomat.getFinalStates());
        kneAutomat.getStateTable().forEach((s, statesForSymbols) ->
                System.out.println(s + "=" + statesForSymbols));

        System.out.print("Введите строку, которую автомат будет анализировать: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        boolean isAccepted = kneAutomat.work(input);
        System.out.println("\nМножество состояний в конце работы: " + kneAutomat.getCurrentStates());
        if (isAccepted) {
            System.out.println("Строка принимается");
        } else {
            System.out.println("Строка не принимается");
        }

        writeToFile(kneAutomat, input, isAccepted);
    }

    private static void writeToFile(KNEAutomat knAutomat,
                                    String input,
                                    boolean isAccepted) throws IOException {
        File output = new File("src/main/java/knae/files/output.txt");
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
