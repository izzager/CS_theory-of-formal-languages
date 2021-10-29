package lab3;

import lab1.LexAnalysis;

import java.util.Scanner;

public class Lab3Main {

    //do a = a * 2 loop while a < 100 and b == 10

    public static void main(String[] args) {
        System.out.println("Введите анализируемый текст: ");
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();

        LexAnalysis lexAnalysis = new LexAnalysis();
        boolean result = lexAnalysis.analyze(text);
        if (result) {
            System.out.println("Текст соответствует правилам.");
        } else {
            System.out.println("Текст не соответствует правилам!");
            return;
        }

        SyntacticAnalysis syntacticAnalysis = new SyntacticAnalysis(lexAnalysis);
        boolean syntacsisAnalysisResult = syntacticAnalysis.begin();
        if (syntacsisAnalysisResult) {
            System.out.println("Синтаксический анализ успешен.");
        } else {
            System.out.println("Синтаксический анализ произошел с ошибками.");
        }
        System.out.println();

        System.out.println("Внутреннее представление программы:");
        Poliz.getPostfixEntries().forEach(System.out::println);
        System.out.println();

        System.out.println(Poliz.writePretty(lexAnalysis));
    }
}
