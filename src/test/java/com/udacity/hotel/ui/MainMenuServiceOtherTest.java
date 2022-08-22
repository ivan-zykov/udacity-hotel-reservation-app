package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuServiceOtherTest {

    private MainMenuService mainMenuService;

    private static ByteArrayOutputStream outContent;

    @Mock
    HotelResource hotelResource;
    @Mock
    Scanner scanner;
    @Mock
    ExitHelper exitHelper;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        mainMenuService = new MainMenuService(null, hotelResource, scanner, exitHelper, null);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void printMenu() {
        // Run this test
        mainMenuService.printMenu();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Welcome to Vanya's Hotel Reservation App")),
                () -> assertTrue(outContent.toString().contains("1. Find and reserve a room")),
                () -> assertTrue(outContent.toString().contains("2. See my reservations")),
                () -> assertTrue(outContent.toString().contains("3. Create an account")),
                () -> assertTrue(outContent.toString().contains("4. Admin")),
                () -> assertTrue(outContent.toString().contains("5. Exit")),
                () -> assertTrue(outContent.toString().endsWith("Please enter a number to select a menu option" +
                        System.lineSeparator()))
        );
    }

    @Test
    void showCustomersReservations_invalidEmail() {
        // Stub user input invalid email
        when(scanner.nextLine()).thenReturn("a");

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().endsWith("It is not a valid email. Please enter like " +
                        "example@mail.com" + System.lineSeparator()))
        );
    }

    @Test
    void showCustomersReservations_notRegistered() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email);

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().endsWith("You are still no registered with this email. " +
                        "Please create an account" + System.lineSeparator()))
        );
    }

    @Test
    void showCustomersReservations_noReservations() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email);

        // Stub that customer is registered
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Stub having no reservations for the customer
        when(hotelResource.getCustomersReservations(email)).thenReturn(List.of());

        // Force exiting the app after the info message
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().endsWith("You still have no reservations with us" +
                        System.lineSeparator()))
        );
    }

    @Test
    void showCustomersReservations_printReservation() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email);

        // Stub that customer is registered
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Stub getting customer's reservation
        var room = new Room("1", 10.0D, RoomType.SINGLE);
        Calendar cal = Calendar.getInstance();
        cal.set(2099, Calendar.MAY, 20);
        Date checkIn = cal.getTime();
        cal.set(2099, Calendar.MAY, 21);
        Date checkOut = cal.getTime();
        var reservationFactory = new ReservationFactory();
        Reservation reservation = reservationFactory.create(customer, room, checkIn, checkOut);
        when(hotelResource.getCustomersReservations(email)).thenReturn(List.of(reservation));

        // Force exiting the app after the info message
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Your reservations:")),
                () -> assertTrue(outContent.toString().endsWith(reservation + System.lineSeparator()))
        );
    }

    @Test
    void createNewAccount_invalidEmail() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("a");

        // Force exiting the app after the error message
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter your email")),
                () -> assertTrue(outContent.toString().endsWith("It is not a valid email. Please enter like " +
                        "example@mail.com" + System.lineSeparator()))
        );
    }

    @Test
    void createNewAccount_customerExists() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email);

        // Force exiting the app after the error message
        when(exitHelper.exit()).thenReturn(true);

        // Stub that customer already exists
        var customer = new Customer("I", "Z", email);
        when(hotelResource.getCustomer(email)).thenReturn(customer);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter your email")),
                () -> assertTrue(outContent.toString().endsWith("Customer with this email already " +
                        "registered." + System.lineSeparator()))
        );
    }

    @Test
    void createNewAccount_invalidFirstName() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email, "&");

        // Force exiting the app after the error message
        when(exitHelper.exit()).thenReturn(true);

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter your first name")),
                () -> assertTrue(outContent.toString().endsWith("Your first name should have at " +
                        "least one letter." + System.lineSeparator()))
        );
    }

    @Test
    void createNewAccount_invalidLastName() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email, "I", "*");

        // Force exiting the app after the error message
        when(exitHelper.exit()).thenReturn(true);

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter your last name")),
                () -> assertTrue(outContent.toString().endsWith("Your last name should have at " +
                        "least one letter." + System.lineSeparator()))
        );
    }

    @Test
    void createNewAccount_allValid() {
        // Stub user's input
        String email = "i@z.com";
        String firstName = "I";
        String lastName = "Z";
        when(scanner.nextLine()).thenReturn(email, firstName, lastName);

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Force exiting the app after the info message
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuService.createNewAccount();

        verify(hotelResource, times(1)).createACustomer(email, firstName, lastName);

        assertTrue(outContent.toString().endsWith("Your account successfully created" + System.lineSeparator()));
    }
}
