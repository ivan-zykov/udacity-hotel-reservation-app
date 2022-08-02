package com.udacity.hotel.api;

import com.udacity.hotel.model.*;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelResourceTest {

    private HotelResource hotelResource;

    @Mock
    private CustomerService customerService;
    @Mock
    private ReservationService reservationService;

    private String firstName;
    private String lastName;
    private String email;
    private String roomNumber;
    private IRoom room;
    private Date checkIn;
    private Date checkOut;

    @BeforeEach
    void init() {
        hotelResource = new HotelResource(customerService, reservationService);
        firstName = "I";
        lastName = "Z";
        email = "i@z.com";
        roomNumber = "101";
        room = new Room(roomNumber, 10.0D, RoomType.SINGLE);
        Calendar cal = Calendar.getInstance();
        final int year = 2099;
        final int month = Calendar.MAY;
        cal.set(year, month, 20);
        checkIn = cal.getTime();
        cal.set(year, month, 27);
        checkOut = cal.getTime();
    }

    @Test
    void getCustomer() {
        var customer = new Customer(firstName, lastName, email);
        when(customerService.getCustomer(email)).thenReturn(customer);
        assertEquals(customer, hotelResource.getCustomer(email));
    }

    @Test
    void createACustomer() {
        hotelResource.createACustomer(email, firstName, lastName);
        verify(customerService, times(1)).addCustomer(email, firstName,
                lastName);
    }

    @Test
    void getRoom() {
        when(reservationService.getARoom(roomNumber)).thenReturn(room);
        assertEquals(room, hotelResource.getRoom(roomNumber));
    }

    @Test
    void bookARoom() {
        hotelResource.createACustomer(email, firstName, lastName);
        var customer = new Customer(firstName, lastName, email);
        var reservation = new Reservation(customer, room, checkIn, checkOut);
        when(customerService.getCustomer(email)).thenReturn(customer);
        when(reservationService.reserveARoom(customer, room, checkIn, checkOut))
                .thenReturn(reservation);
        assertEquals(reservation, hotelResource.bookARoom(email, room, checkIn, checkOut));
    }

    @Test
    void getCustomersReservations() {
        var customer = new Customer(firstName, lastName, email);
        when(customerService.getCustomer(email)).thenReturn(customer);
        var reservation = new Reservation(customer, room, checkIn, checkOut);
        when(reservationService.getCustomersReservation(customer)).thenReturn(List.of(reservation));
        Collection<Reservation> allReservations = hotelResource.getCustomersReservations(email);
        assertEquals(1, allReservations.size());
        assertTrue(allReservations.contains(reservation));
    }

    @Test
    void findARoom() {
        when(reservationService.findRooms(checkIn, checkOut)).thenReturn(List.of(room));
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);
        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(room));
    }
}
