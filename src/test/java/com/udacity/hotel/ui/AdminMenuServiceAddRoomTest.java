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
import static org.mockito.Mockito.*;

/**
 * For method adding a room.
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuServiceAddRoomTest {

    private AdminMenuService adminMenuService;

    private static ByteArrayOutputStream outContent;

    @Mock
    AdminResource adminResource;
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
        adminMenuService = new AdminMenuService(adminResource, scanner, exitHelper);
        when(exitHelper.exit()).thenReturn(true);
    }

    @AfterAll
    static void cleanAll() {
        // Restore the standard out
        System.setOut(System.out);
    }

    @Test
    void addARoom_wrongRoomNumber() {
        // Stub user's input: wrong room number
        when(scanner.nextLine()).thenReturn("a");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().endsWith("Room number should be an integer number" +
                        System.lineSeparator()))
        );
    }

    @Test
    void addARoom_roomAlreadyAdded() {
        /*
          Stub user's input: room number, price, room type, yes add other room, duplicate
          room number
         */
        String roomNumber = "9";
        when(scanner.nextLine()).thenReturn(roomNumber,"10", "s", "y", roomNumber);

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().endsWith("You have already added a room with " +
                        "room number " + roomNumber + System.lineSeparator()))
        );
    }

    @Test
    void addRoom_wrongRoomPrice() {
        // Stub user's input: menu, room number, wrong price
        when(scanner.nextLine()).thenReturn("9", "p");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().endsWith("Room price should be a decimal number" +
                        System.lineSeparator()))
        );
    }

    @Test
    void addRoom_wrongRoomType() {
        // Stub user's input: room number, price, wrong room type
        when(scanner.nextLine()).thenReturn("9", "100", "a");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().contains("Choose room type. \"s\" for single or " +
                        "\"d\" for double" + System.lineSeparator())),
                () -> assertTrue(outContent.toString().endsWith("Enter \"s\" for single or \"d\" for double" +
                        System.lineSeparator()))
        );
    }

    @Test
    void addRoom_wrongYesNo() {
        // Stub user's input: room number, price, room type, wrong yes or no
        when(scanner.nextLine()).thenReturn("9", "100", "s", "a");

        // Force exiting the app after incorrect input
        when(exitHelper.exitNested()).thenReturn(true);

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().contains("Choose room type. \"s\" for single or " +
                        "\"d\" for double" + System.lineSeparator())),
                () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
                () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no" +
                        System.lineSeparator()))
        );
    }

    @Test
    void addRoom_one() {
        // Stub scanner: menu, room number, price, room type, no (add just one room)
        String roomNumber = "9";
        String roomPrice = "100";
        when(scanner.nextLine()).thenReturn("9", "100", "s", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().contains("Choose room type. \"s\" for single or " +
                        "\"d\" for double" + System.lineSeparator())),
                () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
                () -> assertTrue(outContent.toString().endsWith("Rooms were successfully added" +
                        System.lineSeparator()))
        );

        List<IRoom> newRooms = List.of(new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE));
        verify(adminResource, times(1)).addRoom(newRooms);
    }

    @Test
    void addRoom_two() {
        // Stub user's input
        String roomNumber = "9";
        String roomPrice = "100";
        String otherRoomNumber = "8";
        String otherRoomPrice = "200";
        when(scanner.nextLine()).thenReturn(
                roomNumber,
                roomPrice,
                "s", // room type
                "y", // yes (add another room)
                otherRoomNumber,
                otherRoomPrice,
                "d", // type
                "n" // no (add those two rooms)
        );

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().contains("Choose room type. \"s\" for single or " +
                        "\"d\" for double" + System.lineSeparator())),
                () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
                () -> assertTrue(outContent.toString().endsWith("Rooms were successfully added" +
                        System.lineSeparator()))
        );

        List<IRoom> newRooms = List.of(
                new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE),
                new Room(otherRoomNumber, Double.parseDouble(otherRoomPrice), RoomType.DOUBLE)
        );
        verify(adminResource, times(1)).addRoom(newRooms);
    }
}
