package com.udacity.hotel.api;

import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Reservation;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class HotelResourceTest {

    public static void main(String[] args) {

        System.out.println("--- Start HotelResourceTest test ---");
        System.out.println();

        // Instantiate SUT
        CustomerService customerService = CustomerService.getInstance();
        ReservationService reservationService = ReservationService.getInstance();
        HotelResource hotelResource = HotelResource.getInstance(customerService,
                reservationService);

        System.out.println("--- Testing createACustomer() and getCustomer() ---");
        System.out.println();

        String vanyasEmail = "vanya@bk.ru";
        hotelResource.createACustomer(vanyasEmail, "Vanya",
                "Zet");
        System.out.println(hotelResource.getCustomer(vanyasEmail));
        System.out.println();

        System.out.println("--- Testing getRoom() ---");
        System.out.println();

        // Add room first
        String roomNumber = "101";
        IRoom newRoom = new Room(roomNumber, 110.0, RoomType.SINGLE);
        reservationService.addRoom(newRoom);

        System.out.println(hotelResource.getRoom(roomNumber));
        System.out.println();

        System.out.println("--- Testing bookARoom() ---");
        System.out.println();

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.MAY, 10);
        Date checkInDate = cal.getTime();
        cal.set(2022, Calendar.MAY, 17);
        Date checkOutDate = cal.getTime();
        System.out.println(hotelResource.bookARoom(vanyasEmail, newRoom,
                checkInDate, checkOutDate));
        System.out.println();

        System.out.println("--- Testing duplicate booking ---");
        System.out.println();
        try {
            hotelResource.bookARoom(vanyasEmail, newRoom, checkInDate, checkOutDate);
            throw new Error("Failed to prevent duplicate booking");
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        System.out.println();

        System.out.println("--- Testing getCustomersReservations() ---");
        Collection<Reservation> allReservations =
                hotelResource.getCustomersReservations(vanyasEmail);
        for (Reservation aRes: allReservations) {
            System.out.println(aRes);
        }
        System.out.println();

        System.out.println("--- Testing findARoom() ---");
        System.out.println();
        System.out.println("Expect no free rooms");
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate,
                checkOutDate);
        if (! availableRooms.isEmpty()) {
            throw new Error("Failed to detect no free rooms");
        }
        System.out.println("No free rooms for " + checkInDate + " - " + checkOutDate);
        System.out.println();

        System.out.println("Expect a free room");
        checkInDate = shiftDate(checkInDate);
        checkOutDate = shiftDate(checkOutDate);
        availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        if (availableRooms.isEmpty()) {
            throw new Error("Failed to detect a free room");
        }
        for (IRoom aRoom: availableRooms) {
            System.out.println(aRoom);
        }
        System.out.println();

        System.out.println("--- End HotelResourceTest test ---");
    }

    private static Date shiftDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }
}
