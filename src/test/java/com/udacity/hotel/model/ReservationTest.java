package com.udacity.hotel.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
    private ReservationFactory reservationFactory;
    private static Customer customer;
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
        reservationFactory = new ReservationFactory();
        customer = new Customer("I", "Z", "i@z.com");
        room = new Room("1", 100.0D, RoomType.SINGLE);
        reservation = reservationFactory.create(customer, room, checkIn, checkOut);
    }

    @Test
    void toStringTest() {
        assertAll(
                () -> assertTrue(reservation.toString().contains(customer.toString())),
                () -> assertTrue(reservation.toString().contains(room.toString())),
                () -> assertTrue(reservation.toString().contains(checkIn.toString())),
                () -> assertTrue(reservation.toString().contains(checkOut.toString()))
        );
    }

    /**
     * Not using parameters here because in provider (method source) room from comes as null.
     */
    @Test
    void equals_allSame() {
        Reservation reservationSame = reservationFactory.create(customer, room, checkIn, checkOut);
        assertEquals(reservation, reservationSame);
    }

    @Test
    void equals_roomOther() {
        var roomOther = new Room("2", 10.0D, RoomType.SINGLE);
        Reservation reservationOther = reservationFactory.create(customer, roomOther, checkIn, checkOut);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_checkInOther() {
        cal.set(YEAR, MONTH, IN_DATE + 1);
        Date checkInOther = cal.getTime();
        Reservation reservationOther = reservationFactory.create(customer, room, checkInOther, checkOut);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_checkOutOther() {
        cal.set(YEAR, MONTH, OUT_DATE + 1);
        Date checkOutOther = cal.getTime();
        Reservation reservationOther = reservationFactory.create(customer, room, checkIn, checkOutOther);
        assertNotEquals(reservation, reservationOther);
    }

    @Test
    void equals_allNull() {
        Reservation allNull1 = reservationFactory.create(customer, null, null, null);
        Reservation allNull2 = reservationFactory.create(customer, null, null, null);
        assertEquals(allNull1, allNull2);
    }
}
