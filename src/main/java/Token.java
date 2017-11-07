import java.util.*;

public class Token {

    private int lineNumber; // Line number where token was located.
    private int tokenId; // Id of the token.
    private String tokenName; // Token Name can also be the contents of a String variable.
    private int mInteger;
    private boolean integerIsSet;
    private float mFloat;
    private boolean IsSet;

    public Token(int lineNumber, int tokenId, String tokenName) {
        this.lineNumber = lineNumber;
        this.tokenId = tokenId;
        this.tokenName = tokenName;
        this.integerIsSet = false;
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
        this.IsSet = true;
    }

    public float getFloat() throws Exception {
        if (this.IsSet == false) {
            throw new Exception("No float value set for this token!");
        }
        return this.mFloat;
    }

    @Override
    public String toString() {
        if (integerIsSet) {
            return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\" int= " + mInteger + ")";
        }
        if (IsSet) {
            return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\" float= " + mFloat + ")";
        }
        return "(Tok: " + tokenId + " line= " + lineNumber + " str= \"" + tokenName + "\")";
    }

}
