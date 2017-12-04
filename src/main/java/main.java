import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Program Started!");
        // String  old_test1 = "prog { print( \"ASCII:\", \" A= \", 65, \" Z= \", 90 ); }\0";
        // String  old_test2 = "prog { // Find the circumference of a circle.\n    pi = 3.14;\n    print( \"Input radius> \" );\n    rx = input ( float );\n    circum = 2 * pi * rx;\n    print( \"Circumf= \", circum );\n}\0";
        // String  old_test3 = "prog { // Find the hypotenuse of a right triangle.\n    print( \"Input legs> \" );\n    a = input ( int );\n    b = input ( int );\n    print( \"Hypotenuse= \", ( a * a + b * b ) ^ 0.5 );\n}\0";
        // String  old_test4 = "prog { hello->hi }\0";
        // String  old_test5 = "prog { 5 - 3 }\0 hello";
        
        String test1 = "prog {\n    print( \"ASCII:\", \" A= \", 65, \" Z= \", 90 );\n}\0";
        String test2 = "prog { // Find the circumference of a circle.\n    pi = 3.14;\n    print( \"Input radius> \" );\n    circum = 2 * pi * rx;\n    print( \"Circumf= \", circum );\n}\0";
        String test3 = "prog { // Find the hypotenuse of a right triangle.\n    print( \"Input legs> \" );\n    print( \"Hypotenuse= \", ( a * a + b * b ) ^ 0.5 );\n}\0";
        String test4 = "prog {\n    leet = 13.37;\n    print(\"This is just a test!\");\n}\0";
        String test5 = "prog {\n    true_thing = 1 < 2;\n    print(\"Hello\", \"World\");\n    false_thing = 1 > 2;\n}\0";

        System.out.println(test2);
        Lexer lexer = new Lexer(test2);
        
        ASTexample ex = new ASTexample();
        Node firstExample = ex.createFirstExample();
        
        Parser parser = new Parser();
        ScopeNode rootSNode = new ScopeNode();
        parser.ast2sct(firstExample,rootSNode);
        printScopeTree(rootSNode, 0);
        parser.runAST(firstExample);


        /*
        List<Token> tokens = lexer.processInput();
        for (Token t : tokens) {
            System.out.println(t);
        }
        Parser parser = new Parser(tokens);
        Node PST = parser.parse();
        System.out.println("Converting to AST...");
        Node AST = parser.parseToAST(PST, null);
        printTree(AST);
        // List<String> strings = lexer.processInputAsStrings();
        // for (String s : strings) {
        //     System.out.println("Token: \"" + s + "\"");
        // }*/
    }

    public static void printTree(Node tree) {
        System.out.println(tree);
        List<Node> children = tree.getChildren();
        if (children.size() == 0) {
            return;
        }
        for (Node n : children) {
            printTree(n);
        }
    }

    public static void printScopeTree(ScopeNode tree, int level){
        if (tree == null) {
            return;
        }
        System.out.println("Level: " + level);
        System.out.println(tree.getSCTMap().keySet());
        printScopeTree(tree.getKid(), ++level);
    }


}
