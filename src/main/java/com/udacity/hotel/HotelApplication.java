package com.udacity.hotel;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.ReservationFactory;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;
import com.udacity.hotel.ui.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Main class of this application which instantiates all required classes and runs the app.
 *
 * @author Ivan V. Zykov
 * @see <a href="https://www.udacity.com/">First project of Java Programing nanodegree by Udacity</a>
 */
public final class HotelApplication {

    /**
     * Main method starting this app.
     *
     * @param args  string with arguments, but this app doesn't support any
     */
    public static void main(String[] args) {
        // Instantiate classes
        CustomerService customerService = CustomerService.getInstance();
        ReservationService reservationService = ReservationService.getInstance(new ReservationFactory());
        AdminResource adminResource = new AdminResource(customerService, reservationService);
        Scanner scanner = new Scanner(System.in);
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        ExitHelper exitHelper = new ExitHelper();
        AdminMenuService adminMenuService = new AdminMenuService(adminResource, scanner, exitHelper);
        ConsolePrinter consolePrinter = new ConsolePrinterImpl();
        MenuManager adminMenuManager = new AdminMenuManager(scanner, exitHelper, adminMenuService, consolePrinter);
        HotelResource hotelResource = new HotelResource(customerService,
                reservationService);
        Date now = new Date();
        MainMenuService mainMenuService = new MainMenuService(now, hotelResource, scanner, exitHelper,
                simpleDateFormat);
        MenuManager mainMenuManager = new MainMenuManager(adminMenuManager, exitHelper, mainMenuService, scanner,
                consolePrinter);

        // Run the app
        mainMenuManager.open();
    }
}
