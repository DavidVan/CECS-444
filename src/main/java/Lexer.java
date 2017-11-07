import java.util.*;

public class Lexer {

    private char[] input;
    private int currentLine;
    private int currentIndex;

    public Lexer(String input) {
        this.input= input.toCharArray();
        this.currentLine = 1;
        this.currentIndex = -1;
    }

    public List<Token> processInput() {
        List<Token> tokens = new ArrayList<>();
        while (currentIndex < input.length - 1) {
            String token = readUntilSpace();
            int tokenId = handleToken(token);
            if (token.length() == 0) {
                continue;
            }
            tokens.add(new Token(currentLine, tokenId, token));
        }
        return tokens;
    }

    private int handleToken(String token) {
        // This method calls the other handle methods to get tokenId.
        return 99;
    }

    public List<String> processInputAsStrings() {
        List<String> strings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideQuote = false;
        boolean checkIfComment = false;
        try {
            while (canPeek()) {
                String nextCharacter = peek();
                System.out.println("Checking comment? " + checkIfComment + " Current token: " + nextCharacter);
                if (nextCharacter.equals("\"")) {
                    if (insideQuote) {
                        insideQuote = false;
                        strings.add(sb.toString());
                        sb.setLength(0); // Clear the StringBuilder.
                    }
                    else {
                        insideQuote = true;
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
                if (nextCharacter.equals("(") || nextCharacter.equals(",") || nextCharacter.equals(";") || nextCharacter.equals("\n")) {
                    if (sb.length() != 0) {
                        strings.add(sb.toString());
                        sb.setLength(0);
                    }
                }
                if (nextCharacter.equals(" ") && !insideQuote) {
                    if (sb.length() != 0) {
                        strings.add(sb.toString());
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
            strings.add(sb.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return strings;
    }

    private String readUntilSpace() {
        StringBuilder sb = new StringBuilder();
        try {
            boolean breakOnNextNonSpace = false;
            while (canPeek()) {
                String nextCharacter = peek();
                if (!nextCharacter.equals(" ")) {
                    if (breakOnNextNonSpace) {
                        break;
                    }
                    else {
                        sb.append(nextCharacter);
                    }
                }
                else {
                    breakOnNextNonSpace = true;
                }
                advance();
            }
            return sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            return ""; 
        }
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
                return 99;
        }
    }

}
