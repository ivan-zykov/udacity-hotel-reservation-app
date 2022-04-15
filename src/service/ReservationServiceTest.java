package service;

import model.*;

import java.util.*;

public class ReservationServiceTest {

    public static void main(String[] args) {

        System.out.println("--- Start ReservationService test ---");
        System.out.println();

        System.out.println("--- Testing addRoom() and getARoom() ---");
        ReservationService serv = ReservationService.getInstance();
        String roomId = "102";
        IRoom room102 = new Room(roomId, 170.0, RoomType.DOUBLE);
        serv.addRoom(room102);
        // Add a free of charge room
        IRoom room103 = new FreeRoom("103", RoomType.SINGLE);
        serv.addRoom(room103);
        IRoom returnedRoom = serv.getARoom(roomId);
        System.out.println(returnedRoom);
        System.out.println();

        // Handling getting a room with non-existing ID
        try {
            serv.getARoom("999");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        System.out.println("--- Testing reserveARoom() ---");
        Customer vanya = new Customer("Vanya", "Zykov",
                "vanya@bk.ru");
        Calendar cal = Calendar.getInstance();
        Reservation res1 = reserveARoomInMay(serv, vanya, room102, cal, 10,
                17);

        System.out.println("New reservation: ");
        System.out.println(res1);
        System.out.println();

        System.out.println("--- Try reserving the same room again ---");
        // Make the other room also unavailable
        reserveARoomInMay(serv, vanya, room103, cal, 10, 17);
        // Test attempting to reserve a booked room
        IRoom room102Copy = new Room(roomId, 170.0, RoomType.DOUBLE);
        try {
            reserveARoomInMay(serv, vanya, room102Copy, cal, 10,
                    17);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println("All reservations below. Expect two.");
        serv.printAllReservations();
        System.out.println();

        System.out.println("--- Testing findRooms() ---");

        // Check for 9 - 10 May
        System.out.println("Expect free rooms: 102, 103");
        testFindRooms(serv, cal, 9, 10);

        // Check for 10 - 11 May
        System.out.println("Expect nothing free");
        testFindRooms(serv, cal, 10, 11);

        // Check for 16 - 17 May
        System.out.println("Expect nothing free");
        testFindRooms(serv, cal, 16, 17);

        // Check for 17 - 18 May.
        System.out.println("Expect free rooms: 102, 103");
        testFindRooms(serv, cal, 17, 18);

        System.out.println("--- Testing getCustomersReservation() ---");
        Collection<Reservation> customersReservations = serv.getCustomersReservation(vanya);
        for (Reservation aReservation: customersReservations) {
            System.out.println(aReservation);
        }
        System.out.println();

        System.out.println("--- End ReservationService test ---");
    }

    private static Reservation reserveARoomInMay(ReservationService serv,
    Customer customer, IRoom room, Calendar cal, int checkInDay, int checkOutDay) {

        cal.set(2022, Calendar.MAY, checkInDay);
        Date checkInDate = cal.getTime();
        cal.set(2022, Calendar.MAY, checkOutDay);
        Date checkOutDate = cal.getTime();
        return serv.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    private static void testFindRooms(ReservationService serv, Calendar cal,
                                      int checkInDay, int checkOutDay) {

        cal.set(2022, Calendar.MAY, checkInDay);
        Date checkInDate = cal.getTime();
        cal.set(2022, Calendar.MAY, checkOutDay);
        Date checkOutDate = cal.getTime();
        System.out.println("Available rooms for " + checkInDay + " - " +
                checkOutDay + " May");
        Collection<IRoom> availableRooms = serv.findRooms(checkInDate,
            checkOutDate);
        if (! availableRooms.isEmpty()) {
            for (IRoom aRoom: availableRooms) {
                System.out.println(aRoom);
            }
        } else {
            System.out.println("Nothing free");
        }
        System.out.println();
    }
}
