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
        /*
        Node testy = new Node(69, new Symbol(69,"brace1", true));
        Parser parser = new Parser();
        parser.ast2sct(testy, new SNode());
        */
        System.out.println(test2);
        Lexer lexer = new Lexer(test2);


//Pat's AST hard coded tree
        Node one = new Node(1,new Symbol(10,"prog",true));
        Node three = new Node(3,new Symbol(33,"brace1",true));
        Node six = new Node(6,new Symbol(45,"equal",true));
        Node thirteen = new Node(13,new Symbol(2,"id",true),new Token(2,2,"pi"));//Token
        Node fifteen = new Node(15,new Symbol(10,"float",true),new Token(2,4,"float",(float)3.14));//Token
        Node eleven = new Node(11,new Symbol(23,"print",true));
        Node thirtyTwo = new Node(32,new Symbol(37,"parens1",true));
        Node thirtyFour = new Node(34,new Symbol(5,"string",true),new Token(3,5,"Input radius"));//Token
        Node thirtyFive = new Node(35,new Symbol(38,"parens2",true));
        Node twentyNine = new Node(29,new Symbol(45,"equal",true));
        Node fiftyThree = new Node(53,new Symbol(2,"id",true),new Token(4,2,"circum"));//Token
        Node fiftyFive = new Node(55,new Symbol(3,"int",true),new Token(4,3,"int",2));//Token
        Node sixtyTwo = new Node(62,new Symbol(41,"aster",true));
        Node sixtyFive = new Node(65,new Symbol(2,"id",true),new Token(4,2,"pi"));//Token
        Node sixtySix = new Node(66,new Symbol(41,"aster",true));
        Node seventy = new Node(70,new Symbol(2,"id",true),new Token(4,2,"rx"));//Token
        Node fiftyOne = new Node(51,new Symbol(23,"print",true));
        Node eightyTwo = new Node(82,new Symbol(37,"parens1",true));
        Node eightyFour = new Node(84,new Symbol(5,"string",true),new Token(5,5,"Circumf = "));//Token
        Node eightySeven = new Node(87,new Symbol(6,"comma",true));
        Node ninetyNine = new Node(99,new Symbol(2,"id",true),new Token(5,2,"circum"));//Token
        Node eightyFive = new Node(85,new Symbol(38,"parens2",true));
        Node seven = new Node(7,new Symbol(34,"brace2",true));


        //Link children
        one.addChild(three);

        three.addChild(six);
        three.addChild(seven);

        six.addChild(thirteen);
        six.addChild(fifteen);
        six.addChild(eleven);

        eleven.addChild(thirtyTwo);
        eleven.addChild(twentyNine);

        thirtyTwo.addChild(thirtyFour);
        thirtyTwo.addChild(thirtyFive);

        twentyNine.addChild(fiftyThree);
        twentyNine.addChild(fiftyFive);
        twentyNine.addChild(fiftyOne);

        fiftyFive.addChild(sixtyTwo);

        sixtyTwo.addChild(sixtyFive);
        sixtyTwo.addChild(sixtySix);

        sixtySix.addChild(seventy);

        fiftyOne.addChild(eightyTwo);

        eightyTwo.addChild(eightyFour);
        eightyTwo.addChild(eightyFive);

        eightyFour.addChild(eightySeven);

        eightySeven.addChild(ninetyNine);
        
        
        Parser parser = new Parser();
        SNode rootSNode = new SNode();
        parser.ast2sct(one,rootSNode);
        printSTree(rootSNode, 0);


//End Pat's AST hard coded tree


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

    public static void printSTree(SNode tree, int level){
        if (tree == null) {
            return;
        }
        System.out.println("Level: " + level);
        System.out.println(tree.getSCTMap().keySet());
        printSTree(tree.getKid(), ++level);
    }

}
