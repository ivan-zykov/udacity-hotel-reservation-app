package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class AdminResource {

    private static AdminResource INSTANCE;

    private final CustomerService customerService;
    private final ReservationService reservationService;

    private AdminResource(CustomerService customerService,
                          ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static AdminResource getInstance(CustomerService customerService,
                                            ReservationService reservationService) {
        if (INSTANCE == null) {
            INSTANCE = new AdminResource(customerService, reservationService);
        }

        return INSTANCE;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {

        for (IRoom newRoom: rooms) {
            reservationService.addRoom(newRoom);
        }
    }

    public Collection<IRoom> getAllRooms() {

        Map<String, IRoom> allRooms = reservationService.getRooms();
        return new ArrayList<>(allRooms.values());
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations() {
        reservationService.printAllReservations();
    }
}
