package com.udacity.hotel.service;

import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Reservation;
import com.udacity.hotel.model.ReservationFactory;

import java.util.*;

/**
 * A singleton service to keep track, record and retrieve {@link IRoom}s and {@link Reservation}s.
 *
 * @author Ivan V. Zykov
 */
public final class ReservationService {

    private static ReservationService instance;

    private final Set<Reservation> reservations;
    private final Map<String, IRoom> rooms;
    private final ReservationFactory reservationFactory;

    private ReservationService(ReservationFactory reservationFactory) {
        reservations = new HashSet<>();
        rooms = new HashMap<>();
        this.reservationFactory = reservationFactory;
    }

    /**
     * Provides the unique instance of this singleton service.
     *
     * @return  reservationService object of this service
     */
    public static ReservationService getInstance(ReservationFactory reservationFactory) {
        if (instance == null) {
            instance = new ReservationService(reservationFactory);
        }

        return instance;
    }

    /**
     * Returns all rooms recorded so far.
     *
     * @return  map of rooms
     */
    public Map<String, IRoom> getRooms() {
        return rooms;
    }

    /**
     * Records the supplied room if a room with the same number was not recorded yet.
     *
     * @param   room                        iRoom, an object of a room to add
     * @throws  IllegalArgumentException    if a room with the same ID already exists
     */
    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number " + room.getRoomNumber() +
                    " already exists");
        } else {
            rooms.put(room.getRoomNumber(), room);
        }
    }

    /**
     * Returns a room if one was already recorded with the supplied ID.
     *
     * @param   roomId                      string for room's ID
     * @return                              iRoom corresponding to supplied ID
     * @throws  IllegalArgumentException    if there is no room with supplied ID
     */
    public IRoom getARoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            return rooms.get(roomId);
        } else {
            throw new IllegalArgumentException("There is no room with number " +
                    roomId);
        }
    }

    /**
     * Creates a new reservation and if the same reservation was not recorded yet, records it.
     *
     * @param customer      customer for whom the reservation is made
     * @param room          iRoom which is reserved
     * @param checkInDate   date object of check-in
     * @param checkOutDate  date object of check-out
     * @return              reservation newly created
     * @throws IllegalArgumentException if the supplied room is already reserved for exactly the same supplied days
     */
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate,
                                    Date checkOutDate) {
        Reservation newReservation = reservationFactory.create(customer, room, checkInDate,
                checkOutDate);
        if (reservations.contains(newReservation)) {
            throw new IllegalArgumentException("This room is already reserved for these " +
                    "days");
        }
        reservations.add(newReservation);
        return newReservation;
    }

    /**
     * Finds rooms available for booking withing the supplied dates.
     *
     * @param checkInDate   date object of check-in
     * @param checkOutDate  date object of check-out
     * @return              collection of rooms available for the supplied dates
     */
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // Copy all rooms
        Map<String, IRoom> availableRooms = new HashMap<>(this.rooms);

        // Compare with dates of existing reservations
        for (Reservation aReservation: this.reservations) {
            DatesCheckResult checkResult = checkDates(aReservation, checkInDate,
                    checkOutDate);
            boolean isCheckInOK = checkResult.isCheckInOK();
            boolean isCheckOutOK = checkResult.isCheckOutOK();
            if (! isCheckInOK || ! isCheckOutOK) {
                // Remove the room from the list of available rooms
                availableRooms.remove(aReservation.getRoom().getRoomNumber());
            }
        }

        return new ArrayList<>(availableRooms.values());
    }

    /**
     * Checks if the supplied reservation conflicts with the supplied check-in and check-out dates.
     *
     * @param reservation   reservation to compare with the supplied dates
     * @param checkIn       date object of check-in
     * @param checkOut      date object of check-out
     * @return              datesCheckResult object of a helper class containing a check result for each date
     */
    DatesCheckResult checkDates(Reservation reservation, Date checkIn, Date checkOut) {
        boolean isCheckInOK = checkIn.before(reservation.getCheckInDate()) ||
                checkIn.compareTo(reservation.getCheckOutDate()) >= 0;
        boolean isCheckOutOK = checkOut.compareTo(reservation.getCheckInDate()) <= 0 ||
                checkOut.after(reservation.getCheckOutDate());
        return new DatesCheckResult(isCheckInOK, isCheckOutOK);
    }

    /**
     * Finds all reservations for the supplied customer.
     *
     * @param customer  customer for whom reservations are searched
     * @return          collection for reservations for the supplied customer
     */
    public Collection<Reservation> getCustomersReservation(Customer customer) {

        List<Reservation> customersReservations = new ArrayList<>();
        for (Reservation aReservation: this.reservations) {
            if (aReservation.getCustomer().equals(customer)) {
                customersReservations.add(aReservation);
            }
        }

        return customersReservations;
    }

    public Set<Reservation> getAllReservations() {
        return reservations;
    }

    /**
     * Helper for representing results of  {@link ReservationService#checkDates(Reservation, Date, Date)}
     * method.
     *
     * @param isCheckInOK   boolean indicating if the check-in date conflicts with a reservation
     * @param isCheckOutOK  boolean indicating if the check-out date conflicts with a reservation
     */
    record DatesCheckResult(boolean isCheckInOK, boolean isCheckOutOK) {}
}
