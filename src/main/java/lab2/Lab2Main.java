package lab2;

import lab1.LexAnalysis;

import java.util.Scanner;

public class Lab2Main {

    //do a = a * 2 loop while a < 100 and b == 10
    //while a < b and b <= c do b=b+c-20 end

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

        RegressionFall regressionFall = new RegressionFall(lexAnalysis.getLexes());
        boolean regressionFallResult = regressionFall.begin();
        if (regressionFallResult) {
            System.out.println("Синтаксический анализ успешен.");
        } else {
            System.out.println("Синтаксический анализ произошел с ошибками.");
        }
    }
}
