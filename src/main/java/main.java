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
        
        Node one = new Node(1, new Symbol(10,"kprog",true));
        Node two = new Node(2, new Symbol(33,"brace1",true));
        Node three = new Node(3, new Symbol(45,"equal",true));
        Node four = new Node(4, new Symbol(2,"id",true), new Token(1,2,"pi"));
        Node five = new Node(5, new Symbol(4,"float",true),new Token(1,4,"float",(float)3.14));//Token
        Node six = new Node(6, new Symbol(23,"kprint",true));
        Node seven = new Node(7, new Symbol(37,"parens1",true));
        Node eight = new Node(8, new Symbol(5,"string",true),new Token(5,5,"Input radius> "));
        Node nine = new Node(9,new Symbol(38,"parens2",true));
        Node ten = new Node(10, new Symbol(45,"equal",true));
        Node eleven = new Node(11, new Symbol(2,"id",true), new Token(1,2,"circum"));
        Node twelve = new Node(12, new Symbol(41,"aster",true));
        Node thirteen = new Node(13, new Symbol(3,"int",true), new Token(1,3,"int",(int)2));
        Node fourteen = new Node(14, new Symbol(41,"aster",true));
        Node fifteen = new Node(15, new Symbol(2,"id",true), new Token(1,2,"pi"));
        Node sixteen = new Node(16, new Symbol(2,"id",true), new Token(1,2,"rx"));
        Node seventeen = new Node(17, new Symbol(23,"kprint",true));
        Node eighteen = new Node(18, new Symbol(37,"parens1",true));
        Node nineteen = new Node(19, new Symbol(5,"string",true),new Token(5,5,"Circumf = "));
        Node twenty = new Node(20, new Symbol(2,"id",true), new Token(1,2,"circum"));
        Node twentyOne = new Node(21,new Symbol(38,"parens2",true));
        Node twentyTwo = new Node(22,new Symbol(39,"brace2",true));

        one.addChild(two);

        two.addChild(three);
        two.addChild(six);
        two.addChild(ten);
        two.addChild(seventeen);
        two.addChild(twentyTwo);

        three.addChild(four);
        three.addChild(five);

        six.addChild(seven);
        six.addChild(nine);

        seven.addChild(eight);

        ten.addChild(eleven);
        ten.addChild(twelve);

        twelve.addChild(thirteen);
        twelve.addChild(fourteen);

        fourteen.addChild(fifteen);
        fourteen.addChild(sixteen);

        seventeen.addChild(eighteen);
        seventeen.addChild(twentyOne);

        eighteen.addChild(nineteen);
        eighteen.addChild(twenty);
        
        Parser parser = new Parser();
        ScopeNode rootSNode = new ScopeNode();
        parser.ast2sct(firstExample,rootSNode);
        printScopeTree(rootSNode, 0);
        parser.runAST(six);


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
