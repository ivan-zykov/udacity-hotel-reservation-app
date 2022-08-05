package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * For handling exceptions.
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuExceptionsTest {

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
    void numberFormatException() {
        // Stub scanner: wrong menu number
        when(scanner.nextLine()).thenReturn("a");

        // Run this test
        adminMenu.open();

        assertTrue(outContent.toString().endsWith("Please enter a number\r\n"));
    }

    @ParameterizedTest(name = "[{index}] {0}; Message: {1}")
    @MethodSource("provideExceptionAndMessage")
    void roomAlreadyRecorded_andUnknownError(Exception exception, String message) {
        // Stub scanner: menu, room number, price, room type, no (add just one room)
        String roomNumber = "9";
        String roomPrice = "100";
        when(scanner.nextLine()).thenReturn("4", roomNumber, roomPrice, "s", "n");

        List<IRoom> newRooms = List.of(new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE));
        doThrow(exception)
                .when(adminResource)
                .addRoom(newRooms);

        // Run this test
        adminMenu.open();

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
