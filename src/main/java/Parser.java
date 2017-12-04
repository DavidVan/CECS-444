import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                Stmt.addChild(simplifyNode(Stmts, parent));
                return simplifyNode(Stmt, parent); // TODO: Check this... I don't think we need to simplify here but if we don't get get a dangling Stmt node... If we do simplify, we don't get aster anymore... In fact, we don't get any of the tree past this point...
            }
            else if (symbolName.equals("Stmt")) {
                if (children.get(0).getChildren().size() != 0) {
                    Node childSymbol = simplifyNode(children.get(0), parent); // Stmt only has one child... Stasgn, Stprint, or Stwhile
                    return childSymbol;
                }
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
            else if (symbolName.equals("Fact")) {
                return children.get(0); // We only have one child for Fact nodes...
            }
            else if (symbolName.equals("PPexprs")) {
                Node parens1 = n.getChildWithName("parens1");
                Node Exprlist = n.getChildWithName("Exprlist");
                Node parens2 = n.getChildWithName("parens2");
                parens1.addChild(Exprlist);
                parens1.addChild(parens2);
                return parens1;
            }
            else if (symbolName.equals("Exprlist")) {
                Node Expr = n.getChildWithName("Expr");
                Node Moreexprs = n.getChildWithName("Moreexprs");
                Expr.addChild(simplifyNode(Moreexprs, parent));
                return simplifyNode(Expr, parent);
            }
            else if (symbolName.equals("Moreexprs")) {
                if (n.getChildWithName("comma") != null) {
                    Node Exprlist = n.getChildWithName("Exprlist");
                    return Exprlist;
                }
            }
            else if (symbolName.equals("Lthan")) {
                return n.getChildWithName("angle1");
            }
            else if (symbolName.equals("Gthan")) {
                return n.getChildWithName("angle2");
            }
            else if (symbolName.equals("Opadd")) {
                return children.get(0);
            }
            else if (symbolName.equals("Opmul")) {
                return children.get(0);
            }
            else if (symbolName.equals("Oprel")) {
                return children.get(0);
            }
        }
        return n;
    }



//Pat's implementation
    public void ast2sct(Node arn, ScopeNode rsn){
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
    public void a2sBlock(Node arn, ScopeNode rsnParent){
        ScopeNode sKid = new ScopeNode(rsnParent);
        rsnParent.linkParentToChild(sKid);
        for(Node kid: arn.getChildren()){
            ast2sct(kid, sKid);
        }
    }

    //If it's a decl then add to the entry
    public void astUse(Node arn, ScopeNode rsn){
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
    
    public void runAST(Node rn, ScopeNode sct) {
       List<Node> kids = rn.getChildren();
       for (Node k : kids) {
          try {
             runASTHelper(k, sct);
             runAST(k, sct);
          } catch (Exception ex) {
             //Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
          }
       }
    }
    public Object runASTHelper(Node rn, ScopeNode sct) throws Exception {
       if (rn == null) {
          return null;
       }
       
       // get the type of the node
       String typeOfNode = rn.getSymbol().getSymbolName();
       
       switch(typeOfNode) {
          case "id":
              return runASTID(rn);
              //break;
          case "equal":
              runASTEqual(rn, sct);
              break;
          case "int":
              return runASTInt(rn);
              //break;
          case "float":
              return runASTFloat(rn);
              //break;
          case "kprint":
              runASTPrint(rn);
              break;
          case "while":
              runASTWhile(rn, sct);
              break;
          default:
              break;
       }
       return null;
    }

    public void runASTEqual(Node rn, ScopeNode sct) {
          // get symbols and tokens of left and right child for equal
          Node rx = rn.getChildren().get(1);
          Node rz = rn.getChildren().get(0);
          Token t = rz.getSymbol().getToken();
          Token v = rx.getSymbol().getToken();

        String symName = rx.getSymbol().getSymbolName();
        String type;
        
        // check if the symbol is an int
        if(symName.equals("int")){
           type = "int";
           setSCTvalue(symName, t,v, type, sct);
        }
        
        // check if the symbol is a float
        else if (symName.equals("float")){
           type = "float";
           setSCTvalue(symName, t,v, type, sct);
        }
        
        // else its a string
        else{
           type = "string";
           setSCTvalue(symName, t,v, type, sct);
        }
    }
    
    public void setSCTvalue(String entry, Token t, Token v, String type, ScopeNode sct) {

       while(sct != null) {
          if (sct.getSCTMap().containsKey(entry)) {

             if (type.equals("int")) {
                try {
                   // if its int put int object inside
                   sct.getValMap().put(t.getTokenStringName(), v.getInt());
                } catch (Exception ex) {

                }
             }
             else if (type.equals("float")) {
                System.out.println("ENTERED FLOAT");
                try {
                  sct.getValMap().put(t.getTokenStringName(), v.getFloat());
                } catch (Exception ex) {

                }
             }
             else {
                sct.getValMap().put(t.getTokenStringName(), v.getTokenStringName());
             }
          }
          sct = sct.getKid();
       }
    }
    
    public Token runASTID(Node rn){
        Token t = null;
        try {
            t = rn.getToken();
        } catch (Exception ex) {

        }
        return t;
    }
    
    public Integer runASTInt(Node rn){
        Integer intFromToken = null;
        if(rn.getSymbol().getToken()==null){
            return null;
        }
        if(rn.getSymbol().getToken().hasInt()){
            try {
                intFromToken = new Integer(rn.getSymbol().getToken().getInt());
            } catch (Exception ex) {

            }
        }
        else{
            System.out.println("Pat"+rn.getSymbol().getToken());
        }
        return intFromToken;
    }

    public Float runASTFloat(Node rn){
        Float floatFromToken = null;
        if (rn.getSymbol().getToken()==null) {
            return null;
        }
        if(rn.getSymbol().getToken().hasFloat()){
            try {
                floatFromToken = new Float(rn.getSymbol().getToken().getFloat());
            } catch (Exception ex) {

            }
        }
        else{
            System.out.println("Pat"+rn.getSymbol().getToken());
        }
        return floatFromToken;
    }
    
    public void runASTPrint(Node rn) {
       
      try {
         boolean hasComma;
         Token rx;
         // check if comma exists
         Node comma = rn.getChildren().get(0).getChildren().get(0);
          
         if (comma != null && comma.getSymbol().getSymbolName().equals("comma")) {
            hasComma = true;
            rx = comma.getChildren().get(0).getToken();
         }
         else {
            hasComma = false;
            rx = rn.getChildren().get(0).getChildren().get(0).getToken();
          }
          
         do {
            if (rx.hasInt()) {
               try {
                  int intValue = rx.getInt();
                  System.out.println(intValue);
               } catch (Exception ex) {

               }
            }
            else if (rx.hasFloat()) {
               try {
                  float floatValue = rx.getFloat();
                  System.out.println(floatValue);
               } catch (Exception ex) {

               }
            }
            else {
               String stringValue = rx.getTokenStringName();
               System.out.println(stringValue);
            }
            
            // if we are on a comma, check for another comma
            if (hasComma) {
               
               comma = comma.getChildren().get(1);
               if (comma == null) {
                  hasComma = false;
               }
            }
         }
         while(hasComma);
          
       } catch (Exception ex) {

       }
    }
    
    public void runASTWhile(Node rn, ScopeNode sct){
        boolean condition = runConditional(rn.getChildren().get(0), sct);
        if(condition){
            isBlock(rn.getChildren().get(1));
        }
    }
    
    public boolean runConditional(Node rn, ScopeNode sct){
        if(rn.getSymbol().getSymbolName().equals("<")){
            try {
                return runASTChild1(rn, sct);
            } catch (Exception ex) {
            }
        }
        else if(rn.getSymbol().getSymbolName().equals(">")){
            try {
                return runASTAngle2(rn, sct);
            } catch (Exception ex) {
            }
        }
        else if(rn.getSymbol().getSymbolName().equals("==")){
            return runASTDoubleEqual(rn, sct);
        }
        return false;
    }
    
    public boolean runASTChild1(Node rn, ScopeNode sct) throws Exception{
        try {
            Object child1 = runASTHelper(rn.getChildren().get(0), sct); 
            Object child2 = runASTHelper(rn.getChildren().get(1), sct);
            
            if(child1 instanceof Integer){
                return (Integer) child1 < (Integer) child2;
            }
            else if(child1 instanceof Float){
                return (Float) child1 < (Float) child2;
            }
        } catch (Exception ex) {
        }
        return false;
    }
    public boolean runASTAngle2(Node rn, ScopeNode sct) throws Exception{
        try {
            Object child1 = runASTHelper(rn.getChildren().get(0), sct); 
            Object child2 = runASTHelper(rn.getChildren().get(1), sct);
            
            if(child1 instanceof Integer){
                return (Integer) child1 > (Integer) child2;
            }
            else if(child1 instanceof Float){
                return (Float) child1 > (Float) child2;
            }
        } catch (Exception ex) {
        }
        return false;
    }
    public boolean runASTDoubleEqual(Node rn, ScopeNode sct){
        try {
            Object child1 = runASTHelper(rn.getChildren().get(0), sct); 
            Object child2 = runASTHelper(rn.getChildren().get(1), sct);
            
            if(child1 instanceof Integer){
                return (Integer) child1 == (Integer) child2;
            }
            else if(child1 instanceof Float){
                return (Float) child1 == (Float) child2;
            }
            
        } catch (Exception ex) {
        }
        return false;
    }
}


