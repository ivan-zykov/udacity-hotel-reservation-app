import api.AdminResource;
import api.HotelResource;
import service.CustomerService;
import service.ReservationService;
import ui.AdminMenu;
import ui.MainMenu;

public final class HotelReservationApp {

    public static void main(String[] args) {

        // Instantiate classes
        CustomerService customerService = CustomerService.getInstance();
        ReservationService reservationService = ReservationService.getInstance();
        AdminResource adminResource = AdminResource.getInstance(customerService,
                reservationService);
        AdminMenu adminMenu = new AdminMenu(adminResource);
        HotelResource hotelResource = HotelResource.getInstance(customerService,
                reservationService);
        MainMenu mainMenu = new MainMenu(adminMenu, hotelResource);

        // Run the app
        mainMenu.open();
    }
}
