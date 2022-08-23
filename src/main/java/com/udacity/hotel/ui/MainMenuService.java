package com.udacity.hotel.ui;

import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Performs an action selected in the {@link MainMenuManager}.
 * <p>Actions include:</p>
 * <ul>
 *     <li>finding free {@link IRoom}s for the supplied dates and booking a selected room for the
 *     corresponding {@link Customer}</li>
 *     <li>printing all {@link Reservation}s of a specified customer</li>
 *     <li>creating a new account that is recording a new customer with data from user's input</li>
 *     <li>navigating to administrator's menu</li>
 *     <li>exiting and terminating the app</li>
 * </ul>
 *
 * @author Ivan V. Zykov
 */
public class MainMenuService extends MenuService {

    private final Date now;
    private final HotelResource hotelResource;
    private final DateFormat simpleDateFormat;
    private final ConsolePrinter consolePrinter;

    /**
     * Constructor of this class.
     *
     * @param now               date object of the current date
     * @param hotelResource     hotelResource object of the API to services with functionality for regular users
     * @param scanner           scanner object that reads user's input
     * @param simpleDateFormat  simpleDateFormat object that helps to parse dates from user's input
     * @param consolePrinter    consolePrinter object that prints objects to the console
     */
    public MainMenuService (Date now, HotelResource hotelResource, Scanner scanner, DateFormat simpleDateFormat,
                            ConsolePrinter consolePrinter) {
        super(scanner);
        this.now = now;
        this.hotelResource = hotelResource;
        this.simpleDateFormat = simpleDateFormat;
        this.consolePrinter = consolePrinter;
    }

    /**
     * Prints menu for regular users to the console.
     */
    @Override
    public void printMenu() {
        consolePrinter.print("");
        consolePrinter.print("Welcome to Vanya's Hotel Reservation App");
        consolePrinter.print("----------------------------------------");
        consolePrinter.print("1. Find and reserve a room");
        consolePrinter.print("2. See my reservations");
        consolePrinter.print("3. Create an account");
        consolePrinter.print("4. Admin");
        consolePrinter.print("5. Exit");
        consolePrinter.print("----------------------------------------");
        consolePrinter.print("Please enter a number to select a menu option");
    }

    /**
     * Gets all recorded reservations for the customer identified by the email from user's input. Also, checks that
     * a user is registered with the provided email.
     */
    public void showCustomersReservations() {
        // Read customer's email
        consolePrinter.print("Please enter your email");
        String email = readEmail();

        // Check that customer is registered
        if (! customerAlreadyExists(email)) {
            consolePrinter.print("You are still not registered with this email. " +
                    "Please create an account");
        }

        // Get customer's reservations
        Collection<Reservation> customerReservations =
                hotelResource.getCustomersReservations(email);

        // Display customer's reservations
        if (customerReservations.isEmpty()) {
            consolePrinter.print("You still have no reservations with us");
        } else {
            consolePrinter.print("Your reservations:");
            for (Reservation aReservation: customerReservations) {
                consolePrinter.print(aReservation);
            }
        }
    }

    private String readEmail() {
        boolean keepReadingEmail = true;
        String email = "";
        while (keepReadingEmail) {
            String input = scanner.nextLine();
            // Validate email
            if (! isValidEmail(input)) {
                consolePrinter.print("It is not a valid email. Please enter like example@mail.com");
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

    /**
     * Records a new customer with data from user's input. Also, checks that the provided email doesn't belong to
     * already registered customer.
     */
    public void createNewAccount() {
        boolean keepAddingNewAccount = true;
        while (keepAddingNewAccount) {

            consolePrinter.print("Enter your email");
            String email = readEmail();

            // Check that customer with this email already exists
            if (customerAlreadyExists(email)) {
                consolePrinter.print("Customer with this email already " +
                        "registered.");
                continue;
            }

            consolePrinter.print("Enter your first name");
            String firstName = readName(true);

            consolePrinter.print("Enter your last name");
            String lastName = readName(false);

            // Stop outer loop
            keepAddingNewAccount = false;

            // Add new account
            hotelResource.createACustomer(email, firstName, lastName);
            consolePrinter.print("Your account successfully created");
        }
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
                consolePrinter.print("Your " + nameType + " name should have at " +
                        "least one letter.");
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

    /**
     * Searches available rooms for the dates from user's input and if one found, books it for the customer. If no
     * rooms found for the provided dates, searches rooms for the next seven days. While booking also checks that
     * customer with the email from the input is already registered.
     */
    public void findAndReserveARoom() {
        boolean keepFindingAndReservingARoom = true;
        findAndReserveARoom:
        while (keepFindingAndReservingARoom) {

            // Read check-in date
            consolePrinter.print("Enter check-in date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkIn = readDate();

            // Read check-out date
            consolePrinter.print("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkOut = readDate();

            // Check that check-in is before check-out
            if (checkIn != null && checkIn.after(checkOut)) {
                consolePrinter.print("Your check-in date is later than checkout " +
                        "date. Please reenter dates");
                continue;
            }

            // Find available rooms for initial dates
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn,
                    checkOut);

            if (availableRooms.isEmpty()) {
                consolePrinter.print("No rooms found for selected dates. Trying to find" +
                        " a room in the next 7 days");

                // Shift dates
                checkIn = shiftDate(checkIn);
                checkOut = shiftDate(checkOut);

                // Find rooms available for shifted dates
                availableRooms = hotelResource.findARoom(checkIn, checkOut);

                if (availableRooms.isEmpty()) {
                    consolePrinter.print("No free rooms in the next 7 days found. Try " +
                            "different dates");
                } else {
                    // Print shifted dates and available rooms
                    consolePrinter.print("You can book following rooms from " + checkIn +
                            " till " + checkOut + ":");
                    for (IRoom aRoom: availableRooms) {
                        consolePrinter.print(aRoom);
                    }
                }

                // Redirect back to main menu
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Print available rooms for initial dates
            consolePrinter.print("Following rooms are available for booking:");
            for (IRoom aRoom: availableRooms) {
                consolePrinter.print(aRoom);
            }

            // Ask if customer wants to book a room
            consolePrinter.print("Would you like to book one of the rooms above? " +
                    "(y/n)");
            boolean keepReadingAnswer = true;
            while (keepReadingAnswer) {
                String input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y" ->
                        // Proceed with booking
                            keepReadingAnswer = false;
                    case "n" -> {
                        // Go to main menu
                        keepFindingAndReservingARoom = false;
                        continue findAndReserveARoom;
                    }
                    default -> // Keep asking
                            consolePrinter.print("Enter \"y\" for yes or \"n\" for no");
                }
            }

            // Ask if customer has an account
            consolePrinter.print("Do you have an account?");
            keepReadingAnswer = true;
            while (keepReadingAnswer) {
                String input = scanner.nextLine();
                switch (input.toLowerCase()) {
                    case "y" ->
                        // Proceed with booking
                            keepReadingAnswer = false;
                    case "n" -> {
                        // Go to main menu
                        keepFindingAndReservingARoom = false;
                        consolePrinter.print("Please create an account in main menu");
                        continue findAndReserveARoom;
                    }
                    default -> // Keep asking
                            consolePrinter.print("Enter \"y\" for yes or \"n\" for no");
                }
            }

            // Read customer's email
            consolePrinter.print("Please enter your email");
            String email = readEmail();

            // Check that customer is registered
            if (! customerAlreadyExists(email)) {
                consolePrinter.print("You are still not registered with this email. " +
                        "Please create an account");
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Read which room to book
            consolePrinter.print("Please enter which room to book");
            String roomNumberToBook = "";
            boolean keepReadingRoomNumber = true;
            while (keepReadingRoomNumber) {
                String input = scanner.nextLine();
                if (isNumber(input)) {
                    // Check that the room is available for booking
                    boolean isAvailableRoom = false;
                    for (IRoom aRoom: availableRooms) {
                        if (aRoom.getRoomNumber().equals(input)) {
                            isAvailableRoom = true;
                            break;
                        }
                    }
                    if (isAvailableRoom) {
                        keepReadingRoomNumber = false;
                        roomNumberToBook = input;
                    } else {
                        consolePrinter.print("The room you picked is actually not " +
                                "available. Please enter a room number from the the " +
                                "list above");
                    }
                } else {
                    consolePrinter.print("Room number should be an integer number");
                }
            }

            // Book a room
            IRoom roomObjectToBook = hotelResource.getRoom(roomNumberToBook);
            Reservation newReservation = hotelResource.bookARoom(email, roomObjectToBook,
                    checkIn, checkOut);

            // Print reservation
            consolePrinter.print(newReservation);

            // Redirect back to main menu
            keepFindingAndReservingARoom = false;
        }
    }

    private Date readDate() {
        boolean keepReadingDate = true;
        Date date = null;
        while (keepReadingDate) {
            String input = scanner.nextLine();
            if (isValidDate(input)) {
                try {
                    simpleDateFormat.setLenient(false);
                    date = simpleDateFormat.parse(input);
                } catch (ParseException ex) {
                    consolePrinter.print("Try entering the date again");
                    continue;
                }
                if (! date.before(now)) {
                    keepReadingDate = false;
                } else {
                    consolePrinter.print("This date is in the past. Please reenter the date");
                }
            } else {
                consolePrinter.print("Renter the date in format mm/dd/yyyy");
            }
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
            consolePrinter.print(ex.getLocalizedMessage());
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
}
