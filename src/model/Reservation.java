package model;

import java.util.Date;

public class Reservation {

    private Customer customer;
    private IRoom room;
    private Date checkInDate, checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate,
                       Date checkOutDate) {
        this.customer = customer;
        this.room = room;
//        TODO: validate dates?
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation for \r\n" +
                customer + "\r\n" +
                room + "\r\n" +
                "Dates: " + checkInDate + " - " + checkOutDate + ".";
    }

    /**
     * Inspired by an article on www.baeldung.com
     */
    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation)o;
        // Compare rooms
        boolean roomsEquals = (room == null && other.getRoom() == null) ||
                (room != null && room.equals(other.getRoom()));
        // Compare check-in
        boolean checkInEquals = (checkInDate == null && other.getCheckInDate() == null)
                || (checkInDate != null && checkInDate.equals(other.getCheckInDate()));
        // Compare check-out
        boolean checkOutEquals = (checkOutDate == null && other.getCheckOutDate() == null)
                || (checkOutDate != null && checkOutDate.equals(other.getCheckOutDate()));

        return roomsEquals && checkInEquals && checkOutEquals;
    }

    /**
     * Inspired by an article on www.baeldung.com
     */
    @Override
    public final int hashCode() {
        int result = 17;
        if (room != null) {
            result = 31 * result + room.hashCode();
        }
        if (checkInDate != null) {
            result = 31 * result + checkInDate.hashCode();
        }
        if (checkOutDate != null) {
            result = 31 * result + checkOutDate.hashCode();
        }
        return result;
    }
}
