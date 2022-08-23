package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

/**
 * For method adding a room.
 */
@ExtendWith(MockitoExtension.class)
class AdminMenuServiceAddRoomTest {

    private AdminMenuService adminMenuService;

    @Mock
    AdminResource adminResource;
    @Mock
    Scanner scanner;
    @Mock
    ConsolePrinter consolePrinter;

    @BeforeEach
    void init() {
        adminMenuService = new AdminMenuService(adminResource, scanner, consolePrinter);
    }

    @Test
    void addARoom_wrongRoomNumber() {
        // Stub user's input: wrong room number, rest to terminate the test
        when(scanner.nextLine()).thenReturn("a", "1", "10", "s", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter room number"),
                () -> verify(consolePrinter, times(1)).print("Room number should be an integer" +
                        " number")
        );
    }

    @Test
    void addARoom_roomAlreadyAdded() {
        /*
          Stub user's input: room number, price, room type, yes add other room, duplicate
          room number, rest to terminate the test
         */
        String roomNumber = "9";
        when(scanner.nextLine()).thenReturn(roomNumber,"10", "s", "y", roomNumber, "2", "20", "s", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> verify(consolePrinter, times(2)).print("Enter room number"),
                () -> verify(consolePrinter, times(1)).print("You have already added a room with" +
                                " room number " + roomNumber)
        );
    }

    @Test
    void addRoom_wrongRoomPrice() {
        // Stub user's input: menu, room number, wrong price, rest to terminate the test
        when(scanner.nextLine()).thenReturn("9", "p", "10", "s", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Enter room price"),
                () -> verify(consolePrinter, times(1)).print("Room price should be a decimal" +
                        " number")
        );
    }

    @Test
    void addRoom_wrongRoomType() {
        // Stub user's input: room number, price, wrong room type, rest to terminate the test
        when(scanner.nextLine()).thenReturn("9", "100", "a", "s", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Choose room type. \"s\" for single" +
                                " or \"d\" for double"),
                () -> verify(consolePrinter, times(1)).print("Enter \"s\" for single or \"d\" " +
                        "for double")
        );
    }

    @Test
    void addRoom_wrongYesNo() {
        // Stub user's input: room number, price, room type, wrong yes or no, "n" to terminate the test
        when(scanner.nextLine()).thenReturn("9", "100", "s", "a", "n");

        // Run this test
        adminMenuService.addARoom();

        assertAll(
                () -> verify(consolePrinter, times(1)).print("Add another room? (y/n)"),
                () -> verify(consolePrinter, times(1)).print("Enter \"y\" for yes or \"n\" for no")
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

        List<IRoom> newRooms = List.of(new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE));
        assertAll(
                () -> verify(consolePrinter, times(1)).print("Rooms were successfully added"),
                () -> verify(adminResource, times(1)).addRoom(newRooms)
        );
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


        List<IRoom> newRooms = List.of(
                new Room(roomNumber, Double.parseDouble(roomPrice), RoomType.SINGLE),
                new Room(otherRoomNumber, Double.parseDouble(otherRoomPrice), RoomType.DOUBLE)
        );
        assertAll(
                () -> verify(consolePrinter, times(1)).print("Rooms were successfully added"),
                () -> verify(adminResource, times(1)).addRoom(newRooms)
        );
    }
}
