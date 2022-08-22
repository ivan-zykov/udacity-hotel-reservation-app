package com.udacity.hotel.ui;

import java.util.*;

/**
 * Menu manager of the console UI for administrators.
 *
 * @author Ivan V. Zykov
 */
public final class AdminMenuManager implements MenuManager {

    private final ExitHelper exitHelper;
    private final AdminMenuService adminMenuService;
    private final Scanner scanner;

    /**
     * Constructor of this class.
     *
     * @param scanner           scanner object that reads user's input
     * @param exitHelper        exitHelper object that allows breaking loops during tests
     * @param adminMenuService  adminMenuService object that performs an action corresponding to the selected menu
     */
    public AdminMenuManager(Scanner scanner, ExitHelper exitHelper, AdminMenuService adminMenuService) {
        this.exitHelper = exitHelper;
        this.adminMenuService = adminMenuService;
        this.scanner = scanner;
    }

    /**
     * Prints menu to the console, reads administrator's input for the selected menu option and performs the
     * corresponding action utilising admin menu service.
     */
    public void open() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                adminMenuService.printMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1 -> adminMenuService.showAllCustomers();
                    case 2 -> adminMenuService.showAllRooms();
                    case 3 -> adminMenuService.showAllReservations();
                    case 4 -> adminMenuService.addARoom();
                    case 5 -> {
                        adminMenuService.notifyReturningToMainMenu();
                        keepRunning = false;
                    }
                    default -> adminMenuService.notifyNonExistingMenuNumber();
                }
            } catch (NumberFormatException ex) {
                // TODO: refactor to use dedicated printer here and when handling other exceptions
                adminMenuService.menuNotANumber();
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            } catch (Exception ex) {
                System.out.println("Unknown error occurred.");
                System.out.println(ex.getLocalizedMessage());
            }
            if (exitHelper.exit()) { return; }
        }
    }
}
