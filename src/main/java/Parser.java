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

    public Node parseToAST(Node parseTree, Node parent) {
        Node fixedNode = simplifyNode(parseTree, parent); // Fix the node
        List<Node> children = fixedNode.getChildren();
        if (children.size() == 0) {
            return fixedNode;
        }
        List<Node> newChildren = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            Node c = children.get(i);
            if (c != fixedNode) {
                if (c.getSymbol().getSymbolName().equals("Rterm")) {
                    System.out.println("ENCOUNTERED RTERM NODE..." + c);
                }
                System.out.println("Processing: " + c);
                Node fixedChildNode = parseToAST(c, fixedNode);
                newChildren.add(fixedChildNode);
                System.out.println("Fixed Node: " + fixedChildNode);
            }
        }
        children.clear(); // Clear old children list...
        for (Node nC : newChildren) {
            children.add(nC); // Add new children...
        }
        return fixedNode;
    }

    private Node simplifyNode(Node n, Node parent) {
        List<Node> children = n.getChildren();
        if (children.size() != 0) {
            String symbolName = n.getSymbol().getSymbolName();
            if (symbolName.equals("Pgm")) {
                Node kwdprog = n.getChildWithName("kwdprog");
                Node BBlock = n.getChildWithName("BBlock");
                kwdprog.addChild(BBlock);
                return kwdprog;
            }
            else if (symbolName.equals("BBlock")) {
                Node brace1 = n.getChildWithName("brace1");
                // Node Vargroup = n.getChildWithName(1);
                Node Stmts = n.getChildWithName("Stmts");
                Node brace2 = n.getChildWithName("brace2");
                // brace1.addChild(Vargroup);
                // Special case for Stmts... We don't replace brace1 with Stmt's childs... We add them as childs of brace1.
                Node StmtsChildStmt = Stmts.getChildWithName("Stmt"); // Get the Stmt node.
                // We skip getting the semi node.
                Node StmtsChildStmts = Stmts.getChildWithName("Stmts"); // Get the Stmts node.
                brace1.addChild(StmtsChildStmt);
                brace1.addChild(StmtsChildStmts);
                brace1.addChild(brace2);
                return brace1;
            }
            else if (symbolName.equals("Stmts")) {
                Node Stmt = n.getChildWithName("Stmt");
                // Node semi = n.getChildWithName("semi");
                Node Stmts = n.getChildWithName("Stmts");
                // Stmt.addChild(semi);
                Stmt.addChild(Stmts);
                return Stmt;
            }
            else if (symbolName.equals("Stmt")) {
                Node childSymbol = simplifyNode(children.get(0), parent); // Stmt only has one child... Stasgn, Stprint, or Stwhile
                return childSymbol;
            }
            else if (symbolName.equals("Stasgn")) {
                Node Varid = n.getChildWithName("Varid");
                Node equal = n.getChildWithName("equal");
                Node Expr = n.getChildWithName("Expr");
                equal.addChild(Varid);
                equal.addChild(Expr);
                return equal;
            }
            else if (symbolName.equals("Stprint")) {
                Node kwdprint = n.getChildWithName("kwdprint");
                Node PPexprs = n.getChildWithName("PPexprs");
                kwdprint.addChild(PPexprs);
                return kwdprint;
            }
            else if (symbolName.equals("Stwhile")) {
                Node kwdwhile = n.getChildWithName("kwdwhile");
                Node PPexprl = n.getChildWithName("PPexprl");
                Node BBlock = n.getChildWithName("BBlock");
                kwdwhile.addChild(PPexprl);
                kwdwhile.addChild(BBlock);
                return kwdwhile;
            }
            else if (symbolName.equals("Varid")) {
                Node id = n.getChildWithName("id");
                return id;
            }
            else if (symbolName.equals("Expr")) {
                if (n.getChildWithName("Oprel") != null) {
                    Node Oprel = n.getChildWithName("Oprel");
                    Node Rterm = n.getChildWithName("Rterm");
                    Node E = n.getChildWithName("E");
                    Oprel.addChild(Rterm);
                    if (E.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(E, parent));
                    }
                    return Oprel;
                }
                else {
                    Node Rterm = n.getChildWithName("Rterm");
                    Node E = n.getChildWithName("E");
                    if (E.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(E, parent));
                    }
                    return simplifyNode(Rterm, parent);
                }
            }
            else if (symbolName.equals("Rterm")) {
                if (n.getChildWithName("Opadd") != null) {
                    Node Opadd = n.getChildWithName("Opadd");
                    Node Term = n.getChildWithName("Term");
                    Node R = n.getChildWithName("R");
                    Opadd.addChild(Term);
                    if (R.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(R, parent));
                    }
                    return Opadd;
                }
                else {
                    Node Term = n.getChildWithName("Term");
                    Node R = n.getChildWithName("R");
                    if (R.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(R, parent));
                    }
                    return simplifyNode(Term, parent);
                }
            }
            else if (symbolName.equals("Term")) {
                if (n.getChildWithName("Opmul") != null) {
                    Node Opmul = n.getChildWithName("Opmul");
                    Node Fact = n.getChildWithName("Fact");
                    Node T = n.getChildWithName("T");
                    Opmul.addChild(Fact);
                    if (T.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(T, parent));
                    }
                    return Opmul;
                }
                else {
                    Node Fact = n.getChildWithName("Fact");
                    Node T = n.getChildWithName("T");
                    if (T.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(T, parent));
                    }
                    return simplifyNode(Fact, parent);
                }
            }
            else if (symbolName.equals("E")) {
                if (n.getChildWithName("Oprel") != null) {
                    Node Oprel = n.getChildWithName("Oprel");
                    Node Rterm = n.getChildWithName("Rterm");
                    Node E = n.getChildWithName("E");
                    Oprel.addChild(Rterm);
                    if (E.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(E, parent));
                    }
                    return Oprel;
                }
                else {
                    Node Rterm = n.getChildWithName("Rterm");
                    Node E = n.getChildWithName("E");
                    if (E.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(E, parent));
                    }
                    return simplifyNode(Rterm, parent);
                }
            }
            else if (symbolName.equals("R")) {
                if (n.getChildWithName("Opadd") != null) {
                    Node Opadd = n.getChildWithName("Opadd");
                    Node Term = n.getChildWithName("Term");
                    Node R = n.getChildWithName("R");
                    Opadd.addChild(Term);
                    if (R.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(R, parent));
                    }
                    return Opadd;
                }
                else {
                    Node Term = n.getChildWithName("Term");
                    Node R = n.getChildWithName("R");
                    if (R.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(R, parent));
                    }
                    return simplifyNode(Term, parent);
                }
            }
            else if (symbolName.equals("T")) {
                if (n.getChildWithName("Opmul") != null) {
                    Node Opmul = n.getChildWithName("Opmul");
                    Node Fact = n.getChildWithName("Fact");
                    Node T = n.getChildWithName("T");
                    Opmul.addChild(Fact);
                    if (T.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(T, parent));
                    }
                    return Opmul;
                }
                else {
                    Node Fact = n.getChildWithName("Fact");
                    Node T = n.getChildWithName("T");
                    if (T.getChildren().size() != 0) {
                        parent.addChild(simplifyNode(T, parent));
                    }
                    return simplifyNode(Fact, parent);
                }
            }
            

            // else if (symbolName.equals("Expr")) {
            //     Node Rterm = n.getChildWithName("Rterm");
            //     Node E = n.getChildWithName("E");
            //     Rterm.addChild(Rterm.getChildWithName("Term"));
            //     if (E.getChildren().size() != 0) {
            //         Rterm.addChild(E);
            //     }
            //     return Rterm;
            // }
            // else if (symbolName.equals("Rterm")) {
            //     Node Term = n.getChildWithName("Term");
            //     Node R = n.getChildWithName("R");
            //     Term.addChild(Term.getChildWithName("Fact"));
            //     if (R.getChildren().size() != 0) {
            //         Term.addChild(R);
            //     }
            //     return Term;
            // }
            // else if (symbolName.equals("Term")) {
            //     Node Fact = n.getChildWithName("Fact");
            //     Node T = n.getChildWithName("T");
            //     if (n.getSymbol().isTerminal()) {
            //         Fact.addChild(children.get(0));
            //     }
            //     else {
            //         Fact.addChild(children.get(0));
            //     }
            //     if (T.getChildren().size() != 0) {
            //         Fact.addChild(T);
            //     }
            //     return Fact;
            // }
            else if (symbolName.equals("")) {

            }
        }
        return n;
    }

}
