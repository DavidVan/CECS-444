import java.util.*;

public class Parser {

    private LLTable table = new LLTable();
    private List<Token> tokens;

    public Parser(){

    }

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

    public Node parse2AST(Node parseTree) {
        return simplifyNode(parseTree);
    }

    public Node simplifyNode(Node n) {
        List<Node> children = n.getChildren();
        if (children.size() == 0) {
            return null;
        }
        else {
            String symbolName = n.getSymbol().getSymbolName();
            if (symbolName.equals("Pgm")) {
                Node kwdprog = n.getChildWithName("kwdprog");
                Node BBlock = simplifyNode(n.getChildWithName("BBlock"));
                kwdprog.addChild(BBlock);
                return kwdprog;
            }
            else if (symbolName.equals("BBlock")) {
                Node brace1 = n.getChildWithName("brace1");
                Node Vargroup = simplifyNode(n.getChildWithName("Vargroup"));
                Node Stmts = simplifyNode(n.getChildWithName("Stmts"));
                Node brace2 = n.getChildWithName("brace2");
                if (Vargroup != null) {
                    brace1.addChild(Vargroup);
                }
                brace1.addChild(Stmts);
                brace1.addChild(brace2);
                return brace1;
            }
            else if (symbolName.equals("Vargroup")) {
                Node kwdvars = n.getChildWithName("kwdvars");
                Node PPvarlist = simplifyNode(n.getChildWithName("PPvarlist"));
                kwdvars.addChild(PPvarlist);
                return kwdvars;
            }
            else if (symbolName.equals("PPVarlist")) {
                Node parens1 = n.getChildWithName("parens1");
                Node Varlist = simplifyNode(n.getChildWithName("Varlist"));
                Node parens2 = n.getChildWithName("parens2");
                if (Varlist != null) {
                    parens1.addChild(Varlist);
                }
                parens1.addChild(parens2);
                return parens1;
            }
            else if (symbolName.equals("Varlist")) {
                Node Vardecl = simplifyNode(n.getChildWithName("Vardecl"));
                Node semi = n.getChildWithName("semi");
                Node Varlist = simplifyNode(n.getChildWithName("Varlist"));
                Vardecl.addChild(semi);
                if (Varlist != null) {
                    Vardecl.addChild(Varlist);
                }
                return Vardecl;
            }
            else if (symbolName.equals("Vardecl")) {
                Node Basekind = simplifyNode(n.getChildWithName("Basekind"));
                Node Varid = simplifyNode(n.getChildWithName("Varid"));
                Basekind.addChild(Varid);
                return Basekind;
            }
            else if (symbolName.equals("Basekind")) {
                Node childNode = children.get(0);
                return childNode;
            }
            else if (symbolName.equals("Varid")) {
                Node id = n.getChildWithName("id");
                return id;
            }
            else if (symbolName.equals("Stmts")) {
                Node Stmt = simplifyNode(n.getChildWithName("Stmt"));
                Node semi = n.getChildWithName("semi");
                Node Stmts = simplifyNode(n.getChildWithName("Stmts"));
                if (Stmts != null) {
                    Stmt.addChild(Stmts);
                }
                return Stmt;
            }
            else if (symbolName.equals("Stmt")) {
                Node childNode = simplifyNode(children.get(0));
                return childNode;
            }
            else if (symbolName.equals("Stasgn")) {
                Node Varid = simplifyNode(n.getChildWithName("Varid"));
                Node equal = n.getChildWithName("equal");
                Node Expr = simplifyNode(n.getChildWithName("Expr"));
                equal.addChild(Varid);
                equal.addChild(Expr);
                return equal;
            }
            if (symbolName.equals("Stprint")) {
                Node kwdprint = n.getChildWithName("kwdprint");
                Node PPexprs = simplifyNode(n.getChildWithName("PPexprs"));
                kwdprint.addChild(PPexprs);
                return kwdprint;
            }
            else if (symbolName.equals("Stwhile")) {
                Node kwdwhile = n.getChildWithName("kwdwhile");
                Node PPexprl = simplifyNode(n.getChildWithName("PPexprl"));
                Node BBlock = simplifyNode(n.getChildWithName("BBlock"));
                kwdwhile.addChild(PPexprl);
                kwdwhile.addChild(BBlock);
                return kwdwhile;
            }
            else if (symbolName.equals("PPexprl")) {
                Node parens1 = n.getChildWithName("parens1");
                Node Expr = simplifyNode(n.getChildWithName("Expr"));
                Node parens2 = n.getChildWithName("parens2");
                parens1.addChild(Expr);
                parens1.addChild(parens2);
                return parens1;
            }
            else if (symbolName.equals("PPexprs")) {
                Node parens1 = n.getChildWithName("parens1");
                Node Exprlist = simplifyNode(n.getChildWithName("Exprlist"));
                Node parens2 = n.getChildWithName("parens2");
                parens1.addChild(Exprlist);
                parens1.addChild(parens2);
                return parens1;
            }
            else if (symbolName.equals("Exprlist")) {
                Node Expr = simplifyNode(n.getChildWithName("Expr"));
                Node Moreexprs = simplifyNode(n.getChildWithName("Moreexprs"));
                if (Moreexprs != null) {
                    Expr.addChild(Moreexprs);
                }
                return Expr;
            }
            else if (symbolName.equals("Moreexprs")) {
                Node comma = n.getChildWithName("comma");
                Node Exprlist = simplifyNode(n.getChildWithName("Exprlist"));
                comma.addChild(Exprlist);
                return comma;
            }
            else if (symbolName.equals("Expr") || symbolName.equals("E")) {
                if (n.getChildWithName("Oprel") != null) {
                    Node Oprel = simplifyNode(n.getChildWithName("Oprel"));
                    Node Rterm = simplifyNode(n.getChildWithName("Rterm"));
                    Node E = simplifyNode(n.getChildWithName("E"));
                    Oprel.addChild(Rterm);
                    if (E != null) {
                        //if (E.getChildren().size() > 0) {
                        //    E.addChild(Rterm);
                        //    return E;
                        //}
                        //else {
                        //    Oprel.addChild(E);
                        //}
                        Oprel.addChild(E);
                    }
                    return Oprel;
                }
                else {
                    Node Rterm = simplifyNode(n.getChildWithName("Rterm"));
                    Node E = simplifyNode(n.getChildWithName("E"));
                    if (E != null) {
                        if (E.getChildren().size() > 0) {
                            E.addChild(Rterm);
                            return E;
                        }
                        else {
                            Rterm.addChild(E);
                        }
                    }
                    return Rterm;
                }
            }
            else if (symbolName.equals("Rterm") || symbolName.equals("R")) {
                if (n.getChildWithName("Opadd") != null) {
                    Node Opadd = simplifyNode(n.getChildWithName("Opadd"));
                    Node Term = simplifyNode(n.getChildWithName("Term"));
                    Node R = simplifyNode(n.getChildWithName("R"));
                    Opadd.addChild(Term);
                    if (R != null) {
                        //if (R.getChildren().size() > 0) {
                        //    R.addChild(Term);
                        //    return R;
                        //}
                        //else {
                        //    Opadd.addChild(R);
                        //}
                        Opadd.addChild(R);
                    }
                    return Opadd;
                }
                else {
                    Node Term = simplifyNode(n.getChildWithName("Term"));
                    Node R = simplifyNode(n.getChildWithName("R"));
                    if (R != null) {
                        if (R.getChildren().size() > 0) {
                            R.addChild(Term);
                            return R;
                        }
                        else {
                            Term.addChild(R);
                        }
                    }
                    return Term;
                }
            }
            else if (symbolName.equals("Term") || symbolName.equals("T")) {
                if (n.getChildWithName("Opmul") != null) {
                    Node Opmul = simplifyNode(n.getChildWithName("Opmul"));
                    Node Fact = simplifyNode(n.getChildWithName("Fact"));
                    Node T = simplifyNode(n.getChildWithName("T"));
                    Opmul.addChild(Fact);
                    if (T != null) {
                        //if (T.getChildren().size() > 0) {
                        //    T.addChild(Fact);
                        //    return T;
                        //}
                        //else {
                        //    Opmul.addChild(T);
                        //}
                        Opmul.addChild(T);
                    }
                    return Opmul;
                }
                else {
                    Node Fact = simplifyNode(n.getChildWithName("Fact"));
                    Node T = simplifyNode(n.getChildWithName("T"));
                    if (T != null) {
                        if (T.getChildren().size() > 0) {
                            T.addChild(Fact);
                            return T;
                        }
                        else {
                            Fact.addChild(T);
                        }
                    }
                    return Fact;
                }
            }
            else if (symbolName.equals("Fact")) {
                Node childNode = children.get(0);
                if (childNode.getSymbol().isTerminal()) {
                    return childNode;
                }
                else {
                    return simplifyNode(childNode);
                }
            }
            else if (symbolName.equals("Oprel")) {
                Node childNode = children.get(0);
                if (childNode.getSymbol().isTerminal()) {
                    return childNode;
                }
                else {
                    return simplifyNode(childNode);
                }
            }
            else if (symbolName.equals("Lthan")) {
                Node childNode = children.get(0);
                return childNode;
            }
            else if (symbolName.equals("Gthan")) {
                Node childNode = children.get(0);
                return childNode;
            }
            else if (symbolName.equals("Opadd")) {
                Node childNode = children.get(0);
                return childNode;
            }
            else if (symbolName.equals("Opmul")) {
                Node childNode = children.get(0);
                return childNode;
            }
            else {
                System.out.println("Check This: " + n);
                return n;
            }
        }
    }

    //Pat's implementation
    public void ast2sct(Node arn, SNode rsn){
        if(arn == null){
            return;
        }
        if(isBlock(arn)){
            a2sBlock(arn, rsn);
        }
        else if (isUse(arn)){
            astUse(arn, rsn);
        }
        else{
            for (Node k : arn.getChildren()) {
                ast2sct(k, rsn);
            }
        }


    }

    //Check if the AST current symbol is left bracket
    //WORKS
    public boolean isBlock(Node arn){
        String st = arn.getSymbol().getSymbolName();
        if(st.equals("brace1"))
            return true;
        else
            return false;
    }

    //Check if the AST current symbol is an ID type
    public boolean isUse(Node arn){

        String st = arn.getSymbol().getSymbolName();
        if(st.equals("id"))
            return true;
        else
            return false;

    }

    //Create a new scope
    public void a2sBlock(Node arn, SNode rsnParent){
        SNode sKid = new SNode(rsnParent);
        rsnParent.linkParentToChild(sKid);
        for(Node kid: arn.getChildren()){
            ast2sct(kid, sKid);
        }
    }

    //If it's a decl then add to the entry
    public void astUse(Node arn, SNode rsn){
        Map<String, Node> sMap = rsn.getSCTMap();
        try{
            String tokenName = arn.getToken().getTokenStringName();
            while(rsn.getParent()!=null){
                Map<String, Node> pMap = rsn.getParent().getSCTMap();

                if(pMap.containsKey(tokenName)){
                    //Do
                    pMap.put(tokenName,arn);
                    break;
                }
                else{
                    rsn = rsn.getParent();
                }

            }
            sMap.put(tokenName, arn);
        }
        catch(Exception e){};
        if (arn.getChildren().size() > 1) {
            Node rKid = arn.getChildren().get(1);
            if(rKid != null && isUse(rKid)){

                astUse(rKid, rsn);
            }
        }
    }
    //End Pat's implementation

}
