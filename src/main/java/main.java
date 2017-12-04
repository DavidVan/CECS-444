import java.util.*;

public class main {
    public static void main(String[] args) throws Exception {
        System.out.println("Program Started!");
        
        String test1 = "prog {\n    print( \"ASCII:\", \" A= \", 65, \" Z= \", 90 );\n}\0";
        String test2 = "prog { // Find the circumference of a circle.\n    pi = 3.14;\n    print( \"Input radius> \" );\n    circum = 2 * pi * pi;\n    print( \"Circumf= \", circum );\n}\0";
        String test3 = "prog { // Find the hypotenuse of a right triangle.\n    print( \"Input legs> \" );\n    print( \"Hypotenuse= \", ( a * a + b * b ) ^ 0.5 );\n}\0";
        String test4 = "prog {\n    leet = 13.37;\n    print(\"This is just a test!\");\n}\0";
        String test5 = "prog {\n    true_thing = 1 < 2;\n    print(\"Hello\", \"World\");\n    false_thing = 1 > 2;\n}\0";
        
        System.out.println();
        System.out.println(test2);
        System.out.println();
        Lexer lexer = new Lexer(test2);
        
        ASTexample ex = new ASTexample();
        Node firstExample = ex.createFirstExample();
        
        Parser parser = new Parser();
        ScopeNode rootSNode = new ScopeNode();
        System.out.println("RUNNING AST... OUTPUT:");
        parser.ast2sct(firstExample,rootSNode);
        parser.runAST(firstExample, rootSNode);
        
        System.out.println();
        printScopeTree(rootSNode, 0);

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
        System.out.println("Variable Names: " + tree.getSCTMap().keySet());
        System.out.println("Values: " + tree.getValMap().keySet());
        printScopeTree(tree.getKid(), ++level);
    }


}
