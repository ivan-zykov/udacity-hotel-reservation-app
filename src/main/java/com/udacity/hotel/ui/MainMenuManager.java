package com.udacity.hotel.ui;

import java.util.Scanner;

/**
 * Menu manager of the console UI for regular users.
 *
 * @author Ivan V. Zykov
 */
public final class MainMenuManager extends MenuManager {

    private final AdminMenuManager adminMenuManager;
    private final ExitHelper exitHelper;
    private final MainMenuService mainMenuService;

    /**
     * Constructor of this class.
     *
     * @param adminMenuManager  adminMenuManager object of the menu for admins
     * @param exitHelper        exitHelper object that allows breaking loops during tests
     * @param mainMenuService   mainMenuService object that performs an action corresponding to the selected menu
     * @param scanner           scanner object that reads user's input
     */
    public MainMenuManager(AdminMenuManager adminMenuManager, ExitHelper exitHelper, MainMenuService mainMenuService,
                           Scanner scanner) {
        super(scanner);
        this.adminMenuManager = adminMenuManager;
        this.exitHelper = exitHelper;
        this.mainMenuService = mainMenuService;
    }

    /**
     * Prints menu to the console, reads user's input for the selected menu option and performs the corresponding
     * action utilising main menu service.
     */
    public void open() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                mainMenuService.printMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> mainMenuService.findAndReserveARoom();
                    case 2 -> mainMenuService.showCustomersReservations();
                    case 3 -> mainMenuService.createNewAccount();
                    case 4 -> goToAdminMenu();
                    case 5 -> {
                        // TODO: refactor to use dedicated printer here and when handling exceptions
                        System.out.println("Exiting the app");
                        keepRunning = false;
                        scanner.close();
                    }
                    default -> System.out.println("Please enter a number representing" +
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

    private void goToAdminMenu() {
        adminMenuManager.open();
    }
}
