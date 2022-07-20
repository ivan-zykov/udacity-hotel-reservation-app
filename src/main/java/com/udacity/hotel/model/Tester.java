package com.udacity.hotel.model;

import java.util.*;

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

        System.out.println("--- Testing Room::isFree() on room 101 ---");
        System.out.println(room101.isFree());
        System.out.println();

        System.out.println("--- Testing Room::equals() ---");
        System.out.println("Customer equals Room:");
        System.out.println(room101.equals(vanya));
        Room room101Duplicate = new Room("101", 90.0, RoomType.DOUBLE);
        System.out.println("Different rooms with same number equal:");
        System.out.println(room101.equals(room101Duplicate));
        System.out.println();

        System.out.println("--- Testing Room::hashCode() ---");
        System.out.println("Expect the same hash codes:");
        System.out.println(room101.hashCode());
        System.out.println(room101Duplicate.hashCode());
        System.out.println();

        System.out.println("--- Testing FreeRoom ---");
        FreeRoom freeRoom = new FreeRoom("102", RoomType.SINGLE);
        System.out.println(freeRoom);
        System.out.println();

        System.out.println("--- Testing Reservation ---");
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MARCH, 16);
        Date inDate = cal.getTime();
        cal.set(2022, Calendar.MARCH,26);
        Date outDate = cal.getTime();
        Reservation res1 = new Reservation(vanya, room101, inDate, outDate);
        System.out.println(res1);
        System.out.println();

        System.out.println("--- Testing Reservation::equals() ---");
        Customer jenny = new Customer("Jenny", "Red", "j@r.com");
        Reservation res2 = new Reservation(jenny, room101, inDate, outDate);
        System.out.println(res2);
        System.out.println("Reservations equal:");
        System.out.println(res1.equals(res2));
        System.out.println();

        System.out.println("--- Testing adding equal reservations to a set ---");
        System.out.println("Hash codes for same reservations:");
        System.out.println(res1.hashCode());
        System.out.println(res2.hashCode());
        Set<Reservation> allReservations = new HashSet<>();
        allReservations.add(res1);
        allReservations.add(res2);
        System.out.println("HashSet after adding duplicate reservation:");
        for (Reservation aRes: allReservations) {
            System.out.println(aRes);
        }
        System.out.println();

        System.out.println("--- End model tests ---");
    }
}
