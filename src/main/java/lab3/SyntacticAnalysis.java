package lab3;

import lab1.ELexType;
import lab1.Lex;
import lab2.TreeNode;

import java.util.List;

public class SyntacticAnalysis {

    private final List<Lex> lexes;
    private int curLex = 0;
    private TreeNode<Lex> tree;

    public TreeNode<Lex> getTree() {
        return tree.getChildren().get(0);
    }

    public SyntacticAnalysis(List<Lex> lexes) {
        this.lexes = lexes;
    }

    public boolean begin() {
        return DoStatement();
    }

    private boolean DoStatement() {
        if (curLex >= lexes.size()) {
            Error("Ожидается do");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lDo) {
            Error("Ожидается do", lexes.get(curLex).getPos());
            return false;
        }
        tree = new TreeNode<>(new Lex());
        tree.addChild(lexes.get(curLex));
        curLex++;

        int indFirst = 0; // сохраняем адрес начала цикла
        if (!Statement(tree.getChildren().get(0))) return false;
        // сформирована часть ПОЛИЗа для тела цикла

        if (curLex >= lexes.size()) {
            Error("Ожидается loop");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lLoop) {
            Error("Ожидается loop", lexes.get(curLex).getPos());
            return false;
        }
        curLex++;
        if (curLex >= lexes.size()) {
            Error("Ожидается while");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lWhile) {
            Error("Ожидается while", lexes.get(curLex).getPos());
            return false;
        }
        tree.getChildren().get(0).addChild(lexes.get(curLex));
        curLex++;

        if (!Condition()) return false;
        tree.getChildren().get(0).getChildren().get(1).addChild(tempConditions);
        // сформирована часть ПОЛИЗа, вычисляющая условие цикла
        Poliz.writeCmd(ECmd.NOT); //заносим отрицание, т.к. условный переход по лжи
        Poliz.writeCmdPtr(indFirst); //заносим значение адреса условного перехода
        Poliz.writeCmd(ECmd.JZ); // заносим команду условного перехода по лжи

        if (curLex < lexes.size()) {
            Error("Лишние символы", lexes.get(curLex).getPos());
            return false;
        }

        return true;
    }

    private TreeNode<Lex> tempConditions;

    private boolean Condition() {
        if (!LogExpr()) return false;
        // сформирована часть ПОЛИЗа для вычисления логического подвыражения

        if (curLex >= lexes.size() || lexes.get(curLex).getELexType() != ELexType.lOr) {
            tempConditions = tempLogExpr;
            return true;
        }

        tempConditions = new TreeNode<>(lexes.get(curLex));
        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lOr) {
            tempConditions.addChild(tempLogExpr);
            curLex++;
            if (!LogExpr()) return false;
            tempConditions.addChild(tempLogExpr);
            // сформирована часть ПОЛИЗа для вычисления логического подвыражения
            Poliz.writeCmd(ECmd.OR); //заносим операцию OR в ПОЛИЗ
        }
        return true;
    }

    private TreeNode<Lex> tempLogExpr;

    private boolean LogExpr() {
        if (!RelExpr()) return false;
        // сформирована часть ПОЛИЗа для вычисления подвыражения сравнения

        if (curLex >= lexes.size() || lexes.get(curLex).getELexType() != ELexType.lAnd) {
            tempLogExpr = tempRelExpr;
            return true;
        }

        tempLogExpr = new TreeNode<>(lexes.get(curLex));
        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAnd) {
            tempLogExpr.addChild(tempRelExpr);
            curLex++;
            if (!RelExpr()) return false;
            tempLogExpr.addChild(tempRelExpr);
            // сформирована часть ПОЛИЗа для вычисления подвыражения сравнения
            Poliz.writeCmd(ECmd.AND); //заносим операцию AND в ПОЛИЗ
        }
        return true;
    }

    private TreeNode<Lex> tempRelExpr;

    private boolean RelExpr() {
        if (!Operand()) return false;
        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lRel) {
            ECmd cmd; // определяем код операции
            switch (lexes.get(curLex).getContent()) {
                case "<":
                    cmd = ECmd.CMPL;
                    break;
                case "<=":
                    cmd = ECmd.CMPLE;
                    break;
                case "<>":
                    cmd = ECmd.CMPNE;
                    break;
                case "==":
                    cmd = ECmd.CMPE;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + lexes.get(curLex).getContent());
            }

            tempRelExpr = new TreeNode<>(lexes.get(curLex));
            tempRelExpr.addChild(lexes.get(curLex - 1));
            curLex++;
            if (!Operand()) return false;
            tempRelExpr.addChild(lexes.get(curLex - 1));
            Poliz.writeCmd(cmd); // заносим операцию в ПОЛИЗ
        }
        return true;
    }

    private boolean Operand() {
        if (curLex >= lexes.size()) {
            Error("Ожидается переменная или константа");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lVar
                && lexes.get(curLex).getELexType() != ELexType.lConst) {
            Error("Ожидается переменная или константа", lexes.get(curLex).getPos());
            return false;
        }
        if (lexes.get(curLex).getELexType() == ELexType.lVar) {
            Poliz.writeVar(lexes.get(curLex).getPos()); // тип лексемы – переменная
        } else {
            Poliz.writeConst(lexes.get(curLex).getPos()); // тип лексемы - константа
        }
        curLex++;
        return true;
    }

    private boolean Statement(TreeNode<Lex> tree) {
        if (curLex >= lexes.size()) {
            Error("Ожидается переменная");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lVar) {
            Error("Ожидается переменная", lexes.get(curLex).getPos());
            return false;
        }
        Poliz.writeVar(lexes.get(curLex).getPos()); // заносим в ПОЛИЗ переменную
        curLex++;
        if (curLex >= lexes.size()) {
            Error("Ожидается присваивание");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lAs){
            Error("Ожидается присваивание", lexes.get(curLex).getPos());
            return false;
        }
        tree.addChild(lexes.get(curLex));
        TreeNode<Lex> asNode = tree.getChildren().get(0);
        asNode.addChild(lexes.get(curLex - 1));

        curLex++;
        if (!ArithExpr(asNode)) return false;
        // ПОЛИЗ для выражения уже сформирован
        Poliz.writeCmd(ECmd.SET); // заносим в ПОЛИЗ команду присваивания
        return true;
    }

    private boolean ArithExpr(TreeNode<Lex> tree) {
        if (!Operand()) return false;
        // сформирована часть ПОЛИЗа для операнда

        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            tree.addChild(lexes.get(curLex));
            tree.getChildren().get(1).addChild(lexes.get(curLex - 1));
        } else {
            tree.addChild(lexes.get(curLex - 1));
        }
        TreeNode<Lex> aoNode = tree.getChildren().get(1);

        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            ECmd cmd; // определяем код операции
            switch (lexes.get(curLex).getContent()) {
                case "+":
                    cmd = ECmd.ADD;
                    break;
                case "-":
                    cmd = ECmd.SUB;
                    break;
                case "*":
                    cmd = ECmd.MUL;
                    break;
                case "/":
                    cmd = ECmd.DEL;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + lexes.get(curLex).getContent());
            }

            curLex++;
            if (!Operand()) return false;

            if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
                aoNode.addChild(lexes.get(curLex));
                aoNode.getChildren().get(1).addChild(lexes.get(curLex - 1));
            } else {
                aoNode.addChild(lexes.get(curLex - 1));
            }
            aoNode = aoNode.getChildren().get(1);

            // сформирована часть ПОЛИЗа для операнда
            Poliz.writeCmd(cmd); // заносим операцию в ПОЛИЗ
        }

        return true;
    }

    private void Error(String errorMessage, int position) {
        System.out.println(errorMessage + " в позиции " + position);
    }

    private void Error(String errorMessage) {
        int pos = 0;
        if (lexes.size() > 0) {
            pos = lexes.get(lexes.size() - 1).getPos()
                    + lexes.get(lexes.size() - 1).getContent().length();
        }
        Error(errorMessage, pos);
    }
}
