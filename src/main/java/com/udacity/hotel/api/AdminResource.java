package com.udacity.hotel.api;

import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Reservation;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * API which serves as intermediary between the admin UI and the services.
 */
public final class AdminResource {

    private final CustomerService customerService;
    private final ReservationService reservationService;

    /**
     * Constructor of this class.
     *
     * @param customerService       customerService object that handles {@link Customer}s
     * @param reservationService    reservationService object that handles {@link IRoom}s and
     *                              {@link Reservation}s
     */
    public AdminResource(CustomerService customerService,
                          ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * Calls a service to get a customer with the supplied email.
     *
     * @param email string with email of the required customer
     * @return      customer object which has the supplied email
     */
    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    /**
     * Calls a service to record a collection of new rooms.
     *
     * @param rooms list of new rooms to record
     */
    public void addRoom(List<IRoom> rooms) {

        for (IRoom newRoom: rooms) {
            reservationService.addRoom(newRoom);
        }
    }

    /**
     * Calls a service to get all recorded rooms.
     *
     * @return collection of all recorded rooms
     */
    public Collection<IRoom> getAllRooms() {

        Map<String, IRoom> allRooms = reservationService.getRooms();
        return new ArrayList<>(allRooms.values());
    }

    /**
     * Calls a service to get all customers.
     *
     * @return  collection of all recorded customers
     */
    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Calls a service to print all recorded reservations to the console.
     */
    public void displayAllReservations() {
        reservationService.printAllReservations();
    }
}
