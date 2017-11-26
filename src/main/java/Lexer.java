import java.util.*;

public class Lexer {

    private char[] input;
    private int currentIndex;

    private List<Token> tokens;
    
    public Lexer(String input) {
        this.input= input.toCharArray();
        this.currentIndex = -1;

        this.tokens = new ArrayList<>();
    }

    public List<Token> processInput() {
        processInputAsStrings();
        return this.tokens;
    }

    private void addToTokenList(String token, int currentLine, boolean isString) {
        if (!isString) {
            int tokenId = handleToken(token);
            Token tokenObject = new Token(currentLine, tokenId, token);
            this.tokens.add(tokenObject);
            if (tokenId == 3) {
                tokenObject.setInt(Integer.parseInt(token));
            }
            if (tokenId == 4) {
                tokenObject.setFloat(Float.parseFloat(token));
            }
        }
        else {
            this.tokens.add(new Token(currentLine, 5, token));
        }
    }

    private int handleToken(String token) {
        // This method calls the other handle methods to get tokenId.
        int result = handleMultiCharOperators(token);
        if (result == 99) {
            result = handleOtherPunctuation(token);
        }
        if (result == 99) {
            result = handlePairedDelimeters(token);
        }
        if (result == 99) {
            result = handleUnpairedDelimeters(token);
        }
        if (result == 99) {
            result = handleKeywords(token);
        }
        if (result == 99) {
            result = handleMiscellaneous(token);
        }
        return result;
    }



    private List<String> processInputAsStrings() { // Returns string tokens, not token objects.
        List<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideQuote = false;
        boolean checkIfComment = false;
        int currentLine = 1;
        try {
            while (canPeek()) {
                String nextCharacter = peek();
                // System.out.println("Current token: " + nextCharacter);
                if (nextCharacter.equals("\0")) {
                    if (sb.length() != 0) {
                        addToTokenList(sb.toString(), currentLine, false);
                        sb.setLength(0);
                        sb.append(nextCharacter);
                        addToTokenList(sb.toString(), currentLine, false);
                        sb.setLength(0);
                        advance();
                        continue;
                    }
                }
                if (nextCharacter.equals("\"")) {
                    if (insideQuote) {
                        insideQuote = false;
                        addToTokenList(sb.toString(), currentLine, true);
                        sb.setLength(0); // Clear the StringBuilder.
                    }
                    else {
                        insideQuote = true;
                        if (sb.length() != 0) {
                            addToTokenList(sb.toString(), currentLine, false);
                            sb.setLength(0);
                        }
                    }
                    advance();
                    continue;
                }
                if (nextCharacter.equals("/")) {
                    if (checkIfComment) {
                        sb.setLength(0);
                        while (!peek().equals("\n")) {
                            advance();
                        }
                        currentLine++;
                        advance();
                        continue;
                    }
                    else {
                        checkIfComment = true;
                        sb.append(nextCharacter);
                        advance();
                        continue;
                    }
                }
                if (nextCharacter.equals("-")) {
                    String savedCharacter = nextCharacter;
                    advance();
                    if (peek().equals(">")) {
                        if (sb.length() != 0) {
                            addToTokenList(sb.toString(), currentLine, false);
                            sb.setLength(0);
                        }
                        sb.append(savedCharacter);
                        nextCharacter = peek();
                        sb.append(nextCharacter);
                        advance();
                        if (sb.length() != 0) {
                            addToTokenList(sb.toString(), currentLine, false);
                            sb.setLength(0);
                        }
                        //sb.setLength(0);
                        continue;
                    }
                    else {
                        sb.append(savedCharacter);
                        continue;
                    }
                }
                if (nextCharacter.equals("(") || nextCharacter.equals(",") || nextCharacter.equals(";") || nextCharacter.equals("\n")) {
                    if (sb.length() != 0) {
                        addToTokenList(sb.toString(), currentLine, false);
                        sb.setLength(0);
                    }
                    if (nextCharacter.equals("\n")) {
                        currentLine++;
                    }
                }
                if (nextCharacter.equals(" ") && !insideQuote) {
                    if (sb.length() != 0) {
                        addToTokenList(sb.toString(), currentLine, false);
                        sb.setLength(0); // Clear the StringBuilder.
                    }
                    advance();
                    continue;
                }
                if (checkIfComment) {
                    checkIfComment = false; // Reset the check since we did not find another '/'.
                }
                if (!nextCharacter.equals("\n")) {
                    sb.append(nextCharacter);
                }
                advance();
            }
            if (sb.length() != 0) {
                addToTokenList(sb.toString(), currentLine, false);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strings;
    }

    private boolean canPeek() {
        return currentIndex + 1 <= input.length - 1;
    }

    private String peek() throws Exception {
        if (currentIndex + 1 > input.length) {
            throw new Exception("Peek will go out of bounds!");
        }
        return Character.toString(input[currentIndex + 1]);
    }

    private void advance() {
        this.currentIndex++;
    }

    private int handleUnpairedDelimeters(String token) {
        switch (token) {
            case ",":
                return 6;
            case ";":
                return 7;
            default:
                return 99;
        }
    }

    private int handleKeywords(String token) {
        switch (token) {
            case "prog":
                return 10;
            case "main":
                return 11;
            case "fcn":
                return 12;
            case "class":
                return 13;
            case "float":
                return 15;
            case "int":
                return 16;
            case "string":
                return 17;
            case "if":
                return 18;
            case "elseif":
                return 19;
            case "else":
                return 20;
            case "while":
                return 21;
            case "input":
                return 22;
            case "print":
                return 23;
            case "new":
                return 24;
            case "return":
                return 25;
            case "vars":
                return 26;
            default:
                return 99;
        }
    }

    private int handlePairedDelimeters(String token) {
        switch (token) {
            case "<":
                return 31;
            case ">":
                return 32;
            case "{":
                return 33;
            case "}":
                return 34;
            case "[":
                return 35;
            case "]":
                return 36;
            case "(":
                return 37;
            case ")":
                return 38;
            default:
                return 99;
        }
    }

    private int handleOtherPunctuation(String token) {
        switch (token) {
            case "*":
                return 41;
            case "^":
                return 42;
            case ":":
                return 43;
            case ".":
                return 44;
            case "=":
                return 45;
            case "-":
                return 46;
            case "+":
                return 47;
            case "/":
                return 48;
            default:
                return 99;
        }
    }

    private int handleMultiCharOperators(String token) {
        switch (token) {
            case "->":
                return 51;
            case "==":
                return 52;
            case "!=":
                return 53;
            case "<=":
                return 54;
            case ">=":
                return 55;
            case "<<":
                return 56;
            case ">>":
                return 57;
            default:
                return 99;
        }
    }

    private int handleMiscellaneous(String token) {
        switch (token) {
            case "\0":
                return 0;
            default:
                try {
                    Double.parseDouble(token);
                    if (token.contains(".")) {
                        return 4;
                    }
                    else {
                        return 3;
                    }
                }
                catch (NumberFormatException e) {
                    //e.printStackTrace();
                    if (token.matches("[a-zA-Z_]*")) {
                        return 2;
                    }
                    return 99;
                }
        }
    }

}
