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
        Reservation res = reserveARoomInMay(serv, vanya, room102, cal,10,
                17);

        System.out.println("New reservation: ");
        System.out.println(res);
        System.out.println();

        // Test attempting to reserve a booked room
        try {
            reserveARoomInMay(serv, vanya, room102, cal, 10,
                    17);
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        System.out.println("--- Testing printAllReservations() ---");
        System.out.println("All reservations:");
        serv.printAllReservations();
        System.out.println();

        System.out.println("--- Testing findRooms() ---");

        // Check for 9 - 10 May. Expect free rooms: 102, 103
        testFindRooms(serv, cal, 9, 10);

        // Check for 10 - 11 May. Expect nothing free, but recommendation
        // Make no rooms available for 10 - 11 May
        // Book room 103, 10 - 11 May
        reserveARoomInMay(serv, vanya, room103, cal, 10, 11);
        testFindRooms(serv, cal, 10, 11);

        // Check for 16 - 17 May. Expect free rooms: 103
        testFindRooms(serv, cal, 16, 17);

        // Check for 17 - 18 May. Expect nothing free and no recommendation
        // Make no rooms available for 17 - 18 May and the next 7 days
        // Book room 102, 17 - 18 May
        reserveARoomInMay(serv, vanya, room102, cal, 17, 18);
        // Book room 103, 17 - 18 May
        reserveARoomInMay(serv, vanya, room103, cal, 17, 18);
        // Book room 102, 24 - 25 May
        reserveARoomInMay(serv, vanya, room102, cal, 24, 25);
        // Book room 103, 24 - 25 May
        reserveARoomInMay(serv, vanya, room103, cal, 24, 25);
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

        cal.set(2022, 04, checkInDay);
        Date checkInDate = cal.getTime();
        cal.set(2022, 04, checkOutDay);
        Date checkOutDate = cal.getTime();
        return serv.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    private static void testFindRooms(ReservationService serv, Calendar cal,
                                      int checkInDay, int checkOutDay) {

        cal.set(2022, 04, checkInDay);
        Date checkInDate = cal.getTime();
        cal.set(2022, 04, checkOutDay);
        Date checkOutDate = cal.getTime();
        System.out.println("Available rooms for " + checkInDay + " - " +
                checkOutDay + " May");
        Collection<IRoom> availableRooms = serv.findRooms(checkInDate,
            checkOutDate);
        if (! availableRooms.isEmpty()) {
            for (IRoom aRoom: availableRooms) {
                System.out.println(aRoom);
            }
        }
        System.out.println();
    }
}
