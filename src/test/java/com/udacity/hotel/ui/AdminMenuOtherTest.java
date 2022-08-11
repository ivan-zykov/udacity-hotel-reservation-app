package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * For other methods except addRoom().
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuOtherTest {

    private AdminMenu adminMenu;

    private static ByteArrayOutputStream outContent;

    @Mock
    private AdminResource adminResource;
    @Mock
    private Scanner scanner;
    @Mock
    private ExitHelper exitHelper;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        adminMenu = new AdminMenu(adminResource, scanner, exitHelper);
        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void printMenu_returnToMainMenu() {
        when(scanner.nextLine()).thenReturn("5");
        adminMenu.open();
        assertAll(
            () -> assertTrue(outContent.toString().contains("Admin menu of Vanya's Hotel Reservation App")),
            () -> assertTrue(outContent.toString().contains("1. See all Customers")),
            () -> assertTrue(outContent.toString().contains("2. See all Rooms")),
            () -> assertTrue(outContent.toString().contains("3. See all Reservations")),
            () -> assertTrue(outContent.toString().contains("4. Add a room")),
            () -> assertTrue(outContent.toString().contains("5. Back to Main Menu")),
            () -> assertTrue(outContent.toString().contains("Select a menu option")),
            () -> assertTrue(outContent.toString().endsWith("Returning to the main menu\r\n"))
        );
    }

    @Test
    void seeAllCustomers_empty() {
        // Stub scanner
        when(scanner.nextLine()).thenReturn("1");

        // Stub adminResource
        when(adminResource.getAllCustomers()).thenReturn(List.of());

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith("There are no registered customers yet. You can add " +
                "one in main menu" + "\r\n"));
    }

    @Test
    void seeAllCustomers_one() {
        // Stub scanner
        when(scanner.nextLine()).thenReturn("1");

        // Stub adminResource
        var customer = new Customer("I", "Z", "i@z.com");
        when(adminResource.getAllCustomers()).thenReturn(List.of(customer));

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith(customer + "\r\n"));
    }

    @Test
    void seeAllRooms_empty() {
        // Stub scanner
        when(scanner.nextLine()).thenReturn("2");

        // Stub adminResource
        when(adminResource.getAllRooms()).thenReturn(List.of());

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith("There are no rooms yet. Please add some" +
                "\r\n"));
    }

    @Test
    void seeAllRooms_one() {
        // Stub scanner
        when(scanner.nextLine()).thenReturn("2");

        // Stub adminResource
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        when(adminResource.getAllRooms()).thenReturn(List.of(room));

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith(room + "\r\n"));
    }

    @Test
    void seeAllReservations_noReservations() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("3");

        // Stub having no reservations
        when(adminResource.getAllReservations()).thenReturn(Set.of());

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith("There are still no reservations\r\n"));
    }

    @Test
    void seeAllReservations_oneReservation() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("3");

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
        adminMenu.open();

        assertTrue(outContent.toString().endsWith(reservation + "\r\n"));
    }

    @Test
    void nonExistingMenuNumber() {
        // Stub scanner
        when(scanner.nextLine()).thenReturn("99");

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith("Please enter a number representing" +
                "a menu option from above\r\n"));
    }
}
