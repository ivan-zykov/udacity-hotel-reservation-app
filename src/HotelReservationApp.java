import api.AdminResource;
import api.HotelResource;
import service.CustomerService;
import service.ReservationService;
import ui.AdminMenu;
import ui.MainMenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public final class HotelReservationApp {

    public static void main(String[] args) {

        // Instantiate classes
        CustomerService customerService = CustomerService.getInstance();
        ReservationService reservationService = ReservationService.getInstance();
        AdminResource adminResource = AdminResource.getInstance(customerService,
                reservationService);
        Scanner scanner = new Scanner(System.in);
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        AdminMenu adminMenu = new AdminMenu(adminResource, scanner);
        HotelResource hotelResource = HotelResource.getInstance(customerService,
                reservationService);
        MainMenu mainMenu = new MainMenu(adminMenu, hotelResource, scanner,
                simpleDateFormat);

        // Run the app
        mainMenu.open();
    }
}
