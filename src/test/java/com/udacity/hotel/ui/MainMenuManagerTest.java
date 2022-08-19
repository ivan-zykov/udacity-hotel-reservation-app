package com.udacity.hotel.ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * For other methods except findAndReserveARoom().
 */
@ExtendWith(MockitoExtension.class)
class MainMenuManagerTest {

    private MainMenuManager mainMenuManager;

    private static ByteArrayOutputStream outContent;

    @Mock
    private AdminMenuManager adminMenuManager;
//    TODO: needed?
    @Mock
    private ExitHelper exitHelper;
    @Mock
    private MainMenuService mainMenuService;
    @Mock
    private Scanner scanner;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        mainMenuManager = new MainMenuManager(adminMenuManager, exitHelper, mainMenuService, scanner);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void printMenu_exitTheApp() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).printMenu();

        assertTrue(outContent.toString().endsWith("Exiting the app\r\n"));
    }

    @Test
    void findAndReserveARoom() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("1", "5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).findAndReserveARoom();
    }

    @Test
    void showCustomersReservations() {
        // Stub user's input:
        when(scanner.nextLine()).thenReturn("2", "5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).showCustomersReservations();
    }

    @Test
    void createNewAccount() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("3", "5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).createNewAccount();
    }

    @Test
    void goToAdminMenu() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("4", "5");

        // Run this test
        mainMenuManager.open();

        verify(adminMenuManager, times(1)).open();
    }

    @Test
    void exitApp() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("5");

        // Run this test
        mainMenuManager.open();

        assertTrue(outContent.toString().endsWith("Exiting the app\r\n"));
        verify(scanner, times(1)).close();
    }

    @Test
    void nonExistingMenuNumber() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("99", "5");

        // Run this test
        mainMenuManager.open();

        assertTrue(outContent.toString().contains("Please enter a number representing" +
                "a menu option from above\r\n"));
    }

    @Test
    void inputMenuOptionNotNumber() {
        // Stub user's input: menu not a number
        when(scanner.nextLine()).thenReturn("a");

        // Force exiting the app after exception
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).printMenu();

        assertTrue(outContent.toString().endsWith("Please enter a number\r\n"));
    }

    @Test
    void handleIllegalArgumentException() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("1");

        // Stub throwing IllegalArgumentException
        String message = "Test message";
        doThrow(new IllegalArgumentException(message))
                .when(mainMenuService)
                .findAndReserveARoom();

        // Force exiting the app after exception
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuManager.open();

        assertTrue(outContent.toString().endsWith(message + "\r\n"));
    }

    @Test
    void handleUnknownException() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("1", email);

        // Stub throwing RuntimeException
        String message = "Test message";
        doThrow(new RuntimeException(message))
                .when(mainMenuService)
                .findAndReserveARoom();

        // Force exiting the app after exception
        when(exitHelper.exit()).thenReturn(true);

        // Run this test
        mainMenuManager.open();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Unknown error occurred.")),
                () -> assertTrue(outContent.toString().endsWith(message + "\r\n"))
        );
    }
}
