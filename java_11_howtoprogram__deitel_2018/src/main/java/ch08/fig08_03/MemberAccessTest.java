package ch08.fig08_03;

import ch08.fig08_01.Time1;

// Fig. 8.3: MemberAccessTest.java
// Private members of class Time1 are not accessible.
public class MemberAccessTest {

    public static void main(String[] args) {
        Time1 time = new Time1(); // create and initialize Time1 object

////      time.hour = 7; // error: hour has private access in Time1
////      time.minute = 15; // error: minute has private access in Time1
////      time.second = 30; // error: second has private access in Time1
    }

}
