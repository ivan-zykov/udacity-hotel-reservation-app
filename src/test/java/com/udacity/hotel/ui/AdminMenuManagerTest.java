package com.udacity.hotel.ui;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminMenuManagerTest {

    private AdminMenuManager adminMenuManager;

    private static ByteArrayOutputStream outContent;

    @Mock
    private Scanner scanner;
    @Mock
    private ExitHelper exitHelper;
    @Mock
    private AdminMenuService adminMenuService;

    @BeforeAll
    static void initAll() {
        // Overtake printing to the console
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @BeforeEach
    void init() {
        adminMenuManager = new AdminMenuManager(scanner, exitHelper, adminMenuService);
        when(exitHelper.exit()).thenReturn(true);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void printMenu_returnToMainMenu() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("5");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).printMenu();
        verify(adminMenuService, times(1)).notifyReturningToMainMenu();
    }

    @Test
    void showAllCustomers() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("1");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).printMenu();
        verify(adminMenuService, times(1)).showAllCustomers();
    }

    @Test
    void showAllRooms() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("2");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).showAllRooms();
    }

    @Test
    void showAllReservations() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("3");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).showAllReservations();
    }

    @Test
    void addARoom() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("4");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).addARoom();
    }

    @Test
    void nonExistingMenuNumber() {
        // Stub user's input
        when(scanner.nextLine()).thenReturn("99");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).notifyNonExistingMenuNumber();
    }

    @Test
    void numberFormatException() {
        // Stub scanner: wrong menu number
        when(scanner.nextLine()).thenReturn("a");

        // Run this test
        adminMenuManager.open();

        verify(adminMenuService, times(1)).menuNotANumber();
    }

    @ParameterizedTest(name = "[{index}] {0}; Message: {1}")
    @MethodSource("provideExceptionAndMessage")
    void handleIllegalArgumentException_andUnknownError(Exception exception, String message) {
        doThrow(exception)
                .when(adminMenuService)
                .printMenu();

        // Run this test
        adminMenuManager.open();

        assertTrue(outContent.toString().endsWith(message + "\r\n"));
    }

    private static Stream<Arguments> provideExceptionAndMessage() {
        String message = "Test message";
        return Stream.of(
                Arguments.of(new IllegalArgumentException(message), message),
                Arguments.of(new RuntimeException(message), "Unknown error occurred.\r\n" + message)
        );
    }
}
