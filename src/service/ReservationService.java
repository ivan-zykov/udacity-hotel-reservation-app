package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public final class ReservationService {

    private static ReservationService INSTANCE;

    private final List<Reservation> reservations;
    private final Map<String, IRoom> rooms;

    private ReservationService() {
        reservations = new ArrayList<>();
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
                                    Date checkoutDate) {
        // Get rooms available for booking for the time interval
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkoutDate);

        // Check chosen room is available for booking
        for (IRoom anAvailableRoom: availableRooms) {
            if (anAvailableRoom.getRoomNumber().equals(room.getRoomNumber())) {
                // Reserve the room
                Reservation newReservation = new Reservation(customer, room,
                        checkInDate, checkoutDate);
                reservations.add(newReservation);

                return newReservation;
            }
        }

        throw new IllegalArgumentException("Chosen room is not available for the " +
                "date interval");
    }

    /**
     * Find rooms which are available for the date interval
     */
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        // Copy all rooms
        Map<String, IRoom> availableRooms = new HashMap<>(this.rooms);

        // Remove booked rooms
        filterAvailableRooms(availableRooms, checkInDate, checkOutDate);

        // Search for recommended rooms (available for next 7 days)
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms found for selected dates. Trying to find" +
                    " a room in the next 7 days");

            availableRooms = new HashMap<>(this.rooms);

            // Shift dates by 7 days
            Calendar cal = Calendar.getInstance();
            cal.setTime(checkInDate);
            cal.add(Calendar.DATE, 7);
            checkInDate = cal.getTime();
            cal.setTime(checkOutDate);
            cal.add(Calendar.DATE, 7);
            checkOutDate = cal.getTime();

            filterAvailableRooms(availableRooms, checkInDate, checkOutDate);
        }

        if (availableRooms.isEmpty()) {
            System.out.println("No free rooms in the next 7 days found. Try " +
                    "different dates");
            return null;
        }

        return new ArrayList<>(availableRooms.values());
    }

    /**
     * Filters out rooms which are booked for selected dates
     */
    private void filterAvailableRooms(Map<String, IRoom> availableRooms,
                                      Date checkInDate, Date checkOutDate) {

        for (Reservation aReservation: this.reservations) {
            boolean isCheckInOK = false;
            if (checkInDate.before(aReservation.getCheckInDate()) ||
                    checkInDate.compareTo(aReservation.getCheckOutDate()) >= 0) {
                isCheckInOK = true;
            }
            boolean isCheckOutOK = false;
            if (checkOutDate.compareTo(aReservation.getCheckInDate()) <= 0 ||
                    checkOutDate.after(aReservation.getCheckOutDate())) {
                isCheckOutOK = true;
            }
            if (! isCheckInOK || ! isCheckOutOK) {
                // Remove the room from the list of available rooms
                availableRooms.remove(aReservation.getRoom().getRoomNumber());
            }
        }
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
