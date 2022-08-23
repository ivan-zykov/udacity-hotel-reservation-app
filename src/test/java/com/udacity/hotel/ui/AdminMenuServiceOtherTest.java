package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * For other methods except adding a room.
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuServiceOtherTest {

    private AdminMenuService adminMenuService;

    private static ByteArrayOutputStream outContent;

    @Mock
    AdminResource adminResource;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        adminMenuService = new AdminMenuService(adminResource, null, null);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void printMenu() {
        // Run this test
        adminMenuService.printMenu();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Admin menu of Vanya's Hotel Reservation App")),
                () -> assertTrue(outContent.toString().contains("1. See all Customers")),
                () -> assertTrue(outContent.toString().contains("2. See all Rooms")),
                () -> assertTrue(outContent.toString().contains("3. See all Reservations")),
                () -> assertTrue(outContent.toString().contains("4. Add a room")),
                () -> assertTrue(outContent.toString().contains("5. Back to Main Menu")),
                () -> assertTrue(outContent.toString().endsWith("Select a menu option" + System.lineSeparator()))
        );
    }

    @Test
    void showAllCustomers_empty() {
        // Stub adminResource
        when(adminResource.getAllCustomers()).thenReturn(List.of());

        // Run this test
        adminMenuService.showAllCustomers();

        assertTrue(outContent.toString().endsWith("There are no registered customers yet. You can add " +
                "one in main menu" + System.lineSeparator()));
    }

    @Test
    void showAllCustomers_one() {
        // Stub adminResource
        var customer = new Customer("I", "Z", "i@z.com");
        when(adminResource.getAllCustomers()).thenReturn(List.of(customer));

        // Run this test
        adminMenuService.showAllCustomers();

        assertTrue(outContent.toString().endsWith(customer + System.lineSeparator()));
    }

    @Test
    void showAllRooms_empty() {
        // Stub adminResource
        when(adminResource.getAllRooms()).thenReturn(List.of());

        // Run this test
        adminMenuService.showAllRooms();

        assertTrue(outContent.toString().endsWith("There are no rooms yet. Please add some" + System.lineSeparator()));
    }

    @Test
    void showAllRooms_one() {
        // Stub adminResource
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(adminResource.getAllRooms()).thenReturn(List.of(room));

        // Run this test
        adminMenuService.showAllRooms();

        assertTrue(outContent.toString().endsWith(room + System.lineSeparator()));
    }

    @Test
    void showAllReservations_noReservations() {
        // Stub having no reservations
        when(adminResource.getAllReservations()).thenReturn(Set.of());

        // Run this test
        adminMenuService.showAllReservations();

        assertTrue(outContent.toString().endsWith("There are still no reservations" + System.lineSeparator()));
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

        assertTrue(outContent.toString().endsWith(reservation + System.lineSeparator()));
    }
}
