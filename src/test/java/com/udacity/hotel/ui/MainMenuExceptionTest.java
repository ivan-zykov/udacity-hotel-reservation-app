package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * For catching exceptions.
 */
@ExtendWith(MockitoExtension.class)
class MainMenuExceptionTest {

    private MainMenuManager mainMenu;

    private static ByteArrayOutputStream outContent;

    @Mock
    private HotelResource hotelResource;
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
        mainMenu = new MainMenuManager(null, hotelResource, scanner, null, exitHelper, null);
        // Force exiting the app after incorrect input
        when(exitHelper.exit()).thenReturn(true);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void inputMenuOptionNotNumber() {
        // Stub user's input: menu not a number
        when(scanner.nextLine()).thenReturn("a");

        // Run this test
        mainMenu.open();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter a number to select a menu option")),
                () -> assertTrue(outContent.toString().endsWith("Please enter a number\r\n"))
        );
    }

    @Test
    void handleIllegalArgumentException() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("2", email);

        // Stub throwing IllegalArgumentException
        String message = "Test message";
        when(hotelResource.getCustomer(email)).thenThrow(new IllegalArgumentException(message));

        // Run this test
        mainMenu.open();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().endsWith(message + "\r\n"))
        );
    }

    @Test
    void handleUnknownException() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("2", email);

        // Stub throwing other RuntimeException
        String message = "Test message";
        when(hotelResource.getCustomer(email)).thenThrow(new RuntimeException(message));

        // Run this test
        mainMenu.open();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Please enter your email")),
                () -> assertTrue(outContent.toString().contains("Unknown error occurred.")),
                () -> assertTrue(outContent.toString().endsWith(message + "\r\n"))
        );
    }
}
