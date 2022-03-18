package model;

import java.util.Calendar;
import java.util.Date;

public class Tester {

    public static void main(String[] args) {

        System.out.println("--- Start model tests ---");
        System.out.println();

        System.out.println("--- Testing Customer ---");
        Customer vanya = new Customer("Vanya", "Zykov",
                "vanya@bk.ru");
        System.out.println(vanya);
        try {
            new Customer("First", "Second",
                    "email");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        System.out.println("--- Testing Room ---");
        Room room101 = new Room("101", 150.0, RoomType.SINGLE);
        System.out.println(room101);
        System.out.println();

        System.out.println("--- Testing FreeRoom ---");
        FreeRoom freeRoom = new FreeRoom("102", RoomType.SINGLE);
        System.out.println(freeRoom);
        System.out.println();

        System.out.println("--- Testing Reservation ---");
        Calendar cal = Calendar.getInstance();
        cal.set(2022, 03, 16);
        Date inDate = cal.getTime();
        cal.set(2022, 03,26);
        Date outDate = cal.getTime();
        Reservation res = new Reservation(vanya, room101, inDate, outDate);
        System.out.println(res);
        System.out.println();

        System.out.println("--- End model tests ---");
    }
}
