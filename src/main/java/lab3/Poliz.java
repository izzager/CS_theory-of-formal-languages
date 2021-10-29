package lab3;

import lab1.Lex;
import lab1.LexAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Poliz {

    private static List<PostfixEntry> postfixEntries = new ArrayList<>();

    public static List<PostfixEntry> getPostfixEntries() {
        return postfixEntries;
    }

    public static int writeCmd(ECmd cmd) {
        PostfixEntry postfixEntry = new PostfixEntry(EEntryType.etCmd, findCmdNumber(cmd));
        postfixEntries.add(postfixEntry);
        return postfixEntries.size() - 1;
    }

    public static int writeVar(int ind) {
        PostfixEntry postfixEntry = new PostfixEntry(EEntryType.etVar, ind);
        postfixEntries.add(postfixEntry);
        return postfixEntries.size() - 1;
    }

    public static int writeConst(int ind) {
        PostfixEntry postfixEntry = new PostfixEntry(EEntryType.etConst, ind);
        postfixEntries.add(postfixEntry);
        return postfixEntries.size() - 1;
    }

    public static int writeCmdPtr(int ind) {
        PostfixEntry postfixEntry = new PostfixEntry(EEntryType.etCmdPtr, ind);
        postfixEntries.add(postfixEntry);
        return postfixEntries.size() - 1;
    }

    public static void setCmdPtr(int ind, int ptr) {
        postfixEntries.get(ind).setIndex(ptr);
    }

    public static String writePretty(LexAnalysis lexAnalysis) {
        StringBuilder result = new StringBuilder();
        for (PostfixEntry postfixEntry : postfixEntries) {
            if (postfixEntry.getType() == EEntryType.etVar || postfixEntry.getType() == EEntryType.etConst) {
                List<String> entry = lexAnalysis.getLexes().stream()
                        .filter(lex -> lex.getPos() == postfixEntry.getIndex())
                        .map(Lex::getContent)
                        .collect(Collectors.toList());
                result.append(entry.get(0));
            } else if (postfixEntry.getType() == EEntryType.etCmdPtr) {
                result.append(postfixEntry.getIndex());
            } else {
                result.append(findCmdByNumber(postfixEntry.getIndex()));
            }
            result.append(" ");
        }
        return result.toString();
    }

    private static int findCmdNumber(ECmd cmd) {
        int i = 0;
        for (ECmd cmd1 : ECmd.values()) {
            if (cmd1 != cmd) {
                i++;
            } else {
                return i;
            }
        }
        return 0;
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

}
