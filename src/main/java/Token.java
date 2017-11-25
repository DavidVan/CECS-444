import java.util.*;

public class Token {

    private static HashMap<Integer, String> tokenIdToName = new HashMap<>();
    
    static {
        tokenIdToName.put(2, "id");
        tokenIdToName.put(3, "int");
        tokenIdToName.put(4, "float");
        tokenIdToName.put(5, "string");
        tokenIdToName.put(6, "coma");
        tokenIdToName.put(7, "semi");
        tokenIdToName.put(10, "kprog");
        tokenIdToName.put(11, "kmain");
        tokenIdToName.put(12, "kfcn");
        tokenIdToName.put(13, "kclass");
        tokenIdToName.put(15, "kfloat");
        tokenIdToName.put(16, "kint");
        tokenIdToName.put(17, "kstring");
        tokenIdToName.put(18, "kif");
        tokenIdToName.put(19, "kelseif");
        tokenIdToName.put(20, "kelse");
        tokenIdToName.put(21, "kwhile");
        tokenIdToName.put(22, "kinput");
        tokenIdToName.put(23, "kprint");
        tokenIdToName.put(24, "knew");
        tokenIdToName.put(25, "kreturn");
        tokenIdToName.put(26, "kvars");
        tokenIdToName.put(31, "angle1");
        tokenIdToName.put(32, "angle2");
        tokenIdToName.put(33, "brace1");
        tokenIdToName.put(34, "brace2");
        tokenIdToName.put(35, "bracket1");
        tokenIdToName.put(36, "bracket2");
        tokenIdToName.put(37, "parens1");
        tokenIdToName.put(38, "parens2");
        tokenIdToName.put(41, "aster");
        tokenIdToName.put(42, "caret");
        tokenIdToName.put(43, "colon");
        tokenIdToName.put(44, "dot");
        tokenIdToName.put(45, "equal");
        tokenIdToName.put(46, "minus");
        tokenIdToName.put(47, "plus");
        tokenIdToName.put(48, "slash");
        tokenIdToName.put(51, "oparrow");
        tokenIdToName.put(52, "opeq");
        tokenIdToName.put(53, "opne");
        tokenIdToName.put(54, "ople");
        tokenIdToName.put(55, "opge");
        tokenIdToName.put(56, "opshl");
        tokenIdToName.put(57, "opshr");
        tokenIdToName.put(99, "error");
        tokenIdToName.put(0, "eof");
    }

    private int lineNumber; // Line number where token was located.
    private int tokenId; // Id of the token.
    private String tokenName; // Token Name can also be the contents of a String variable.
    private int mInteger;
    private boolean integerIsSet;
    private float mFloat;
    private boolean floatIsSet;

    public Token(int lineNumber, int tokenId, String tokenName) {
        this.lineNumber = lineNumber;
        this.tokenId = tokenId;
        this.tokenName = tokenName;
        this.integerIsSet = false;
        this.floatIsSet = false;
    }

    public void setInt(int anInteger) {
        this.mInteger = anInteger;
        this.integerIsSet = true;
    }

    public int getInt() throws Exception {
        if (this.integerIsSet == false) {
            throw new Exception("No integer value set for this token!");
        }
        return this.mInteger;
    }

    public void setFloat(float aFloat) {
        this.mFloat= aFloat;
        this.floatIsSet = true;
    }

    public float getFloat() throws Exception {
        if (this.floatIsSet == false) {
            throw new Exception("No float value set for this token!");
        }
        return this.mFloat;
    }

    public String getTokenName() {
        return Token.tokenIdToName.get(this.tokenId);
    }

    @Override
    public String toString() {
        if (integerIsSet) {
            return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\" int= " + mInteger + ")";
        }
        if (floatIsSet) {
            return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\" float= " + mFloat + ")";
        }
        return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\")";
    }

}
