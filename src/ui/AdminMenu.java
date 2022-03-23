package ui;

import api.AdminResource;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public final class AdminMenu {

    private final AdminResource adminResource;

    public AdminMenu(AdminResource adminResource) {
        this.adminResource = adminResource;
    }

    public void open() {

        boolean keepRunning = true;

        // TODO: inject scanner instead
        Scanner scanner = new Scanner(System.in);

        while (keepRunning) {
            try {
                printMenu();

                int input = Integer.parseInt(scanner.next());

                // Clean scanner
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                switch (input) {
                    case 1:
                        System.out.println("Selected 1");
                        break;
                    case 2:
                        seeAllRooms();
                        break;
                    case 3:
                        System.out.println("Selected 3");
                        break;
                    case 4:
                        addARoom();
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
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Admin menu of Vanya's Hotel Reservation App");
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to Main Menu");
        System.out.println("----------------------------------------");
        System.out.println();
        System.out.println("Select a menu option");
    }

    private void addARoom() {

        // TODO: add option to add a room using FreeRoom class

        boolean keepAddingRooms = true;
        Scanner scanner = new Scanner(System.in);
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
                    }
                } else {
                    System.out.println("Room number should be an integer number");
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
                }
            }

            System.out.println("Choose room type. \"S\" for single or " +
                    "\"D\" for double");
            RoomType roomType = RoomType.SINGLE;
            boolean isBadRoomType = true;
            while (isBadRoomType) {
                input = scanner.next();
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
                }
            }

            newRooms.add(new Room(roomNumber, roomPrice, roomType));

            System.out.println("Add another room? (y/n)");
            boolean isBadInput = true;
            while (isBadInput) {
                input = scanner.next();
                switch (input.toLowerCase()) {
                    case "y":
                        // Restart outer while loop
                        isBadInput = false;
                        // Clean scanner
                        if (scanner.hasNextLine()) {
                            scanner.nextLine();
                        }
                        break;
                    case "n":
                        // Exit both loops
                        isBadInput = false;
                        keepAddingRooms = false;
                        break;
                    default:
                        // Keep inside inner loop
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
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

    private boolean isNumber(String strInt) {
        if (strInt == null) {
            return false;
        }
        try {
            Double.parseDouble(strInt);
        } catch (NumberFormatException ex) {
            return false;
        }

        return true;
    }
}
