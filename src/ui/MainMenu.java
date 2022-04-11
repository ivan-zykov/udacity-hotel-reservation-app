package ui;

import api.HotelResource;
import model.IRoom;
import model.Reservation;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public final class MainMenu extends Menu {

    private final AdminMenu adminMenu;
    private final HotelResource hotelResource;
    private final DateFormat simpleDateFormat;


    public MainMenu(AdminMenu adminMenu, HotelResource hotelResource,
                    Scanner scanner, DateFormat simpleDateFormat) {
        super(scanner);
        this.adminMenu = adminMenu;
        this.hotelResource = hotelResource;
        this.simpleDateFormat = simpleDateFormat;
    }

    public void open() {

        boolean keepRunning = true;

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
                        findAndReserveARoom();
                        break;
                    case 2:
                        seeCustomersReservations();
                        break;
                    case 3:
                        createNewAccount();
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

            // Read check-out date
            System.out.println("Enter check-out date in format mm/dd/yyyy " +
                    "Example: 05/30/2022");
            Date checkOut = readDate();

            // Check that check-in is before check-out
            if (checkIn.after(checkOut)) {
                System.out.println("Your check-in date is later than checkout " +
                        "date. Please reenter dates");
                continue;
            }

            // Check that both dates are in the future
            Date now = new Date();
            if (checkIn.before(now) || checkOut.before(now)) {
                System.out.println("At least one of the dates is in the past. " +
                        "Please reenter dates");
                continue;
            }

            // Find available rooms
            Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn,
                    checkOut);

            // Stop finding a room
            if (availableRooms == null) {
                keepFindingAndReservingARoom = false;
                continue;
                // The resource will print info messages to console
            }

            // Show available rooms
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
                }
            }

            // Read customer's email
            System.out.println("Please enter your email");
            String email = readEmail();

            // Check that customer is registered
            if (! customerAlreadyExists(email)) {
                System.out.println("You are still no registered with this email. " +
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
                    continue;
                }

                keepReadingRoomNumber = false;
                roomNumberToBook = input;
            }

            // Book a room
            IRoom roomObjectToBook = hotelResource.getRoom(roomNumberToBook);
            Reservation newReservation = hotelResource.bookARoom(email,
                    roomObjectToBook, checkIn, checkOut);

            // Print reservation
            System.out.println(newReservation);

            // Stop outer loop
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
                continue;
            }
            try {
                simpleDateFormat.setLenient(false);
                date = simpleDateFormat.parse(input);
            } catch (ParseException ex) {
                System.out.println("Try entering the date again");
                continue;
            }
            // TODO: Move checking that the date is in the future to here
            keepReadingDate = false;
        }
        return date;
    }

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

    private void seeCustomersReservations() {
        System.out.println("Showing customer's reservations");
    }

    private void createNewAccount() {

        boolean keepAddingNewAccount = true;
        while (keepAddingNewAccount) {

            System.out.println("Enter your email");
            String email = readEmail();

            // Check that customer with this email already exists
            if (customerAlreadyExists(email)) {
                System.out.println("Customer with this email already " +
                        "registered.");
                continue;
            }

            System.out.println("Enter your first name");
            String firstName = readName(true);

            System.out.println("Enter your last name");
            String lastName = readName(false);

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
        if (hotelResource.getCustomer(email) == null) {
            return false;
        }

        return true;
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
                continue;
            }

            name = input;
            keepReadingName = false;
        }

        return name;
    }

    private boolean hasCharacters(String input) {
        if (input.matches(".*[a-zA-Z]+.*")) {
            return true;
        }

        return false;
    }

    private void goToAdminMenu() {
        adminMenu.open();
    }
}
