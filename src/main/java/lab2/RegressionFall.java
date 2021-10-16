package lab2;

import lab1.ELexType;
import lab1.Lex;

import java.util.List;

public class RegressionFall {

    private final List<Lex> lexes;
    private int curLex = 0;
    private TreeNode<Lex> tree;

    public TreeNode<Lex> getTree() {
        return tree.getChildren().get(0);
    }

    public RegressionFall(List<Lex> lexes) {
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

        if (!Statement(tree.getChildren().get(0))) return false;

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

        if (curLex < lexes.size()) {
            Error("Лишние символы", lexes.get(curLex).getPos());
            return false;
        }

        return true;
    }

    private TreeNode<Lex> tempConditions;

    private boolean Condition() {
        if (!LogExpr()) return false;

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
        }
        return true;
    }

    private TreeNode<Lex> tempLogExpr;

    private boolean LogExpr() {
        if (!RelExpr()) return false;

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
        }
        return true;
    }

    private TreeNode<Lex> tempRelExpr;

    private boolean RelExpr() {
        if (!Operand()) return false;
        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lRel) {
            tempRelExpr = new TreeNode<>(lexes.get(curLex));
            tempRelExpr.addChild(lexes.get(curLex - 1));
            curLex++;
            if (!Operand()) return false;
            tempRelExpr.addChild(lexes.get(curLex - 1));
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
        curLex++;
        return true;
    }

//    private boolean LogicalOp() {
//        if (curLex >= lexes.size()) {
//            Error("Ожидается логическая операция");
//            return false;
//        }
//        if (lexes.get(curLex).getELexType() != ELexType.lAnd
//                    && lexes.get(curLex).getELexType() != ELexType.lOr) {
//            Error("Ожидается логическая операция", lexes.get(curLex).getPos());
//            return false;
//        }
//        curLex++;
//        return true;
//    }

    private boolean Statement(TreeNode<Lex> tree) {
        if (curLex >= lexes.size()) {
            Error("Ожидается переменная");
            return false;
        }
        if (lexes.get(curLex).getELexType() != ELexType.lVar) {
            Error("Ожидается переменная", lexes.get(curLex).getPos());
            return false;
        }
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
        return true;
    }

    private boolean ArithExpr(TreeNode<Lex> tree) {
        if (!Operand()) return false;

        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            tree.addChild(lexes.get(curLex));
            tree.getChildren().get(1).addChild(lexes.get(curLex - 1));
        } else {
            tree.addChild(lexes.get(curLex - 1));
        }
        TreeNode<Lex> aoNode = tree.getChildren().get(1);

        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            curLex++;
            if (!Operand()) return false;

            if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
                aoNode.addChild(lexes.get(curLex));
                aoNode.getChildren().get(1).addChild(lexes.get(curLex - 1));
            } else {
                aoNode.addChild(lexes.get(curLex - 1));
            }
            aoNode = aoNode.getChildren().get(1);
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
