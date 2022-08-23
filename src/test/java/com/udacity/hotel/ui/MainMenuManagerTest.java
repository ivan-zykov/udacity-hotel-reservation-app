package com.udacity.hotel.ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainMenuManagerTest {

    private MainMenuManager mainMenuManager;

    @Mock
    private AdminMenuManager adminMenuManager;
    @Mock
    private MainMenuService mainMenuService;
    @Mock
    private Scanner scanner;
    @Mock
    private ConsolePrinter consolePrinter;

    @BeforeEach
    void init() {
        mainMenuManager = new MainMenuManager(adminMenuManager, mainMenuService, scanner, consolePrinter);
    }

    @Test
    void printMenu_exitTheApp() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(1)).printMenu();
        verify(consolePrinter, times(1)).print("Exiting the app");
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

        verify(consolePrinter, times(1)).print("Exiting the app");
        verify(scanner, times(1)).close();
    }

    @Test
    void nonExistingMenuNumber() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("99", "5");

        // Run this test
        mainMenuManager.open();

        verify(consolePrinter, times(1)).print("Please enter a number representing " +
                "a menu option from above");
    }

    @Test
    void inputMenuOptionNotNumber() {
        // Stub user's input: menu not a number
        when(scanner.nextLine()).thenReturn("a", "5");

        // Run this test
        mainMenuManager.open();

        verify(mainMenuService, times(2)).printMenu();
        verify(consolePrinter, times(1)).print("Please enter a number");
    }

    @Test
    void handleIllegalArgumentException() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("1", "5");

        // Stub throwing IllegalArgumentException
        String message = "Test message";
        doThrow(new IllegalArgumentException(message))
                .when(mainMenuService)
                .findAndReserveARoom();

        // Run this test
        mainMenuManager.open();

        verify(consolePrinter, times(1)).print(message);
    }

    @Test
    void handleUnknownException() {
        // Stub user's input
        String email = "i@z.com";
        when(scanner.nextLine()).thenReturn("1", email, "5");

        // Stub throwing RuntimeException
        String message = "Test message";
        doThrow(new RuntimeException(message))
                .when(mainMenuService)
                .findAndReserveARoom();

        // Run this test
        mainMenuManager.open();

        verify(consolePrinter, times(1)).print("Unknown error occurred.");
        verify(consolePrinter, times(1)).print(message);
    }
}
