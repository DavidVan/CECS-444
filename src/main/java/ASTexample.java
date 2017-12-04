class ASTexample {
   
   public Node createFirstExample() {
        Node one = new Node(1, new Symbol(10,"kprog",true));
        Node two = new Node(2, new Symbol(33,"brace1",true));
        Node three = new Node(3, new Symbol(45,"equal",true));

        Token t = new Token(1,2,"pi");
        Symbol s = new Symbol(2,"id",true);
        s.setToken(t);
        Node four = new Node(4, s, t);
        
        Token t2 = new Token(1,4,"float",(float)3.14);
        Symbol s2 = new Symbol(4,"float",true);
        s2.setToken(t2);
        Node five = new Node(5, s2,t2);//Token

        Node six = new Node(6, new Symbol(23,"kprint",true));
        Node seven = new Node(7, new Symbol(37,"parens1",true));

        Token t3 = new Token(5,5,"Input radius> ");
        Symbol s3 = new Symbol(5,"string",true);
        s3.setToken(t3);
        Node eight = new Node(8, s3, t3);


        Node nine = new Node(9,new Symbol(38,"parens2",true));
        Node ten = new Node(10, new Symbol(45,"equal",true));

        Token t4 = new Token(1,2,"circum");
        Symbol s4 = new Symbol(2,"id",true);
        s4.setToken(t4);
        Node eleven = new Node(11, s4, t4);

        Node twelve = new Node(12, new Symbol(41,"aster",true));

        Token t5 = new Token(1,3,"int",(int)2);
        Symbol s5 = new Symbol(3,"int",true);
        s5.setToken(t5);
        Node thirteen = new Node(13, s5, t5);
        Node fourteen = new Node(14, new Symbol(41,"aster",true));

        Token t6 = new Token(1,2,"pi");
        Symbol s6 = new Symbol(2,"id",true);
        Node fifteen = new Node(15, s6,t6);

        Token t7 = new Token(1,2,"rx");
        Symbol s7 = new Symbol(2,"id",true);
        s7.setToken(t7);
        Node sixteen = new Node(16, s7, t7);


        Node seventeen = new Node(17, new Symbol(23,"kprint",true));
        Node eighteen = new Node(18, new Symbol(37,"parens1",true));

        Token t8 = new Token(5,5,"Circumf = ");
        Symbol s8 =  new Symbol(5,"string",true);
        s8.setToken(t8);
        Node nineteen = new Node(19, s8, t8);

        Token t9 =  new Token(1,2,"circum");
        Symbol s9 = new Symbol(2,"id",true);
        s9.setToken(t9);
        Node twenty = new Node(20, s9, t9);

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
        
        return one;
   }
   
   public Node createOldExample() {
//      Node one = new Node(1,new Symbol(10,"prog",true));
//        Node three = new Node(3,new Symbol(33,"brace1",true));
//        Node six = new Node(6,new Symbol(45,"equal",true));
//        Node thirteen = new Node(13,new Symbol(2,"id",true),new Token(2,2,"pi"));//Token
//        Node fifteen = new Node(15,new Symbol(4,"float",true),new Token(2,4,"float",(float)3.14));//Token
//        Node eleven = new Node(11,new Symbol(23,"print",true));
//        Node thirtyTwo = new Node(32,new Symbol(37,"parens1",true));
//        Node thirtyFour = new Node(34,new Symbol(5,"string",true),new Token(3,5,"Input radius"));//Token
//        Node thirtyFive = new Node(35,new Symbol(38,"parens2",true));
//        Node twentyNine = new Node(29,new Symbol(45,"equal",true));
//        Node fiftyThree = new Node(53,new Symbol(2,"id",true),new Token(4,2,"circum"));//Token
//        Node fiftyFive = new Node(55,new Symbol(3,"int",true),new Token(4,3,"int",2));//Token
//        Node sixtyTwo = new Node(62,new Symbol(41,"aster",true));
//        Node sixtyFive = new Node(65,new Symbol(2,"id",true),new Token(4,2,"pi"));//Token
//        Node sixtySix = new Node(66,new Symbol(41,"aster",true));
//        Node seventy = new Node(70,new Symbol(2,"id",true),new Token(4,2,"rx"));//Token
//        Node fiftyOne = new Node(51,new Symbol(23,"print",true));
//        Node eightyTwo = new Node(82,new Symbol(37,"parens1",true));
//        Node eightyFour = new Node(84,new Symbol(5,"string",true),new Token(5,5,"Circumf = "));//Token
//        Node eightySeven = new Node(87,new Symbol(6,"comma",true));
//        Node ninetyNine = new Node(99,new Symbol(2,"id",true),new Token(5,2,"circum"));//Token
//        Node eightyFive = new Node(85,new Symbol(38,"parens2",true));
//        Node seven = new Node(7,new Symbol(34,"brace2",true));
//
//
//        //Link children
//        one.addChild(three);
//
//        three.addChild(six);
//        three.addChild(seven);
//
//        six.addChild(thirteen);
//        six.addChild(fifteen);
//        six.addChild(eleven);
//
//        eleven.addChild(thirtyTwo);
//        eleven.addChild(twentyNine);
//
//        thirtyTwo.addChild(thirtyFour);
//        thirtyTwo.addChild(thirtyFive);
//
//        twentyNine.addChild(fiftyThree);
//        twentyNine.addChild(fiftyFive);
//        twentyNine.addChild(fiftyOne);
//
//        fiftyFive.addChild(sixtyTwo);
//
//        sixtyTwo.addChild(sixtyFive);
//        sixtyTwo.addChild(sixtySix);
//
//        sixtySix.addChild(seventy);
//
//        fiftyOne.addChild(eightyTwo);
//
//        eightyTwo.addChild(eightyFour);
//        eightyTwo.addChild(eightyFive);
//
//        eightyFour.addChild(eightySeven);
//
//        eightySeven.addChild(ninetyNine);
        return null;
   }
}
