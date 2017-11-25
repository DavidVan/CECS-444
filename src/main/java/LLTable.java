import java.util.*;

public class LLTable {

    // Since the "table"/"matrix" will be "sparse" (mostly empty), we
    // will opt to give back symbols based on the current symbol and
    // token.
    
    public LLTable() {

    }

    // We will have 52 rules...
    public List<Symbol> expandSymbolForToken(Symbol symbol, Token token) throws Exception {
        String symbolName = symbol.getSymbolName();
        String tokenName = token.getTokenName();
        // Simple switch block to determine what we get back...
        // A function will return a list of symbols.
        // If the list is empty, that means it's an epsilon rule.
        // If an exception is thrown, there was no rule for a given
        // symbol and token.
        switch (symbolName) {
            case "Pgm":
                return getPgmSymbols(token);
            case "BBlock":
                return getBBlockSymbols(token);
            case "Vargroup":
                return getVargroupSymbols(token);
            case "PPVarlist":
                return getPPvarlistSymbols(token);
            case "Varlist":
                return getVarlistSymbols(token);
            case "Vardecl":
                return getVardeclSymbols(token);
            case "Basekind":
                return getBasekindSymbols(token);
            case "Varid":
                return getVaridSymbols(token);
            case "Stmts":
                return getStmtsSymbols(token);
            case "Stmt":
                return getStmtSymbols(token);
            case "Stasgn":
                return getStasgnSymbols(token);
            case "Stprint":
                return getStprintSymbols(token);
            case "Stwhile":
                return getStwhileSymbols(token);
            case "PPexprs":
                return getPPexprsSymbols(token);
            case "PPexprl":
                return getPPexprlSymbols(token);
            case "Exprlist":
                return getExprlistSymbols(token);
            case "Moreexprs":
                return getMoreexprsSymbols(token);
            case "E":
                return getESymbols(token);
            case "Expr":
                return getExprSymbols(token);
            case "R":
                return getRSymbols(token);
            case "Rterm":
                return getRtermSymbols(token);
            case "T":
                return getTSymbols(token);
            case "Term":
                return getTermSymbols(token);
            case "Fact":
                return getFactSymbols(token);
            case "Oprel":
                return getOprelSymbols(token);
            case "Lthan":
                return getLthanSymbols(token);
            case "Gthan":
                return getGthanSymbols(token);
            case "Opadd":
                return getOpaddSymbols(token);
            case "Opmul":
                return getOpmulSymbols(token);
            default:
                throw new Exception("Symbol not found in LLTable!");
        }
    }

    private List<Symbol> getPgmSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kprog": // Rule 1
                Symbol kwdprog = new Symbol(10, "kwdprog", false);
                Symbol BBlock = new Symbol(-1, "BBlock", true);
                results.add(kwdprog);
                results.add(BBlock);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getBBlockSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "brace1": // Rule 2
                Symbol brace1 = new Symbol(33, "brace1", false);
                Symbol Vargroup = new Symbol(-1, "Vargroup", true);
                Symbol Stmts = new Symbol(-1, "Stmts", true);
                Symbol brace2 = new Symbol(34, "brace2", false);
                results.add(brace1);
                results.add(Vargroup);
                results.add(Stmts);
                results.add(brace2);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getVargroupSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kvars": // Rule 3
                Symbol kwdvars = new Symbol(26, "kwdvars", false);
                Symbol PPvarlist = new Symbol(-1, "PPvarlist", true);
                results.add(kwdvars);
                results.add(PPvarlist);
                return results;
            case "brace2": // Rule 47
                return results;
            case "id": // Rule 47
                return results;
            case "kprint": // Rule 47
                return results;
            case "kwhile": // Rule 47
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getPPvarlistSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 4
                Symbol parens1 = new Symbol(37, "parens1", false);
                Symbol Varlist = new Symbol(-1, "Varlist", true);
                Symbol parens2 = new Symbol(38, "parens2", false);
                results.add(parens1);
                results.add(Varlist);
                results.add(parens2);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getVarlistSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kint": // Rule 5 - Should fall through
            case "kfloat": // Rule 5 - Should fall through
            case "kstring": // Rule 5 - Should end here
                Symbol Vardecl = new Symbol(-1, "Vardecl", true);
                Symbol semi = new Symbol(7, "semi", false);
                Symbol Varlist = new Symbol(-1, "Varlist", true);
                results.add(Vardecl);
                results.add(semi);
                results.add(Varlist);
                return results;
            case "brace2": // Rule 48
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getVardeclSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kint": // Rule 6 - Should fall through
            case "kfloat": // Rule 6 - Should fall through
            case "kstring": // Rule 6 - Should end here
                Symbol Basekind = new Symbol(-1, "Basekind", true);
                Symbol Varid = new Symbol(-1, "Varid", true);
                results.add(Basekind);
                results.add(Varid);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getBasekindSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kint": // Rule 7
                Symbol kint = new Symbol(16, "kwdint", false);
                results.add(kint);
                return results;
            case "kfloat": // Rule 8
                Symbol kfloat = new Symbol(15, "kwdfloat", false);
                results.add(kfloat);
                return results;
            case "kstring": // Rule 9
                Symbol kstring = new Symbol(17, "kwdstring", false);
                results.add(kstring);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getVaridSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "id": // Rule 10
                Symbol id = new Symbol(2, "id", false);
                results.add(id);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getStmtsSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "id": // Rule 11 - Should fall through
            case "kprint": // Rule 11 - Should fall through
            case "kwhile": // Rule 11 - Should end here
                Symbol Stmt = new Symbol(-1, "Stmt", true);
                Symbol semi = new Symbol(7, "semi", false);
                Symbol Varlist = new Symbol(-1, "Varlist", true);
                results.add(Stmt);
                results.add(semi);
                results.add(Varlist);
                return results;
            case "brace2": // Rule 46
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getStmtSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "id": // Rule 12
                Symbol Stasgn = new Symbol(-1, "Stasgn", true);
                results.add(Stasgn);
                return results;
            case "kprint": // Rule 13
                Symbol Stprint = new Symbol(-1, "Stprint", true);
                results.add(Stprint);
                return results;
            case "kwhile": // Rule 14
                Symbol Stwhile = new Symbol(-1, "Stwhile", true);
                results.add(Stwhile);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getStasgnSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "id": // Rule 15
                Symbol Varid = new Symbol(-1, "Varid", true);
                Symbol equal = new Symbol(45, "equal", false);
                Symbol Expr = new Symbol(-1, "Expr", true);
                results.add(Varid);
                results.add(equal);
                results.add(Expr);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getStprintSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "": // Rule 16
                Symbol kprint = new Symbol(23, "kwdprint", false);
                Symbol PPexprs = new Symbol(-1, "PPexprs", true);
                results.add(kprint);
                results.add(PPexprs);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getStwhileSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "kwhile": // Rule 17
                Symbol kwdwhile = new Symbol(21, "kwdwhile", false);
                Symbol PPexprl = new Symbol(-1, "PPexprl", true);
                Symbol BBlock = new Symbol(-1, "BBlock", true);
                results.add(kwdwhile);
                results.add(PPexprl);
                results.add(BBlock);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getPPexprsSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 18
                Symbol parens1 = new Symbol(37, "parens1", false);
                Symbol Exprlist = new Symbol(-1, "Exprlist", true);
                Symbol parens2 = new Symbol(38, "parens2", false);
                results.add(parens1);
                results.add(Exprlist);
                results.add(parens2);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getPPexprlSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 19
                Symbol parens1 = new Symbol(37, "parens1", false);
                Symbol Expr = new Symbol(-1, "Expr", true);
                Symbol parens2 = new Symbol(38, "parens2", false);
                results.add(parens1);
                results.add(Expr);
                results.add(parens2);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getExprlistSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 20 - Should fall through
            case "kint": // Rule 20 - Should fall through
            case "kfloat": // Rule 20 - Should fall through
            case "kstring": // Rule 20 - Should fall through
            case "id": // Rule 20 - Should end here
                Symbol Expr = new Symbol(-1, "Expr", true);
                Symbol Moreexprs = new Symbol(-1, "Moreexprs", true);
                results.add(Expr);
                results.add(Moreexprs);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getMoreexprsSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "comma": // Rule 21
                Symbol comma = new Symbol(6, "comma", false);
                Symbol Exprlist = new Symbol(-1, "Exprlist", true);
                results.add(comma);
                results.add(Exprlist);
                return results;
            case "brace2": // Rule 49
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getESymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "opeq": // Rule 22 - Should fall through
            case "opne": // Rule 22 - Should fall through
            case "ople": // Rule 22 - Should fall through
            case "opge": // Rule 22 - Should fall through
            case "angle1": // Rule 22 - Should fall through
            case "angle2": // Rule 22 - Should end here
                Symbol Oprel = new Symbol(-1, "Oprel", true);
                Symbol RTerm = new Symbol(-1, "RTerm", true);
                Symbol E = new Symbol(-1, "E", true);
                results.add(Oprel);
                results.add(RTerm);
                results.add(E);
                return results;
            default: // Special case for LRE... Rule 50
                return results;
        }
    }

    private List<Symbol> getExprSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 23 - Should fall through
            case "int": // Rule 23 - Should fall through
            case "float": // Rule 23 - Should fall through
            case "string": // Rule 23 - Should fall through
            case "id": // Rule 23 - Should end here
                Symbol RTerm = new Symbol(-1, "RTerm", true);
                Symbol E = new Symbol(-1, "E", true);
                results.add(RTerm);
                results.add(E);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getRSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "plus": // Rule 24 - Should fall through
            case "minus": // Rule 24 - Should end here
                Symbol Opadd = new Symbol(-1, "Opadd", true);
                Symbol Term = new Symbol(-1, "Term", true);
                Symbol R = new Symbol(-1, "R", true);
                results.add(Opadd);
                results.add(Term);
                results.add(R);
                return results;
            default: // Special case for LRE... Rule 51
                return results;
        }
    }

    private List<Symbol> getRtermSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 25 - Should fall through
            case "int": // Rule 25 - Should fall through
            case "float": // Rule 25 - Should fall through
            case "string": // Rule 25 - Should fall through
            case "id": // Rule 25 - Should end here
                Symbol Term = new Symbol(-1, "Term", true);
                Symbol R = new Symbol(-1, "R", true);
                results.add(Term);
                results.add(R);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getTSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "aster": // Rule 26 - Should fall through
            case "slash": // Rule 26 - Should fall through
            case "caret": // Rule 26 - Should end here
                Symbol Opmul = new Symbol(-1, "Opmul", true);
                Symbol Fact = new Symbol(-1, "Fact", true);
                Symbol T = new Symbol(-1, "T", true);
                results.add(Opmul);
                results.add(Fact);
                results.add(T);
                return results;
            default: // Special case for LRE... Rule 52
                return results;
        }
    }

    private List<Symbol> getTermSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "parens1": // Rule 27 - Should fall through
            case "int": // Rule 27 - Should fall through
            case "float": // Rule 27 - Should fall through
            case "string": // Rule 27 - Should fall through
            case "id": // Rule 27 - Should end here
                Symbol Fact = new Symbol(-1, "Fact", true);
                Symbol T = new Symbol(-1, "T", true);
                results.add(Fact);
                results.add(T);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getFactSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "int": // Rule 28
                Symbol fact_int = new Symbol(3, "int", false);
                results.add(fact_int);
                return results;
            case "float": // Rule 29
                Symbol fact_float = new Symbol(4, "float", false);
                results.add(fact_float);
                return results;
            case "string": // Rule 30
                Symbol string = new Symbol(5, "string", false);
                results.add(string);
                return results;
            case "id": // Rule 31
                Symbol Varid = new Symbol(-1, "Varid", true);
                results.add(Varid);
                return results;
            case "parens1": // Rule 32
                Symbol PPexprl = new Symbol(-1, "PPexprl", true);
                results.add(PPexprl);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getOprelSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "opeq": // Rule 33
                Symbol opeq = new Symbol(52, "opeq", false);
                results.add(opeq);
                return results;
            case "opne": // Rule 34
                Symbol opne = new Symbol(53, "opne", false);
                results.add(opne);
                return results;
            case "angle1": // Rule 35
                Symbol Lthan = new Symbol(-1, "Lthan", true);
                results.add(Lthan);
                return results;
            case "ople": // Rule 36
                Symbol ople = new Symbol(54, "ople", false);
                results.add(ople);
                return results;
            case "opge": // Rule 37
                Symbol opge  = new Symbol(55, "opge", false);
                results.add(opge);
                return results;
            case "angle2": // Rule 38
                Symbol Gthan = new Symbol(-1, "Gthan", true);
                results.add(Gthan);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getLthanSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "angle1": // Rule 39
                Symbol angle1 = new Symbol(31, "angle1", false);
                results.add(angle1);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getGthanSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "angle2": // Rule 40
                Symbol angle2 = new Symbol(32, "angle2", false);
                results.add(angle2);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }

    private List<Symbol> getOpaddSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "plus":
                Symbol plus = new Symbol(47, "plus", false);
                results.add(plus);
                return results;
            case "minus":
                Symbol minus = new Symbol(46, "minus", false);
                results.add(minus);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }
    private List<Symbol> getOpmulSymbols(Token token) throws Exception {
        List<Symbol> results = new ArrayList<>();
        switch (token.getTokenName()) {
            case "aster":
                Symbol aster = new Symbol(41, "aster", false);
                results.add(aster);
                return results;
            case "slash":
                Symbol slash = new Symbol(48, "slash", false);
                results.add(slash);
                return results;
            case "caret":
                Symbol caret = new Symbol(42, "caret", false);
                results.add(caret);
                return results;
            default:
                throw new Exception("Token not found!");
        }
    }
}
