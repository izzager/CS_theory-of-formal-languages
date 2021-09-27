package lab1;

import java.util.Scanner;

public class Lab1Main {

    //do a = a * 2 loop while a < 100 and b == 25
    //while a < b and b <= c do b=b+c-20 end

    public static void main(String[] args) {
        System.out.println("Введите анализируемый текст: ");
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        LexAnalysis lexAnalysis = new LexAnalysis();
        boolean result = lexAnalysis.analyze(text);

        System.out.println("Список всех лексем: ");
        lexAnalysis.getLexes().forEach(System.out::println);
        System.out.println();

        System.out.println("Список констант: ");
        lexAnalysis.getConsts().forEach(System.out::println);
        System.out.println();

        System.out.println("Список идентификаторов: ");
        lexAnalysis.getVars().forEach(System.out::println);

        if (result) {
            System.out.println("Текст соответствует правилам.");
        } else {
            System.out.println("Текст не соответствует правилам!");
        }
    }
}
