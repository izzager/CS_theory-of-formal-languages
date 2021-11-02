package lab4;

import lab1.LexAnalysis;
import lab3.Poliz;
import lab3.SyntacticAnalysis;

import java.util.Scanner;
import java.util.Stack;

public class Lab4Main {

    //do a = a + 2 loop while a < 3

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

        System.out.println("Внутреннее представление программы:");
        System.out.println(Poliz.writePretty(lexAnalysis));

        System.out.println("Шаги работы интерпретатора: ");
        Interpreter interpreter = new Interpreter(Poliz.getPostfixEntries(), syntacticAnalysis);
        interpreter.interpret();
    }

}
