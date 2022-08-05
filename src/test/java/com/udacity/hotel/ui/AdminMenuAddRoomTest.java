package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * For method addARoom().
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuAddRoomTest {

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
    void addARoom_wrongRoomNumber() {
        // Stub scanner: menu, wrong room number
        when(scanner.nextLine()).thenReturn("4","a");

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().endsWith("Room number should be an integer number" +
                "\r\n"))
        );
    }

    @Test
    void addARoom_roomAlreadyAdded() {
        /*
          Stub scanner: menu, room number, price, room type, yes add other room, duplicate
          room number
         */
        String roomNumber = "9";
        when(scanner.nextLine()).thenReturn("4", roomNumber,"10", "s", "y", roomNumber);

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().endsWith("You have already added a room with " +
                "room number " + roomNumber + "\r\n"))
        );
    }

    @Test
    void addRoom_wrongRoomPrice() {
        // Stub scanner: menu, room number, wrong price
        when(scanner.nextLine()).thenReturn("4", "9", "p");

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().contains("Enter room price")),
            () -> assertTrue(outContent.toString().endsWith("Room price should be a decimal number\r\n"))
        );
    }

    @Test
    void addRoom_wrongRoomType() {
        // Stub scanner: menu, room number, price, wrong room type
        when(scanner.nextLine()).thenReturn("4", "9", "100", "a");

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().contains("Enter room price")),
            () -> assertTrue(outContent.toString().contains("Choose room type. \"S\" for single or " +
                        "\"D\" for double\r\n")),
            () -> assertTrue(outContent.toString().endsWith("Enter \"S\" for single or \"D\" for double\r\n"))
        );
    }

    @Test
    void addRoom_wrongYesNo() {
        // Stub scanner: menu, room number, price, room type, wrong yes or no
        when(scanner.nextLine()).thenReturn("4", "9", "100", "s", "a");

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().contains("Enter room price")),
            () -> assertTrue(outContent.toString().contains("Choose room type. \"S\" for single or " +
                        "\"D\" for double\r\n")),
            () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
            () -> assertTrue(outContent.toString().endsWith("Enter \"y\" for yes or \"n\" for no\r\n"))
        );
    }

    @Test
    void addRoom_one() {
        // Stub scanner: menu, room number, price, room type, no (add just one room)
        String roomNumber = "9";
        String roomPrice = "100";
        when(scanner.nextLine()).thenReturn("4", "9", "100", "s", "n");

        // Run this test
        adminMenu.open();

        assertAll(
            () -> assertTrue(outContent.toString().contains("Enter room number")),
            () -> assertTrue(outContent.toString().contains("Enter room price")),
            () -> assertTrue(outContent.toString().contains("Choose room type. \"S\" for single or " +
                    "\"D\" for double\r\n")),
            () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
            () -> assertTrue(outContent.toString().endsWith("Rooms were successfully added\r\n"))
        );

        List<IRoom> newRooms = List.of(new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE));
        verify(adminResource, times(1)).addRoom(newRooms);
    }

    @Test
    void addRoom_two() {
        // Stub scanner
        String roomNumber = "9";
        String roomPrice = "100";
        String otherRoomNumber = "8";
        String otherRoomPrice = "200";
        when(scanner.nextLine()).thenReturn(
                "4", // menu
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
        adminMenu.open();

        assertAll(
                () -> assertTrue(outContent.toString().contains("Enter room number")),
                () -> assertTrue(outContent.toString().contains("Enter room price")),
                () -> assertTrue(outContent.toString().contains("Choose room type. \"S\" for single or " +
                        "\"D\" for double\r\n")),
                () -> assertTrue(outContent.toString().contains("Add another room? (y/n)")),
                () -> assertTrue(outContent.toString().endsWith("Rooms were successfully added\r\n"))
        );

        List<IRoom> newRooms = List.of(
                new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE),
                new Room(otherRoomNumber, Double.parseDouble(otherRoomPrice), RoomType.DOUBLE)
        );
        verify(adminResource, times(1)).addRoom(newRooms);
    }
}
