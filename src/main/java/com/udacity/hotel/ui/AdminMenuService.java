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
    private final ExitHelper exitHelper;

    /**
     * Constructor of this class.
     *
     * @param adminResource adminResource object of the API to services with functionality for admin users
     * @param scanner       scanner object that reads user's input
     * @param exitHelper    exitHelper object that allows breaking loops during tests
     */
    public AdminMenuService(AdminResource adminResource, Scanner scanner, ExitHelper exitHelper) {
        super(scanner);
        this.adminResource = adminResource;
        this.exitHelper = exitHelper;
    }

    /**
     * Prints admin menu to the console.
     */
    public void printMenu() {
        print("");
        print("Admin menu of Vanya's Hotel Reservation App");
        print("----------------------------------------");
        print("1. See all Customers");
        print("2. See all Rooms");
        print("3. See all Reservations");
        print("4. Add a room");
        print("5. Back to Main Menu");
        print("----------------------------------------");
        print("Select a menu option");
    }

    /**
     * Gets all customers using admin resource and if any present, prints them to the console.
     */
    public void showAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        if (allCustomers.isEmpty()) {
            print("There are no registered customers yet. You can add " +
                    "one in main menu");
            return;
        }
        for (Customer aCustomer: allCustomers) {
            print(aCustomer);
        }
    }

    /**
     * Gets all rooms using admin resource and if any present, prints them to the console.
     */
    public void showAllRooms() {
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        if (allRooms.isEmpty()) {
            print("There are no rooms yet. Please add some");
            return;
        }
        for (IRoom aRoom: allRooms) {
            print(aRoom);
        }
    }

    /**
     * Gets all reservations using admin resource and if any present, prints them to the console.
     */
    public void showAllReservations() {
        Set<Reservation> allReservations = adminResource.getAllReservations();
        if (allReservations.isEmpty()) {
            print("There are still no reservations");
            return;
        }
        for (Reservation reservation: allReservations) {
            print(reservation);
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
            if (exitHelper.exit() && roomNumber == null) { return; }

            double roomPrice = readRoomPrice();
            if (exitHelper.exit() && roomPrice == 0D) { return; }

            RoomType roomType = readRoomType();
            if (exitHelper.exit() && roomType == null) { return; }

            newRooms.add(new Room(roomNumber, roomPrice, roomType));

            keepAddingRooms = readAddingAnotherRoom();
            // Using ExitHelper#exitNested() here in order not to break adding just one room.
            if (exitHelper.exitNested() && ! keepAddingRooms) { return; }
        }

        adminResource.addRoom(newRooms);
        print("Rooms were successfully added");
    }

    private String readRoomNumber(List<IRoom> newRooms) {
        print("Enter room number");
        String input = scanner.nextLine();
        boolean isBadRoomNumber = true;
        while (isBadRoomNumber) {
            if (! isNumber(input)) {
                print("Room number should be an integer number");
                if (exitHelper.exit()) { return null; }
                continue;
            }
            if (! isNewRoomNumber(newRooms, input)) {
                print("You have already added a room with " +
                        "room number " + input);
                if (exitHelper.exit()) { return null; }
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
        print("Enter room price");
        boolean isBadRoomPrice = true;
        String input = "";
        while (isBadRoomPrice) {
            input = scanner.nextLine();
            if (! isNumber(input)) {
                print("Room price should be a decimal number");
                if (exitHelper.exit()) { return 0D; }
                continue;
            }
            isBadRoomPrice = false;
        }
        return Double.parseDouble(input);
    }

    private RoomType readRoomType() {
        print("Choose room type. \"s\" for single or " +
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
                default -> {
                    print("Enter \"s\" for single or \"d\" " +
                            "for double");
                    if (exitHelper.exit()) {
                        return null;
                    }
                }
            }
        }
        return roomType;
    }

    private boolean readAddingAnotherRoom() {
        print("Add another room? (y/n)");
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
                default -> {
                    // Keep inside inner loop
                    print("Enter \"y\" for yes or \"n\" for no");
                    if (exitHelper.exitNested()) {
                        return false;
                    }
                }
            }
        }
        return keepAddingRooms;
    }

    // TODO: add JavaDoc here and for other notify*() methods
    public void notifyReturningToMainMenu() {
        print("Returning to the main menu");
    }

    public void notifyNonExistingMenuNumber() {
        print("Please enter a number representing a menu option from above");
    }

    public void menuNotANumber() {
        print("Please enter a number");
    }
}
