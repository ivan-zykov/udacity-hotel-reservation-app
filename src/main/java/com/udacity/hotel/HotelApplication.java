package com.udacity.hotel;

import com.udacity.hotel.api.AdminResource;
import com.udacity.hotel.api.HotelResource;
import com.udacity.hotel.model.ReservationFactory;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;
import com.udacity.hotel.ui.AdminMenu;
import com.udacity.hotel.ui.ExitHelper;
import com.udacity.hotel.ui.MainMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 * Main class of this application which instantiates all required classes and prints the main menu in the console.
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
        AdminMenu adminMenu = new AdminMenu(adminResource, scanner, new ExitHelper());
        HotelResource hotelResource = new HotelResource(customerService,
                reservationService);
        MainMenu mainMenu = new MainMenu(adminMenu, hotelResource, scanner,
                simpleDateFormat, new ExitHelper(), new Date());

        // Run the app
        mainMenu.open();
    }
}
