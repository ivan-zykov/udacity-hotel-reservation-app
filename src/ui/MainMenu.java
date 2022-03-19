package ui;

import api.HotelResource;

import java.util.Scanner;

public final class MainMenu {

    private final AdminMenu adminMenu;
    private final HotelResource hotelResource;

    public MainMenu(AdminMenu adminMenu, HotelResource hotelResource) {
        this.adminMenu = adminMenu;
        this.hotelResource = hotelResource;
    }

    public void open() {

        boolean keepRunning = true;

        Scanner scanner = new Scanner(System.in);

        while (keepRunning) {
            try {
                printMenu();

                int input = Integer.parseInt(scanner.next());

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
                        break;
                    default:
                        System.out.println("Please enter a number representing" +
                                "a menu option from above");
                }
            } catch (Exception ex) {
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
        System.out.println("Crating new account");
    }

    private void goToAdminMenu() {
        adminMenu.open();
    }
}
