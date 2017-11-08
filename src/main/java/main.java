import java.util.*;

public class main {
    public static void main(String[] args) {
        System.out.println("Program Started!");
        String test1 = "prog main { print( \"ASCII:\", \" A= \", 65, \" Z= \", 90 ); }\0";
        String test2 = "prog main { // Find the circumference of a circle.\n    pi = 3.14;\n    print( \"Input radius> \" );\n    rx = input ( float );\n    circum = 2 * pi * rx;\n    print( \"Circumf= \", circum );\n}\0";
        String test3 = "prog main { // Find the hypotenuse of a right triangle.\n    print( \"Input legs> \" );\n    a = input ( int );\n    b = input ( int );\n    print( \"Hypotenuse= \", ( a * a + b * b ) ^ 0.5 );\n}\0";
        String test4 = "prog main { hello->hi  }";
        String test5 = "prog main { 5 - 3 }\0 hello";
        System.out.println(test3);
        Lexer lexer = new Lexer(test3);

        List<Token> tokens = lexer.processInput();
         for (Token t : tokens) {
             System.out.println(t);
        }
        // List<String> strings = lexer.processInputAsStrings();
        // for (String s : strings) {
        //     System.out.println("Token: \"" + s + "\"");
        // }
    }
}
