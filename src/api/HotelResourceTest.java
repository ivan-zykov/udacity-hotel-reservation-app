package api;

import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;

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
        cal.set(2022, 05, 10);
        Date checkInDate = cal.getTime();
        cal.set(2022, 05, 17);
        Date checkOutDate = cal.getTime();
        System.out.println(hotelResource.bookARoom(vanyasEmail, newRoom,
                checkInDate, checkOutDate));
        System.out.println();

        System.out.println("--- Testing getCustomersReservations() ---");
        System.out.println();

        Collection<Reservation> vanyasReservations =
                hotelResource.getCustomersReservations(vanyasEmail);
        for (Reservation aReservation: vanyasReservations) {
            System.out.println(aReservation);
        }
        System.out.println();

        System.out.println("--- Testing findARoom() ---");
        System.out.println();

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate,
                checkOutDate);
        for (IRoom freeRoom: availableRooms) {
            System.out.println(freeRoom);
        }
        System.out.println();

        System.out.println("--- End HotelResourceTest test ---");
    }

}
