package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

/**
 * For other methods except adding a room.
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuServiceOtherTest {

    private AdminMenuService adminMenuService;

    @Mock
    AdminResource adminResource;
    @Mock
    ConsolePrinter consolePrinter;

    @BeforeEach
    void init() {
        adminMenuService = new AdminMenuService(adminResource, null, consolePrinter);
    }

    @Test
    void printMenu() {
        // Run this test
        adminMenuService.printMenu();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Admin menu of Vanya's Hotel " +
                        "Reservation App"),
                () -> verify(consolePrinter, times(1)).print("1. See all Customers"),
                () -> verify(consolePrinter, times(1)).print("2. See all Rooms"),
                () -> verify(consolePrinter, times(1)).print("3. See all Reservations"),
                () -> verify(consolePrinter, times(1)).print("4. Add a room"),
                () -> verify(consolePrinter, times(1)).print("5. Back to Main Menu"),
                () -> verify(consolePrinter, times(1)).print("Select a menu option")
        );
    }

    @Test
    void showAllCustomers_empty() {
        // Stub adminResource
        when(adminResource.getAllCustomers()).thenReturn(List.of());

        // Run this test
        adminMenuService.showAllCustomers();

        verify(consolePrinter, times(1)).print("There are no registered customers yet. You can" +
                " add one in main menu");
    }

    @Test
    void showAllCustomers_one() {
        // Stub adminResource
        var customer = new Customer("I", "Z", "i@z.com");
        when(adminResource.getAllCustomers()).thenReturn(List.of(customer));

        // Run this test
        adminMenuService.showAllCustomers();

        verify(consolePrinter, times(1)).print(customer);
    }

    @Test
    void showAllRooms_empty() {
        // Stub adminResource
        when(adminResource.getAllRooms()).thenReturn(List.of());

        // Run this test
        adminMenuService.showAllRooms();

        verify(consolePrinter, times(1)).print("There are no rooms yet. Please add some");
    }

    @Test
    void showAllRooms_one() {
        // Stub adminResource
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(adminResource.getAllRooms()).thenReturn(List.of(room));

        // Run this test
        adminMenuService.showAllRooms();

        verify(consolePrinter, times(1)).print(room);
    }

    @Test
    void showAllReservations_noReservations() {
        // Stub having no reservations
        when(adminResource.getAllReservations()).thenReturn(Set.of());

        // Run this test
        adminMenuService.showAllReservations();

        verify(consolePrinter, times(1)).print("There are still no reservations");
    }

    @Test
    void showAllReservations_oneReservation() {
        // Create a reservation
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        var customerI = new Customer("I", "Z", "i@z.com");
        Calendar cal = Calendar.getInstance();
        int year = 2023;
        int month = Calendar.MAY;
        int day = 20;
        cal.set(year, month, day);
        Date checkIn = cal.getTime();
        cal.add(Calendar.DATE, 7);
        Date checkOut = cal.getTime();
        var reservationFactory = new ReservationFactory();
        Reservation reservation = reservationFactory.create(customerI, room, checkIn, checkOut);

        // Stub having one reservation
        when(adminResource.getAllReservations()).thenReturn(Set.of(reservation));

        // Run this test
        adminMenuService.showAllReservations();

        verify(consolePrinter, times(1)).print(reservation);
    }
}
