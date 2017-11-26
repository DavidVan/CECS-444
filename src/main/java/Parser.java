import java.util.*;

public class Parser {
    
    private LLTable table = new LLTable();
    private List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Node parse() throws Exception { // Returns the PST.
        int count = 0;

        Stack<Symbol> stack = new Stack<>();
        Queue<Token> queue = new LinkedList<>(tokens);

        Symbol endingSymbol = new Symbol(0, "$", true);
        Symbol startingSymbol = new Symbol(-1, "Pgm", false);

        Stack<Node> nodeStack = new Stack<>();

        Node tree = new Node(++count, startingSymbol);

        stack.push(endingSymbol);
        stack.push(startingSymbol);

        nodeStack.push(tree);

        while (!stack.isEmpty()) {
            Symbol currentSymbol = stack.peek();
            Token currentToken = queue.peek();
            if (currentSymbol.isTerminal()) { // Rule m1: Looks like we have a terminal symbol. Time to check if they match!
                if (currentSymbol.getSymbolId() == currentToken.getTokenId()) {
                    Symbol poppedSymbol = stack.pop();
                    Token removedToken = queue.poll();
                    // Added to generate PST...
                    if (currentToken.getTokenId() != 0) {
                        Node associatedNode = nodeStack.pop();
                        associatedNode.addToken(removedToken);
                    }
                    // End Add
                }
                else { // Rule m2: Something does not match...
                    throw new Exception("Error during parsing. Symbol and token do not match.");
                }
            }
            else { // Looks like we have a non-terminal symbol. Time to expand!
                try { // Rule m4: Time to expand stuff!
                    // Empty list means epsilon rule was used...
                    List<Symbol> expandedSymbol = table.expandSymbolForToken(currentSymbol, currentToken);
                    Collections.reverse(expandedSymbol);
                    Symbol poppedSymbol = stack.pop(); // Remove current symbol...
                    // Add in the new symbols... 
                    for (Symbol s : expandedSymbol) {
                        stack.push(s);
                    }
                    // Added to generate PST...
                    Collections.reverse(expandedSymbol); // Have to reverse it again because it needs to be attached to the node in order...
                    Node mom = nodeStack.pop();
                    List<Node> kids = new ArrayList<>();
                    for (Symbol s: expandedSymbol) {
                        Node kid = new Node(++count, s);
                        mom.addChild(kid);
                        kids.add(kid);
                    }
                    Collections.reverse(kids);
                    for (Node k : kids) {
                        nodeStack.push(k);
                    }
                    // End Add
                }
                catch (Exception e) { // Rule m3: No rule for current symbol and token...
                    e.printStackTrace();
                    throw new Exception("Error during parsing. Table does not contain a rule for the given symbol and token.");
                }
            }
            // System.out.println("Processed Symbol: " + currentSymbol.getSymbolName() + " (id: " + currentSymbol.getSymbolId() + ") and Token: " + currentToken.getTokenName() + " (id: " + currentToken.getTokenId() + ")");
        }
        return tree;
    }

}
