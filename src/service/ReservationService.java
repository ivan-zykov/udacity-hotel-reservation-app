package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public final class ReservationService {

    private static ReservationService INSTANCE;

    private final Set<Reservation> reservations;
    private final Map<String, IRoom> rooms;

    private ReservationService() {
        reservations = new HashSet<>();
        rooms = new HashMap<>();
    }

    public static ReservationService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReservationService();
        }

        return INSTANCE;
    }

    public Map<String, IRoom> getRooms() {
        return rooms;
    }

    public void addRoom(IRoom room) {
        if (rooms.containsKey(room.getRoomNumber())) {
            throw new IllegalArgumentException("Room number " + room.getRoomNumber() +
                    " already exists");
        } else {
            rooms.put(room.getRoomNumber(), room);
        }
    }

    public IRoom getARoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            return rooms.get(roomId);
        } else {
            throw new IllegalArgumentException("There is no room with number " +
                    roomId);
        }
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate,
                                    Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate,
                checkOutDate);
        if (reservations.contains(newReservation)) {
            throw new IllegalArgumentException("This room is already reserved for these " +
                    "days");
        }
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // Copy all rooms
        Map<String, IRoom> availableRooms = new HashMap<>(this.rooms);

        // Compare with dates of existing reservations
        for (Reservation aReservation: this.reservations) {
            DatesCheckResult checkResult = checkDates(aReservation, checkInDate,
                    checkOutDate);
            boolean isCheckInOK = checkResult.getIsCheckInOK();
            boolean isCheckOutOK = checkResult.getIsCheckOutOK();
            if (! isCheckInOK || ! isCheckOutOK) {
                // Remove the room from the list of available rooms
                availableRooms.remove(aReservation.getRoom().getRoomNumber());
            }
        }

        return new ArrayList<>(availableRooms.values());
    }

    DatesCheckResult checkDates(Reservation reservation, Date checkIn, Date checkOut) {
        boolean isCheckInOK = false;
        if (checkIn.before(reservation.getCheckInDate()) ||
                checkIn.compareTo(reservation.getCheckOutDate()) >= 0) {
            isCheckInOK = true;
        }
        boolean isCheckOutOK = false;
        if (checkOut.compareTo(reservation.getCheckInDate()) <= 0 ||
                checkOut.after(reservation.getCheckOutDate())) {
            isCheckOutOK = true;
        }
        DatesCheckResult result = new DatesCheckResult(isCheckInOK, isCheckOutOK);

        return result;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {

        List<Reservation> customersReservations = new ArrayList<>();
        for (Reservation aReservation: this.reservations) {
            if (aReservation.getCustomer().compareTo(customer) == 0) {
                customersReservations.add(aReservation);
            }
        }

        return customersReservations;
    }

    public void printAllReservations() {
        if (this.reservations.isEmpty()) {
            System.out.println("There are still no reservations");
        } else {
            for (Reservation aRes: this.reservations) {
                System.out.println(aRes);
            }
        }
    }
}

/**
 * Helper class for ReservationService::checkDates()
 */
final class DatesCheckResult {

    private final boolean isCheckInOK, isCheckOutOK;

    public DatesCheckResult(boolean isCheckInOK, boolean isCheckOutOK) {
        this.isCheckInOK = isCheckInOK;
        this.isCheckOutOK = isCheckOutOK;
    }

    public boolean getIsCheckInOK() {
        return isCheckInOK;
    }

    public boolean getIsCheckOutOK() {
        return isCheckOutOK;
    }
}