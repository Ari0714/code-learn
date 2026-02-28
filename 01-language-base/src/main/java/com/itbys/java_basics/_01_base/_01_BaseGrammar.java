package com.itbys.java_basics._01_base;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _01_BaseGrammar {

    public static void main(String[] args) {

//        byte a = 1;
//        short b = 22;
//
//        short c = (short)(a + b);
//
//        System.out.println(c);

//        Scanner scanner = new Scanner(System.in);
//        System.out.println(scanner.next());


//        Scanner scanner = new Scanner(System.in);
//        int nextInt = scanner.nextInt();
//        switch (nextInt){
//            case 1:
//                System.out.println("111");
//                break;
//            case 2:
//                System.out.println("222");
//                break;
//        }


//        int a = 0;
//        for (int i=1;i<=100;i++){
//            if (i % 2 == 0){
//                System.out.println(i);
//                a += i;
//            }
//        }

        for (int a = 1; a <= 9; a++) {
            for (int b = 1; b <= a; b++) {
                System.out.print(a + "*" + b + "=" + a * b + "\t");
            }
            System.out.println();
        }


    }

}
