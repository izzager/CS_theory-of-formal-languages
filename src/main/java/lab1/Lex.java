package lab1;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Lex {

    private ELexCategory eLexCategory;

    private ELexType eLexType;

    private int pos;

    private String content;

    @Override
    public String toString() {
        return "Lex{" + "eLexCategory=" + eLexCategory +", eLexType="
                + eLexType + ", pos=" + pos + ", content='" + content + '\'' + '}';
    }
}
