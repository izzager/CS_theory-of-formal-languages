package lab1;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class LexAnalysis {

    private List<Lex> lexes = new ArrayList<>();

    public boolean analyze(String text) {
        int strIndex = 0;
        char str = text.charAt(strIndex);
        String lexema = "";
        strIndex++;

        EState state = EState.S;
        boolean add;

        while (state != EState.E && state != EState.F) {
            add = true;

            switch (state) {

                case S: {
                    if (str == ' ') {}
                    else if (isAlpha(str)) {
                        state = EState.Ai;
                    } else if (isDigit(str)) {
                        state = EState.Ac;
                    } else if (str == '<') {
                        state = EState.As;
                    } else if (str == '=') {
                        state = EState.Bs;
                    } else if (str == '+' || str == '-' || str == '*' || str == '/') {
                        state = EState.Cs;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                    }
                    add = false;
                    break;
                }

                case Ai: {
                    if (str == ' ') {
                        state = EState.S;
                    } else if (isAlpha(str) || isDigit(str)) {
                        add = false;
                    } else if (str == '<') {
                        state = EState.As;
                    } else if (str == '=') {
                        state = EState.Bs;
                    } else if (str == '+' || str == '-' || str == '*' ||str == '/') {
                        state = EState.Cs;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                        add = false;
                    }
                    break;
                }

                case Ac: {
                    if (str == ' ') {
                        state = EState.S;
                    } else if (isDigit(str)) {
                        add = false;
                    } else if (str == '<') {
                        state = EState.As;
                    } else if (str == '=') {
                        state = EState.Bs;
                    } else if (str == '+' || str == '-' || str == '*' ||str == '/') {
                        state = EState.Cs;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                        add = false;
                    }
                    break;
                }

                case As: {
                    if (str == ' ') {
                        state = EState.S;
                    } else if (isAlpha(str)) {
                        state = EState.Ai;
                    } else if (isDigit(str)) {
                        state = EState.Ac;
                    } else if (str == '=' || str == '>') {
                        state = EState.Ds;
                        add = false;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                        add = false;
                    }
                    break;
                }

                case Bs: {
                    if (str == ' ') {
                        state = EState.S;
                    } else if (isAlpha(str)) {
                        state = EState.Ai;
                    } else if (isDigit(str)) {
                        state = EState.Ac;
                    } else if (str == '=') {
                        state = EState.Gs;
                        add = false;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                        add = false;
                    }
                    break;
                }

                case Cs: case Ds: case Gs: {
                    if (str == ' ') {
                        state = EState.S;
                    } else if (isAlpha(str)) {
                        state = EState.Ai;
                    } else if (isDigit(str)) {
                        state = EState.Ac;
                    } else if (str == 0) {
                        state = EState.F;
                    } else {
                        state = EState.E;
                        add = false;
                    }
                    break;
                }
            } //end switch

            //System.out.println(str + ": " + prevState + " -> " + state + " add: " + add);
            if (add) {
                addLex(lexema, strIndex);
                lexema = "";
            }
            if (state == EState.Ai || state == EState.Ac || state == EState.As || state == EState.Bs || state == EState.Cs) {
                lexema = lexema + str;
            }
            if (state != EState.E && state != EState.F && strIndex < text.length()) {
                str = text.charAt(strIndex);
                strIndex++;
            } else {
                str = 0;
            }
        }

        return state == EState.F;
    }

    private void addLex(String lexema, int index) {
        if (lexema.equals("do")) {
            lexes.add(new Lex(ELexType.lDo, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("loop")) {
            lexes.add(new Lex(ELexType.lLoop, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("while")) {
            lexes.add(new Lex(ELexType.lWhile, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("input")) {
            lexes.add(new Lex(ELexType.lInput, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("output")) {
            lexes.add(new Lex(ELexType.lOutput, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("and")) {
            lexes.add(new Lex(ELexType.lAnd, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("or")) {
            lexes.add(new Lex(ELexType.lOr, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("<") || lexema.equals(">") || lexema.equals("<>") || lexema.equals("==")) {
            lexes.add(new Lex(ELexType.lRel, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("+") || lexema.equals("-") || lexema.equals("*") || lexema.equals("/")) {
            lexes.add(new Lex(ELexType.lAo, index - lexema.length() - 1, lexema));
        } else if (lexema.equals("=")) {
            lexes.add(new Lex(ELexType.lAs, index - lexema.length() - 1, lexema));
        } else {
            try {
                Integer.parseInt(lexema);
                lexes.add(new Lex(ELexType.lConst, index - lexema.length() - 1, lexema));
            } catch (NumberFormatException ex) {
                lexes.add(new Lex(ELexType.lVar, index - lexema.length() - 1, lexema));
            }
        }
    }

    public List<Lex> getLexes() {
        return lexes;
    }

    public List<Lex> getConsts() {
        return lexes.stream()
                .filter(lex -> lex.getELexType().equals(ELexType.lConst))
                .collect(Collectors.toList());
    }

    public List<Lex> getVars() {
        return lexes.stream()
                .filter(lex -> lex.getELexType().equals(ELexType.lVar))
                .collect(Collectors.groupingBy(Lex::getContent, LinkedHashMap::new, Collectors.toList()))
                .values()
                .stream()
                .map(lexList -> new Lex(ELexType.lVar, lexList.get(0).getPos(), lexList.get(0).getContent()))
                .collect(Collectors.toList());
    }

    private boolean isDigit(char str) {
        return str >= '0' && str <= '9';
    }

    private boolean isAlpha(char str) {
        return str >= 'a' && str <= 'z' || str >= 'A' && str <= 'Z';
    }


}
