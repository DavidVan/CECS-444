import java.util.*;

public class Symbol {
    
    private int symbolId;
    private String symbolName;
    private boolean isTerminal;
    private Token token; // A symbol can reference a token.

    public Symbol(int symbolId, String symbolName, boolean isTerminal) {
        this.symbolId = symbolId;
        this.symbolName = symbolName;
        this.isTerminal = isTerminal;
    }

    public int getSymbolId() {
        return this.symbolId;
    }

    public String getSymbolName() {
        return this.symbolName;
    }

    public boolean isTerminal() {
        return this.isTerminal;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
    public Token getToken() {
        return this.token;
    }

}
