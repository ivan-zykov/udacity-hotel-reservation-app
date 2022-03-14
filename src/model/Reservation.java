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

    @Override
    public String toString() {
        return "Reservation for \r\n" +
                customer + "\r\n" +
                room + "\r\n" +
                "Dates: " + checkInDate + " - " + checkOutDate + ".";
    }
}
