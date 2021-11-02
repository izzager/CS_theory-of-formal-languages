package lab4;

import lab3.ECmd;
import lab3.EEntryType;
import lab3.PostfixEntry;
import lab3.SyntacticAnalysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Interpreter {

    private final List<PostfixEntry> postfixEntries;

    private final SyntacticAnalysis syntacticAnalysis;

    private Stack<PostfixEntry> stack;

    private Map<String, Integer> vars;

    public Interpreter(List<PostfixEntry> postfixEntries, SyntacticAnalysis syntacticAnalysis) {
        this.postfixEntries = postfixEntries;
        this.syntacticAnalysis = syntacticAnalysis;
        this.stack = new Stack<>();
        this.vars = new HashMap<>();
    }

    public void interpret() {
        syntacticAnalysis.getVarTable().forEach(varLex -> vars.put(varLex.getContent(), 0));
        int tmp;
        int pos = 0;
        int var1 = 0, var2 = 0;
        PostfixEntry last;
        int step = 0;
        while (pos < postfixEntries.size()) {
            if (postfixEntries.get(pos).getType() == EEntryType.etCmd) {
                ECmd cmd = findCmdByNumber(postfixEntries.get(pos).getIndex());
                switch (cmd) {
                    case JMP:
                        pos = stack.pop().getIndex();
                        break;
                    case JZ:
                        tmp = stack.pop().getIndex();
                        if (stack.pop().getIndex() == 1) {
                            pos++;
                        } else {
                            pos = tmp;
                        }
                        break;
                    case SET:
                        var1 = popValue();
                        last = stack.pop();
                        vars.put(syntacticAnalysis.getVarTable().get(last.getIndex()).getContent(), var1);
                        pos++;
                        break;
                    case ADD:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 + var2));
                        pos++;
                        break;
                    case SUB:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, - var1 + var2));
                        pos++;
                        break;
                    case MUL:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 * var2));
                        pos++;
                        break;
                    case DEL:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var2 / var1));
                        pos++;
                        break;
                    case AND:
                        stack.push(new PostfixEntry(EEntryType.etConst,
                                stack.pop().getIndex() == 1 && stack.pop().getIndex() == 1 ? 1 : 0));
                        pos++;
                        break;
                    case OR:
                        stack.push(new PostfixEntry(EEntryType.etConst,
                                stack.pop().getIndex() == 1 || stack.pop().getIndex() == 1 ? 1 : 0));
                        pos++;
                        break;
                    case CMPE:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 == var2 ? 1 : 0));
                        pos++;
                        break;
                    case CMPNE:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 != var2 ? 1 : 0));
                        pos++;
                        break;
                    case CMPL:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 > var2 ? 1 : 0));
                        pos++;
                        break;
                    case CMPLE:
                        var1 = popValue();
                        var2 = popValue();
                        stack.push(new PostfixEntry(EEntryType.etConst, var1 >= var2 ? 1 : 0));
                        pos++;
                        break;
                    case NOT:
                        var1 = stack.pop().getIndex() == 1 ? 0 : 1;
                        stack.push(new PostfixEntry(EEntryType.etConst, var1));
                        pos++;
                        break;
                }
            } else if (postfixEntries.get(pos).getType() == EEntryType.etConst) {
                int constNumber = postfixEntries.get(pos).getIndex();
                int constValue = Integer.parseInt(syntacticAnalysis.getConstTable().get(constNumber).getContent());
                stack.push(new PostfixEntry(EEntryType.etConst, constValue));
                pos++;
            } else {
                stack.push(postfixEntries.get(pos));
                pos++;
            }

            if (pos < postfixEntries.size()) {
                System.out.println("Шаг " + step
                        + ". Текущий элемент ПОЛИЗА #" + pos + ": " + writeOneFromTablePretty(postfixEntries.get(pos))
                        + ". Стек: " + writeStackPretty()
                        + ". Значения переменных: " + vars);
                step++;
            } else {
                System.out.println("Выполнение завершено. Значения переменных: " + vars);
            }
        }
    }

    private int popValue() {
        PostfixEntry last = stack.pop();
        if (last.getType() == EEntryType.etVar) {
            String varName = syntacticAnalysis.getVarTable().get(last.getIndex()).getContent();
            return vars.get(varName);
        } else {
            return last.getIndex();
        }
    }

    private static ECmd findCmdByNumber(int number) {
        int i = 0;
        for (ECmd cmd : ECmd.values()) {
            if (i < number) {
                i++;
            } else {
                return cmd;
            }
        }
        return ECmd.JZ;
    }

    private String writeOneFromTablePretty(PostfixEntry postfixEntry) {
        if (postfixEntry.getType() == EEntryType.etVar) {
            return syntacticAnalysis.getVarTable().get(postfixEntry.getIndex()).getContent();
        } else if (postfixEntry.getType() == EEntryType.etConst) {
            return syntacticAnalysis.getConstTable().get(postfixEntry.getIndex()).getContent();
        } else if (postfixEntry.getType() == EEntryType.etCmdPtr) {
            return String.valueOf(postfixEntry.getIndex());
        } else {
            return findCmdByNumber(postfixEntry.getIndex()).name();
        }
    }

    private String writeOneFromStackPretty(PostfixEntry postfixEntry) {
        if (postfixEntry.getType() == EEntryType.etVar) {
            return syntacticAnalysis.getVarTable().get(postfixEntry.getIndex()).getContent();
        } else if (postfixEntry.getType() == EEntryType.etConst) {
            return String.valueOf(postfixEntry.getIndex());
        } else if (postfixEntry.getType() == EEntryType.etCmdPtr) {
            return String.valueOf(postfixEntry.getIndex());
        } else {
            return findCmdByNumber(postfixEntry.getIndex()).name();
        }
    }

    private String writeStackPretty() {
        Stack<PostfixEntry> tmpStack = new Stack<>();
        tmpStack.addAll(stack);
        StringBuilder stringBuilder = new StringBuilder();
        while (!tmpStack.empty()) {
            stringBuilder.insert(0, writeOneFromStackPretty(tmpStack.pop()) + " ");
        }
        return stringBuilder.toString();
    }

}
