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
            regressionFall.getTree().traverseTree();
        } else {
            System.out.println("Синтаксический анализ произошел с ошибками.");
        }
        //print(regressionFall);

    }

    private static void print(RegressionFall regressionFall) {
        System.out.println("1:" + regressionFall.getTree().getData());

        System.out.println("2:" + regressionFall.getTree().getChildren().get(0).getData());

        System.out.println("3:" + regressionFall.getTree().getChildren().get(0).getChildren().get(0).getData());
        System.out.println("4:" + regressionFall.getTree().getChildren().get(0).getChildren().get(1).getData());

        System.out.println("5:" + regressionFall.getTree().getChildren().get(0).getChildren().get(1).getChildren().get(0).getData());
        System.out.println("6:" + regressionFall.getTree().getChildren().get(0).getChildren().get(1).getChildren().get(1).getData());

        System.out.println("7:" + regressionFall.getTree().getChildren().get(1).getData());

        System.out.println("8:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getData());

        System.out.println("9:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(0).getData());

        System.out.println("10:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(0)
                .getChildren().get(0).getData());
        System.out.println("11:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(0)
                .getChildren().get(1).getData());

        System.out.println("12:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(1).getData());

        System.out.println("13:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(1)
                .getChildren().get(0).getData());
        System.out.println("14:" + regressionFall.getTree().getChildren().get(1).getChildren().get(0).getChildren().get(1)
                .getChildren().get(1).getData());
    }
}
