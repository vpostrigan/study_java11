package ch04;

// Exercise 4.10: Printing.java
public class Printing {

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 5; j++) {
                System.out.print('@');
            }

            System.out.println();
        }
    }
/*
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
@@@@@
 */
}