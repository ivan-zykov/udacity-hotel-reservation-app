package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public final class HotelResource {

    private static HotelResource INSTANCE;

    private final CustomerService customerService;
    private final ReservationService reservationService;

    private HotelResource(CustomerService customerService,
                          ReservationService reservationService) {

        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    public static HotelResource getInstance(CustomerService customerService,
                                            ReservationService reservationService) {

        if (INSTANCE == null) {
            INSTANCE = new HotelResource(customerService, reservationService);
        }

        return INSTANCE;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lasName) {
        customerService.addCustomer(email, firstName, lasName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate,
                                 Date checkOutDate) {

        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate,
                checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {

        Customer customer = getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
}
