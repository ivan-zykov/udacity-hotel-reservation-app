package com.udacity.hotel.ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMenuManagerTest {

    private AdminMenuManager adminMenuManager;

    @Mock
    private Scanner scanner;
    @Mock
    private AdminMenuService adminMenuService;
    @Mock
    private ConsolePrinterImpl consolePrinter;

    @BeforeEach
    void init() {
        adminMenuManager = new AdminMenuManager(scanner, adminMenuService, consolePrinter);
    }

    @Test
    void printMenu_returnToMainMenu() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).printMenu();
        verify(consolePrinter, times(1)).print("Returning to the main menu");
    }

    @Test
    void showAllCustomers() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("1", "5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).showAllCustomers();
    }

    @Test
    void showAllRooms() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("2", "5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).showAllRooms();
    }

    @Test
    void showAllReservations() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("3", "5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).showAllReservations();
    }

    @Test
    void addARoom() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("4", "5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).addARoom();
    }

    @Test
    void nonExistingMenuNumber() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("99", "5");

        // Run this test
        adminMenuManager.open();

        verify(consolePrinter, times(1))
                .print("Please enter a number representing a menu option from above");
    }

    @Test
    void numberFormatException() {
        // Stub scanner: wrong menu number
        when(scanner.nextLine()).thenReturn("a", "5");

        // Run this test
        adminMenuManager.open();

        verify(consolePrinter, times(1)).print("Please enter a number");
    }

    @ParameterizedTest(name = "[{index}] {0}; Message: {1}")
    @MethodSource("provideExceptionAndMessage")
    void handleIllegalArgumentException_andUnknownError(Exception exception, String message) {
        // Stub user's input: exit the app
        when(scanner.nextLine()).thenReturn("1", "5");

        doThrow(exception)
                .when(adminMenuService)
                .showAllCustomers();

        // Run this test
        adminMenuManager.open();

        verify(consolePrinter, times(1)).print(message);
    }

    private static Stream<Arguments> provideExceptionAndMessage() {
        String message = "Test message";
        return Stream.of(
                Arguments.of(new IllegalArgumentException(message), message),
                Arguments.of(new RuntimeException(message), message)
        );
    }
}
