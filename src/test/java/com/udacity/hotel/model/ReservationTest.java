package com.udacity.hotel.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTest {

    private Reservation reservation;

    private static Calendar cal;
    private final static int YEAR = 2099;
    private final static int MONTH = Calendar.MAY;
    private final static int IN_DATE = 20;
    private final static int OUT_DATE = 25;
    private static Date checkIn;
    private static Date checkOut;

    @Mock
    private static Customer customer;
    @Mock
    private static IRoom room;

    @BeforeAll
    static void initAll() {
        cal = Calendar.getInstance();
        cal.set(YEAR, MONTH, IN_DATE);
        checkIn = cal.getTime();
        cal.set(YEAR, MONTH, OUT_DATE);
        checkOut = cal.getTime();
    }

    @BeforeEach
    void init() {
        reservation = new Reservation(customer, room, checkIn, checkOut);
    }

    @Test
    void toStringTest() {
        // Stub customer
        String expCustomer = "customer's toString";
        when(customer.toString()).thenReturn(expCustomer);

        // Stub room
        String expRoom = "room's toString";
        when(reservation.toString()).thenReturn(expRoom);

        assertTrue(reservation.toString().contains("Reservation for"));
        assertTrue(reservation.toString().contains(expCustomer));
        assertTrue(reservation.toString().contains(expRoom));
        assertTrue(reservation.toString().contains("Dates: " + checkIn + " - " + checkOut + "."));
    }

    @Test
    void equals_otherObj() {
        assertNotEquals("a", reservation);
    }

    /**
     * Not using parameters here because in provider (method source) room from comes as null.
     */
    @Test
    void equals_allSame() {
        var reservationSame = new Reservation(customer, room, checkIn, checkOut);
        assertEquals(reservation, reservationSame);
    }

    @Test
    void equals_roomOther() {
        var roomOther = new Room("1", 10.0D, RoomType.SINGLE);
        var reservationOther = new Reservation(customer, roomOther, checkIn, checkOut);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_checkInOther() {
        cal.set(YEAR, MONTH, IN_DATE + 1);
        Date checkInOther = cal.getTime();
        var reservationOther = new Reservation(customer, room, checkInOther, checkOut);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_checkOutOther() {
        cal.set(YEAR, MONTH, OUT_DATE + 1);
        Date checkOutOther = cal.getTime();
        var reservationOther = new Reservation(customer, room, checkIn, checkOutOther);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_allNull() {
        var allNull1 = new Reservation(customer, null, null, null);
        var allNull2 = new Reservation(customer, null, null, null);
        assertEquals(allNull1, allNull2);
    }
}
