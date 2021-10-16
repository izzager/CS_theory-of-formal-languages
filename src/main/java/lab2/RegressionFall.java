package lab2;

import com.sun.source.tree.Tree;
import lab1.ELexType;
import lab1.Lex;

import java.util.List;

public class RegressionFall {

    private final List<Lex> lexes;
    private int curLex = 0;
    private TreeNode<Lex> tree;

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
        this.tree = new TreeNode<>(lexes.get(curLex));
        curLex++;

        if (!Statement(tree)) return false;

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
        TreeNode<Lex> whileNode = this.tree.addChild(lexes.get(curLex));
        curLex++;

        if (!Condition(whileNode)) return false;

        if (curLex < lexes.size()) {
            Error("Лишние символы", lexes.get(curLex).getPos());
            return false;
        }

        return true;
    }

    private boolean Condition(TreeNode<Lex> whileNode) {
        if (!LogExpr()) return false;
        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lOr) {
            curLex++;
            if (!LogExpr()) return false;
        }
        return true;
    }

    private boolean LogExpr() {
        if (!RelExpr()) return false;
        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAnd) {
            curLex++;
            if (!RelExpr()) return false;
        }
        return true;
    }

    private boolean RelExpr() {
        if (!Operand()) return false;
        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lRel){
            curLex++;
            if (!Operand()) return false;
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
        TreeNode<Lex> asNode = tree.children.get(0);
        asNode.addChild(lexes.get(curLex - 1));

        curLex++;
        if (!ArithExpr(asNode)) return false;
        return true;
    }

    private boolean ArithExpr(TreeNode<Lex> tree) {
        if (!Operand()) return false;

        if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            tree.addChild(lexes.get(curLex - 1));
            tree.children.get(0).addChild(lexes.get(curLex - 1));
        } else {
            tree.addChild(lexes.get(curLex - 1));
        }
        TreeNode<Lex> aoNode = tree.children.get(0);

        while (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
            curLex++;
            if (!Operand()) return false;

            if (curLex < lexes.size() && lexes.get(curLex).getELexType() == ELexType.lAo) {
                aoNode.addChild(lexes.get(curLex - 1));
                aoNode.children.get(0).addChild(lexes.get(curLex - 1));
            } else {
                aoNode.addChild(lexes.get(curLex - 1));
            }
            aoNode = aoNode.children.get(0);
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
