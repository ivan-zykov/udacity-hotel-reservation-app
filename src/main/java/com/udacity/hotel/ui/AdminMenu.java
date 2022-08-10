package com.udacity.hotel.ui;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Reservation;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Console UI for administrators.
 *
 * @author Ivan V. Zykov
 */
public final class AdminMenu extends Menu {

    private final AdminResource adminResource;
    private final ExitHelper exitHelper;

    /**
     * Constructor of this class.
     *
     * @param adminResource adminResource object of the API to services with functionality for admins
     * @param scanner       scanner object that reads user's input
     */
    public AdminMenu(AdminResource adminResource, Scanner scanner, ExitHelper exitHelper) {
        super(scanner);
        this.adminResource = adminResource;
        this.exitHelper = exitHelper;
    }

    /**
     * Prints menu to the console, reads administrator's input for the selected menu option and performs the
     * corresponding action utilising APIs to services.
     * <p>Performed actions include:</p>
     * <ul>
     *     <li>printing all {@link Customer}s recorded so far</li>
     *     <li>printing all {@link IRoom}s</li>
     *     <li>printing all {@link Reservation}s</li>
     *     <li>recording one or multiple new rooms with data provided by the console input</li>
     *     <li>navigating to the main menu</li>
     * </ul>
     */
    public void open() {

        boolean keepRunning = true;

        while (keepRunning) {
            try {
                printMenu();

                int input = Integer.parseInt(scanner.nextLine());

                switch (input) {
                    case 1:
                        seeAllCustomers();
                        if (exitHelper.exit()) { return; }
                        break;
                    case 2:
                        seeAllRooms();
                        if (exitHelper.exit()) { return; }
                        break;
                    case 3:
                        seeAllReservations();
                        if (exitHelper.exit()) { return; }
                        break;
                    case 4:
                        addARoom();
                        if (exitHelper.exit()) { return; }
                        break;
                    case 5:
                        System.out.println("Returning to the main menu");
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Please enter a number representing" +
                                "a menu option from above");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            } catch (Exception ex) {
                System.out.println("Unknown error occurred.");
                System.out.println(ex.getLocalizedMessage());
            }
            if (exitHelper.exit()) { return; }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Admin menu of Vanya's Hotel Reservation App");
        System.out.println("----------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to Main Menu");
        System.out.println("----------------------------------------");
        System.out.println("Select a menu option");
    }

    private void seeAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();

       if (allCustomers.isEmpty()) {
           System.out.println("There are no registered customers yet. You can add " +
                   "one in main menu");
           return;
       }

       for (Customer aCustomer: allCustomers) {
           System.out.println(aCustomer);
       }
    }

    private void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private void addARoom() {

        // TODO: add option to add a room using FreeRoom class

        boolean keepAddingRooms = true;
        List<IRoom> newRooms = new ArrayList<>();

        while (keepAddingRooms) {
            String input;
            System.out.println("Enter room number");
            String roomNumber = "";
            boolean isBadRoomNumber = true;
            while (isBadRoomNumber) {
                input = scanner.nextLine();
                // TODO: Refactor into separate reversed ifs with continue
                if (isNumber(input)) {
                    if (isNewRoomNumber(newRooms, input)) {
                        isBadRoomNumber = false;
                        roomNumber = input;
                    } else {
                        System.out.println("You have already added a room with " +
                                "room number " + input);
                        if (exitHelper.exit()) { return; }
                    }
                } else {
                    System.out.println("Room number should be an integer number");
                    if (exitHelper.exit()) { return; }
                }
            }

            System.out.println("Enter room price");
            double roomPrice = 0.0;
            boolean isBadRoomPrice = true;
            while (isBadRoomPrice) {
                input = scanner.nextLine();
                if (isNumber(input)) {
                    isBadRoomPrice = false;
                    roomPrice = Double.parseDouble(input);
                } else {
                    System.out.println("Room price should be a decimal number");
                    if (exitHelper.exit()) { return; }
                }
            }

            System.out.println("Choose room type. \"S\" for single or " +
                    "\"D\" for double");
            RoomType roomType = RoomType.SINGLE;
            boolean isBadRoomType = true;
            while (isBadRoomType) {
                input = scanner.nextLine();
                // TODO: refactor using switch
                if ("d".equalsIgnoreCase(input)) {
                    isBadRoomType = false;
                    roomType = RoomType.DOUBLE;
                } else if ("s".equalsIgnoreCase(input)) {
                    isBadRoomType = false;
                }
                else {
                    System.out.println("Enter \"S\" for single or \"D\" " +
                            "for double");
                    if (exitHelper.exit()) { return; }
                }
            }

            newRooms.add(new Room(roomNumber, roomPrice, roomType));

            System.out.println("Add another room? (y/n)");
            boolean isBadInput = true;
            while (isBadInput) {
                input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y":
                        // Restart inner while loop
                        isBadInput = false;
                        break;
                    case "n":
                        // Exit both loops
                        isBadInput = false;
                        keepAddingRooms = false;
                        break;
                    default:
                        // Keep inside inner loop
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
                        if (exitHelper.exit()) { return; }
                }
            }
        }

        adminResource.addRoom(newRooms);
        System.out.println("Rooms were successfully added");
    }

    private boolean isNewRoomNumber(List<IRoom> newRooms, String roomNumber) {
        for (IRoom aRoom: newRooms) {
            if (aRoom.getRoomNumber().equals(roomNumber)) {
                return false;
            }
        }

        return true;
    }

    private void seeAllRooms() {
        Collection<IRoom> allRooms = adminResource.getAllRooms();

        if (allRooms.isEmpty()) {
            System.out.println("There are no rooms yet. Please add some");
            return;
        }

        for (IRoom aRoom: allRooms) {
            System.out.println(aRoom);
        }
    }
}
