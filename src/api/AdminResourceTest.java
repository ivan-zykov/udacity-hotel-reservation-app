package api;

import model.*;
import service.CustomerService;
import service.ReservationService;

import java.util.*;

public class AdminResourceTest {

    public static void main(String[] args) {

        System.out.println("--- Start AdminResource test ---");
        System.out.println();

        // Instantiate SUT
        CustomerService customerService = CustomerService.getInstance();
        ReservationService reservationService = ReservationService.getInstance();
        AdminResource adminResource = AdminResource.getInstance(customerService,
                reservationService);

        System.out.println("--- Testing getCustomer() ---");
        System.out.println();

        // Create a customer
        String vanyasEmail = "vanya@bk.ru";
        customerService.addCustomer(vanyasEmail, "Vanya", "Zet");
        Customer vanya = adminResource.getCustomer(vanyasEmail);
        System.out.println(vanya);
        System.out.println();

        System.out.println("--- Testing addRoom() and getAllRooms() ---");
        System.out.println();

        // Create new rooms
        IRoom room101 = new Room("101", 110.0, RoomType.DOUBLE);
        IRoom room102 = new FreeRoom("102", RoomType.SINGLE);
        List<IRoom> newRooms = new ArrayList<>();
        newRooms.add(room101);
        newRooms.add(room102);
        adminResource.addRoom(newRooms);

        Collection<IRoom> allRooms = adminResource.getAllRooms();
        for (IRoom aRoom: allRooms) {
            System.out.println(aRoom);
        }
        System.out.println();

        System.out.println("--- Testing getAllCustomers() ---");
        System.out.println();

        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        for (Customer aCustomer: allCustomers) {
            System.out.println(aCustomer);
        }
        System.out.println();

        System.out.println("--- Testing displayAllReservations() ---");
        System.out.println();

        // Reserve a room
        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10);
        Date checkIn = cal.getTime();
        cal.set(2022, Calendar.MAY, 17);
        Date checkOut = cal.getTime();
        reservationService.reserveARoom(vanya, room102, checkIn, checkOut);

        adminResource.displayAllReservations();
        System.out.println();

        System.out.println("--- End AdminResource test ---");
    }
}
