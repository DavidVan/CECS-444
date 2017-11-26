import java.util.*;
import java.util.stream.*;

public class Node {

    private int nodeId;
    private Symbol symbol;
    private Token token;
    private List<Node> children;

    public Node(int nodeId, Symbol symbol) {
        this.nodeId = nodeId;
        this.symbol = symbol;
        children = new ArrayList<>();
    }

    public Node(int nodeId, Symbol symbol, Token token) {
        this.nodeId = nodeId;
        this.symbol = symbol;
        this.token = token;
        children = new ArrayList<>();
    }

    public Node(int nodeId, Symbol symbol, Token token, List<Node> children) {
        this.nodeId = nodeId;
        this.symbol = symbol;
        this.token = token;
        this.children = children;
    }

    public int getNodeId() {
        return this.nodeId;
    }

    public Symbol getSymbol() {
        return this.symbol;
    }

    public Token getToken() throws Exception {
        if (this.token != null) {
            return this.token;
        }
        throw new Exception("Token not initialized!");
    }

    public void addToken(Token token) throws Exception {
        if (this.token != null) {
            throw new Exception("Token already initialized!");
        }
        this.token = token;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    @Override
    public String toString() {
        if (this.children.size() != 0) {
            List<Integer> ids = new ArrayList<>();
            for (Node c : this.children) {
                ids.add(c.getNodeId());
            }
            return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + " (Kid Ids: " + ids.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "))";
        }
        else if (this.token != null) {
            if (this.token.hasInt()) {
                try {
                    return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + " (Integer Value: " + this.token.getInt() + "))";
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (this.token.hasFloat()) {
                try {
                    return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + " (Float Value: " + this.token.getFloat() + "))";
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (this.token.getTokenId() == 2) {
                return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + " (Variable Name: " + this.token.getTokenStringName() + "))";
            }
            else if (this.token.getTokenId() == 5) {
                return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + " (String Value: " + this.token.getTokenStringName() + "))";
            }
        }
        return "(Node Id: " + this.nodeId + ", type: " + this.symbol.getSymbolName() + ")";
    }

}
