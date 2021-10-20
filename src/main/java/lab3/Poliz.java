package lab3;

import java.util.ArrayList;
import java.util.List;

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

}
