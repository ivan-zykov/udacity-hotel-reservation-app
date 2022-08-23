package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuServiceOtherTest {

    private MainMenuService mainMenuService;

    @Mock
    HotelResource hotelResource;
    @Mock
    Scanner scanner;
    @Mock
    ConsolePrinter consolePrinter;

    @BeforeEach
    void init() {
        mainMenuService = new MainMenuService(null, hotelResource, scanner, null, consolePrinter);
    }

    @Test
    void printMenu() {
        // Run this test
        mainMenuService.printMenu();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Welcome to Vanya's Hotel " +
                        "Reservation App"),
                () -> verify(consolePrinter, times(1)).print("1. Find and reserve a room"),
                () -> verify(consolePrinter, times(1)).print("2. See my reservations"),
                () -> verify(consolePrinter, times(1)).print("3. Create an account"),
                () -> verify(consolePrinter, times(1)).print("4. Admin"),
                () -> verify(consolePrinter, times(1)).print("5. Exit"),
                () -> verify(consolePrinter, times(1)).print("Please enter a number to select a" +
                        " menu option")
        );
    }

    @Test
    void showCustomersReservations_invalidEmail() {
        // Stub user input invalid email
        when(scanner.nextLine()).thenReturn("a", "i@z.com");

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Please enter your email"),
                () -> verify(consolePrinter, times(1)).print("It is not a valid email. Please " +
                                "enter like example@mail.com")
        );
    }

    @Test
    void showCustomersReservations_notRegistered() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email);

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Please enter your email"),
                () -> verify(consolePrinter, times(1)).print("You are still not registered with " +
                                "this email. Please create an account")
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

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Please enter your email"),
                () -> verify(consolePrinter, times(1)).print("You still have no reservations " +
                        "with us")
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

        // Run this test
        mainMenuService.showCustomersReservations();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Your reservations:"),
                () -> verify(consolePrinter, times(1)).print(reservation)
        );
    }

    @Test
    void createNewAccount_invalidEmail() {
        // Stub user's input: wrong email, rest is ok to terminate the test
        when(scanner.nextLine()).thenReturn("a", "i@z.com", "I", "Z");

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter your email"),
                () -> verify(consolePrinter, times(1)).print("It is not a valid email. Please " +
                        "enter like example@mail.com")
        );
    }

    @Test
    void createNewAccount_customerExists() {
        // Stub user's input: already registered email, rest to terminate the test
        String emailRegistered = "i@z.com";
        String emailNew = "j@r.com";
        when(scanner.nextLine()).thenReturn(emailRegistered, emailNew, "J", "R");

        // Stub that customer already exists
        var customer = new Customer("I", "Z", emailRegistered);
        when(hotelResource.getCustomer(emailRegistered)).thenReturn(customer);

        // Stub that other customer is new
        when(hotelResource.getCustomer(emailNew)).thenReturn(null);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> verify(consolePrinter, times(2)).print("Enter your email"),
                () -> verify(consolePrinter, times(1)).print("Customer with this email already " +
                        "registered.")
        );
    }

    @Test
    void createNewAccount_invalidFirstName() {
        // Stub user's input: invalid first name, rest to terminate the test
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email, "&", "I", "Z");

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter your first name"),
                () -> verify(consolePrinter, times(1)).print("Your first name should have at " +
                        "least one letter.")
        );
    }

    @Test
    void createNewAccount_invalidLastName() {
        // Stub user's input: wrong last name, rest to terminate the test
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn(email, "I", "*", "Z");

        // Stub that customer not registered
        when(hotelResource.getCustomer(email)).thenReturn(null);

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter your last name"),
                () -> verify(consolePrinter, times(1)).print("Your last name should have at " +
                        "least one letter.")
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

        // Run this test
        mainMenuService.createNewAccount();

        assertAll(
                () -> verify(hotelResource, times(1)).createACustomer(email, firstName, lastName),
                () -> verify(consolePrinter, times(1)).print("Your account successfully created")
        );
    }
}
