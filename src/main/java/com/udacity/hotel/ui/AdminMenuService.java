package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.*;

import java.util.*;

/**
 * Performs an action selected in the {@link AdminMenuManager}.
 * <p>Actions include:</p>
 * <ul>
 *     <li>printing menu</li>
 *     <li>printing all {@link Customer}s recorded so far</li>
 *     <li>printing all {@link IRoom}s</li>
 *     <li>printing all {@link Reservation}s</li>
 *     <li>recording one or multiple new rooms with data provided by the console input</li>
 *     <li>navigating to the main menu</li>
 * </ul>
 *
 * @author Ivan V. Zykov
 */
public class AdminMenuService extends MenuService {
    private final AdminResource adminResource;
    private final ConsolePrinter consolePrinter;

    /**
     * Constructor of this class.
     *
     * @param adminResource     adminResource object of the API to services with functionality for admin users
     * @param scanner           scanner object that reads user's input
     * @param consolePrinter    consolePrinter object that prints objects to the console
     */
    public AdminMenuService(AdminResource adminResource, Scanner scanner, ConsolePrinter consolePrinter) {
        super(scanner);
        this.adminResource = adminResource;
        this.consolePrinter = consolePrinter;
    }

    /**
     * Prints admin menu to the console.
     */
    @Override
    public void printMenu() {
        consolePrinter.print("");
        consolePrinter.print("Admin menu of Vanya's Hotel Reservation App");
        consolePrinter.print("----------------------------------------");
        consolePrinter.print("1. See all Customers");
        consolePrinter.print("2. See all Rooms");
        consolePrinter.print("3. See all Reservations");
        consolePrinter.print("4. Add a room");
        consolePrinter.print("5. Back to Main Menu");
        consolePrinter.print("----------------------------------------");
        consolePrinter.print("Select a menu option");
    }

    /**
     * Gets all customers using admin resource and if any present, prints them to the console.
     */
    public void showAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        if (allCustomers.isEmpty()) {
            consolePrinter.print("There are no registered customers yet. You can add one in main menu");
            return;
        }
        for (Customer aCustomer: allCustomers) {
            consolePrinter.print(aCustomer);
        }
    }

    /**
     * Gets all rooms using admin resource and if any present, prints them to the console.
     */
    public void showAllRooms() {
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        if (allRooms.isEmpty()) {
            consolePrinter.print("There are no rooms yet. Please add some");
            return;
        }
        for (IRoom aRoom: allRooms) {
            consolePrinter.print(aRoom);
        }
    }

    /**
     * Gets all reservations using admin resource and if any present, prints them to the console.
     */
    public void showAllReservations() {
        Set<Reservation> allReservations = adminResource.getAllReservations();
        if (allReservations.isEmpty()) {
            consolePrinter.print("There are still no reservations");
            return;
        }
        for (Reservation reservation: allReservations) {
            consolePrinter.print(reservation);
        }
    }

    /**
     * Creates multiple or a single new room with properties from administrator's input and records it.
     */
    public void addARoom() {
        List<IRoom> newRooms = new ArrayList<>();
        boolean keepAddingRooms = true;
        while (keepAddingRooms) {
            String roomNumber = readRoomNumber(newRooms);
            double roomPrice = readRoomPrice();
            RoomType roomType = readRoomType();
            newRooms.add(new Room(roomNumber, roomPrice, roomType));
            keepAddingRooms = readAddingAnotherRoom();
        }
        adminResource.addRoom(newRooms);
        consolePrinter.print("Rooms were successfully added");
    }

    private String readRoomNumber(List<IRoom> newRooms) {
        consolePrinter.print("Enter room number");
        String input = "";
        boolean isBadRoomNumber = true;
        while (isBadRoomNumber) {
            input = scanner.nextLine();
            if (! isNumber(input)) {
                consolePrinter.print("Room number should be an integer number");
                continue;
            }
            if (! isNewRoomNumber(newRooms, input)) {
                consolePrinter.print("You have already added a room with " +
                        "room number " + input);
            } else {
                isBadRoomNumber = false;
            }
        }
        return input;
    }

    private boolean isNewRoomNumber(List<IRoom> newRooms, String roomNumber) {
        for (IRoom aRoom: newRooms) {
            if (aRoom.getRoomNumber().equals(roomNumber)) {
                return false;
            }
        }
        return true;
    }

    private double readRoomPrice() {
        consolePrinter.print("Enter room price");
        boolean isBadRoomPrice = true;
        String input = "";
        while (isBadRoomPrice) {
            input = scanner.nextLine();
            if (! isNumber(input)) {
                consolePrinter.print("Room price should be a decimal number");
                continue;
            }
            isBadRoomPrice = false;
        }
        return Double.parseDouble(input);
    }

    private RoomType readRoomType() {
        consolePrinter.print("Choose room type. \"s\" for single or " +
                "\"d\" for double");
        RoomType roomType = null;
        boolean isBadRoomType = true;
        while (isBadRoomType) {
            String input = scanner.nextLine();
            switch (input) {
                case "d", "D" -> {
                    isBadRoomType = false;
                    roomType = RoomType.DOUBLE;
                }
                case "s", "S" -> {
                    isBadRoomType = false;
                    roomType = RoomType.SINGLE;
                }
                default -> consolePrinter.print("Enter \"s\" for single or \"d\" " +
                        "for double");
            }
        }
        return roomType;
    }

    private boolean readAddingAnotherRoom() {
        consolePrinter.print("Add another room? (y/n)");
        boolean keepAddingRooms = true;
        boolean isBadInput = true;
        while (isBadInput) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y" ->
                    // Restart inner while loop
                        isBadInput = false;
                case "n" -> {
                    // Exit both loops
                    isBadInput = false;
                    keepAddingRooms = false;
                }
                default -> // Keep inside inner loop
                        consolePrinter.print("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return keepAddingRooms;
    }
}
