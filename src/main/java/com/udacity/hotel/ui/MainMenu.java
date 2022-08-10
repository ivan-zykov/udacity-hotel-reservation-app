package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Console UI for a regular users.
 *
 * @author Ivan V. Zykov
 */
public final class MainMenu extends Menu {

    private final AdminMenu adminMenu;
    private final HotelResource hotelResource;
    private final DateFormat simpleDateFormat;
    private final ExitHelper exitHelper;
    private final Date now;

    /**
     * Constructor of this class.
     *
     * @param adminMenu         adminMenu object of the menu for admins
     * @param hotelResource     hotelResource object of the API to services with functionality for regular users
     * @param scanner           scanner object that reads user's input
     * @param simpleDateFormat  dateFormat object that parses and validates dates from scanner
     */
    public MainMenu(AdminMenu adminMenu, HotelResource hotelResource,
                    Scanner scanner, DateFormat simpleDateFormat, ExitHelper exitHelper,
                    Date now) {
        super(scanner);
        this.adminMenu = adminMenu;
        this.hotelResource = hotelResource;
        this.simpleDateFormat = simpleDateFormat;
        this.exitHelper = exitHelper;
        this.now = now;
    }

    /**
     * Prints menu to the console, reads user's input for the selected menu option and performs the corresponding
     * action utilising APIs to services.
     * <p>Performed actions include:</p>
     * <ul>
     *     <li>finding free {@link IRoom}s for the supplied dates and booking a selected room for the
     *     corresponding {@link Customer}</li>
     *     <li>printing all reservations of a specified customer</li>
     *     <li>creating a new account that is recording a new customer with data from user's input</li>
     *     <li>navigating to administrator's menu</li>
     *     <li>exiting and terminating the app</li>
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
                        findAndReserveARoom();
                        break;
                    case 2:
                        seeCustomersReservations();
                        if (exitHelper.exit() || exitHelper.exitNested()) { return; }
                        break;
                    case 3:
                        createNewAccount();
                        if (exitHelper.exit() || exitHelper.exitNested()) { return; }
                        break;
                    case 4:
                        goToAdminMenu();
                        break;
                    case 5:
                        System.out.println("Exiting the app");
                        keepRunning = false;
                        scanner.close();
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
            if (exitHelper.exit() || exitHelper.exitNested()) { return; }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Welcome to Vanya's Hotel Reservation App");
        System.out.println("----------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------");
        System.out.println("Please enter a number to select a menu option");
    }

    private void findAndReserveARoom() {

        boolean keepFindingAndReservingARoom = true;
        findAndReserveARoom:
        while (keepFindingAndReservingARoom) {

            // Read check-in date
            System.out.println("Enter check-in date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkIn = readDate();
            if (exitHelper.exitNested() && checkIn == null) { return; }

            // Read check-out date
            System.out.println("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkOut = readDate();
            if (exitHelper.exitNested() && checkOut == null) { return; }

            // Check that check-in is before check-out
            if (checkIn.after(checkOut)) {
                System.out.println("Your check-in date is later than checkout " +
                        "date. Please reenter dates");
                if (exitHelper.exit()) { return; }
                continue;
            }

            // Check that both dates are in the future
            if (checkIn.before(now) || checkOut.before(now)) {
                System.out.println("At least one of the dates is in the past. " +
                        "Please reenter dates");
                if (exitHelper.exit()) { return; }
                continue;
            }

            // Find available rooms for initial dates
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn,
                    checkOut);

            if (availableRooms.isEmpty()) {
                System.out.println("No rooms found for selected dates. Trying to find" +
                        " a room in the next 7 days");

                // Shift dates
                checkIn = shiftDate(checkIn);
                checkOut = shiftDate(checkOut);

                // Find rooms available for shifted dates
                availableRooms = hotelResource.findARoom(checkIn, checkOut);

                if (availableRooms.isEmpty()) {
                    System.out.println("No free rooms in the next 7 days found. Try " +
                            "different dates");
                } else {
                    // Print shifted dates and available rooms
                    System.out.println("You can book following rooms from " + checkIn +
                            " till " + checkOut + ":");
                    for (IRoom aRoom: availableRooms) {
                        System.out.println(aRoom);
                    }
                }

                // Redirect back to main menu
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Print available rooms for initial dates
            System.out.println("Following rooms are available for booking:");
            for (IRoom aRoom: availableRooms) {
                System.out.println(aRoom);
            }

            // Ask if customer wants to book a room
            System.out.println("Would you like to book one of the rooms above? " +
                    "(y/n)");
            boolean keepReadingAnswer = true;
            while (keepReadingAnswer) {
                String input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y":
                        // Proceed with booking
                        keepReadingAnswer = false;
                        break;
                    case "n":
                        // Go to main menu
                        keepFindingAndReservingARoom = false;
                        continue findAndReserveARoom;
                    default:
                        // Keep asking
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
                        if (exitHelper.exit()) { return; }
                }
            }

            // Ask if customer has an account
            System.out.println("Do you have an account?");
            keepReadingAnswer = true;
            while (keepReadingAnswer) {
                String input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y":
                        // Proceed with booking
                        keepReadingAnswer = false;
                        break;
                    case "n":
                        // Go to main menu
                        keepFindingAndReservingARoom = false;
                        System.out.println("Please create an account in main menu");
                        continue findAndReserveARoom;
                    default:
                        // Keep asking
                        System.out.println("Enter \"y\" for yes or \"n\" for no");
                        if (exitHelper.exit()) { return; }
                }
            }

            // Read customer's email
            System.out.println("Please enter your email");
            String email = readEmail();

            // Check that customer is registered
            if (! customerAlreadyExists(email)) {
                System.out.println("You are still not registered with this email. " +
                        "Please create an account");
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Read which room to book
            System.out.println("Please enter which room to book");
            String roomNumberToBook = "";
            boolean keepReadingRoomNumber = true;
            while (keepReadingRoomNumber) {
                String input = scanner.nextLine();
                if (! isNumber(input)) {
                    System.out.println("Room number should be an integer number");
                    if (exitHelper.exit()) { return; }
                    continue;
                }
                // Check that the room is available for booking
                boolean isAvailableRoom = false;
                for (IRoom aRoom: availableRooms) {
                    if (aRoom.getRoomNumber().equals(input)) {
                        isAvailableRoom = true;
                        break;
                    }
                }
                if (! isAvailableRoom) {
                    System.out.println("The room you picked is actually not " +
                            "available. Please enter a room number from the the " +
                            "list above");
                    if (exitHelper.exit()) { return; }
                    continue;
                }

                keepReadingRoomNumber = false;
                roomNumberToBook = input;
            }

            // Book a room
            IRoom roomObjectToBook = hotelResource.getRoom(roomNumberToBook);
            Reservation newReservation = hotelResource.bookARoom(email, roomObjectToBook,
                    checkIn, checkOut);

            // Print reservation
            System.out.println(newReservation);

            // Redirect back to main menu
            keepFindingAndReservingARoom = false;
        }
    }

    private Date readDate() {
        boolean keepReadingDate = true;
        Date date = null;
        while (keepReadingDate) {
            String input = scanner.nextLine();
            if (! isValidDate(input)) {
                System.out.println("Renter the date in format mm/dd/yyyy");
                if (exitHelper.exitNested()) { return null; }
                continue;
            }
            try {
                simpleDateFormat.setLenient(false);
                date = simpleDateFormat.parse(input);
            } catch (ParseException ex) {
                System.out.println("Try entering the date again");
                if (exitHelper.exitNested()) { return null; }
                continue;
            }
            // TODO: Move checking that the date is in the future to here
            keepReadingDate = false;
        }
        return date;
    }

    /**
     * Implementation inspired by example on www.baeldung.com
     */
    private boolean isValidDate(String input) {
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(input);
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private Date shiftDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private void seeCustomersReservations() {

        // Read customer's email
        System.out.println("Please enter your email");
        String email = readEmail();
        if (exitHelper.exitNested()) { return; }

        // Check that customer is registered
        if (! customerAlreadyExists(email)) {
            System.out.println("You are still no registered with this email. " +
                    "Please create an account");
            if (exitHelper.exit()) { return; }
        }

        // Get customer's reservations
        Collection<Reservation> customerReservations =
                hotelResource.getCustomersReservations(email);

        // Display customer's reservations
        if (customerReservations.isEmpty()) {
            System.out.println("You still have no reservations with us");
        } else {
            System.out.println("Your reservations:");
            for (Reservation aReservation: customerReservations) {
                System.out.println(aReservation);
            }
        }
    }

    private void createNewAccount() {

        boolean keepAddingNewAccount = true;
        while (keepAddingNewAccount) {

            System.out.println("Enter your email");
            String email = readEmail();
            if (exitHelper.exitNested() && email == null) { return; }

            // Check that customer with this email already exists
            if (customerAlreadyExists(email)) {
                System.out.println("Customer with this email already " +
                        "registered.");
                if (exitHelper.exit()) { return; }
                continue;
            }

            System.out.println("Enter your first name");
            String firstName = readName(true);
            if (exitHelper.exitNested() && firstName == null) { return; }

            System.out.println("Enter your last name");
            String lastName = readName(false);
            if (exitHelper.exitNested() && lastName == null) { return; }

            // Stop outer loop
            keepAddingNewAccount = false;

            // Add new account
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Your account successfully created");
        }
    }

    private String readEmail() {
        boolean keepReadingEmail = true;
        String email = "";
        while (keepReadingEmail) {
            String input = scanner.nextLine();
            // Validate email
            if (! isValidEmail(input)) {
                System.out.println("It is not a valid email. Please enter like " +
                        "example@mail.com");
                if (exitHelper.exitNested()) { return null; }
                continue;
            }

            email = input;
            keepReadingEmail = false;
        }

        return email;
    }

    private boolean isValidEmail(String input) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(input).matches();
    }

    private boolean customerAlreadyExists(String email) {
        return hotelResource.getCustomer(email) != null;
    }

    private String readName(boolean isFirstName) {
        String name = "";
        String input;
        String nameType = "last";
        if (isFirstName) {
            nameType = "first";
        }
        boolean keepReadingName = true;
        while (keepReadingName) {
            input = scanner.nextLine();
            if (! hasCharacters(input)) {
                System.out.println("Your " + nameType + " name should have at " +
                        "least one letter.");
                if (exitHelper.exitNested()) { return null; }
                continue;
            }

            name = input;
            keepReadingName = false;
        }

        return name;
    }

    private boolean hasCharacters(String input) {
        return input.matches(".*[a-zA-Z]+.*");
    }

    private void goToAdminMenu() {
        adminMenu.open();
    }
}
