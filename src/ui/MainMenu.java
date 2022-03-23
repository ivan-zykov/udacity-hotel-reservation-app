package ui;

import api.HotelResource;

import java.util.Scanner;
import java.util.regex.Pattern;

public final class MainMenu {

    private final AdminMenu adminMenu;
    private final HotelResource hotelResource;
    private final Scanner scanner;

    public MainMenu(AdminMenu adminMenu, HotelResource hotelResource,
                    Scanner scanner) {
        this.adminMenu = adminMenu;
        this.hotelResource = hotelResource;
        this.scanner = scanner;
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
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------");
        System.out.println();
        System.out.println("Please enter a number to select a menu option");
    }

    private void findAndReserveARoom() {
        System.out.println("Finding and reserving a room");
    }

    private void seeCustomersReservations() {
        System.out.println("Showing customer's reservations");
    }

    private void createNewAccount() {

        boolean keepAddingNewAccount = true;
        while (keepAddingNewAccount) {
            String input;

            System.out.println("Enter your email");
            String email = "";
            boolean keepReadingEmail = true;
            while (keepReadingEmail) {
                input = scanner.nextLine();
                // Validate email
                if (! isValidEmail(input)) {
                    System.out.println("It is not a valid email. Please enter like " +
                            "example@mail.com");
                    continue;
                }

                // Check that customer with this email already exists
                if (customerAlreadyExists(input)) {
                    System.out.println("Customer with this email already " +
                            "registered.");
                    continue;
                }

                email = input;
                keepReadingEmail = false;
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

    private boolean isValidEmail(String input) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        if (emailPattern.matcher(input).matches()) {
            return true;
        }

        return false;
    }

    private boolean customerAlreadyExists(String email) {
        if (hotelResource.getCustomer(email) == null) {
            return false;
        }

        return true;
    }

    private String readName(boolean isFirstName) {
        String name = "";
        String input = "";
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
