package com.udacity.hotel.service;

import com.udacity.hotel.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private static ReservationService reservationService;

    private static String numberRoom1;
    private static IRoom room1;
    private static Calendar cal;
    private final static int YEAR = 2099;
    private final static int MONTH = Calendar.MAY;
    private final static int DAY_IN = 20;
    private final static int DAY_OUT = 27;
    private static Date checkIn;
    private static Date checkOut;
    private static Customer customer;

    @BeforeAll
    static void initAll() {
        reservationService = ReservationService.getInstance();
        numberRoom1 = "1";
        room1 = new Room(numberRoom1, 10.0D, RoomType.SINGLE);
        cal = Calendar.getInstance();
        cal.set(YEAR, MONTH, DAY_IN);
        checkIn = cal.getTime();
        cal.set(YEAR, MONTH, DAY_OUT);
        checkOut = cal.getTime();
        customer = new Customer("I", "Z", "i@z.com");
    }

    @BeforeEach
    void reset() throws NoSuchFieldException, IllegalAccessException {
        Field reservations = ReservationService.class.getDeclaredField("reservations");
        reservations.setAccessible(true);
        reservations.set(reservationService, new HashSet<>());
        Field rooms = ReservationService.class.getDeclaredField("rooms");
        rooms.setAccessible(true);
        rooms.set(reservationService, new HashMap<>());
    }

    @Test
    void getInstance() {
        ReservationService reservationServiceOther = ReservationService.getInstance();
        assertSame(reservationServiceOther, reservationService);
    }

    @Test
    void addRoom_ok() {
        reservationService.addRoom(room1);
        String numberRoom2 = "2";
        var room2 = new Room(numberRoom2, 15.0D, RoomType.DOUBLE);
        reservationService.addRoom(room2);

        Map<String, IRoom> allRooms = reservationService.getRooms();
        assertEquals(2, allRooms.size());
        assertTrue(allRooms.containsKey(numberRoom1));
        assertTrue(allRooms.containsValue(room1));
        assertTrue(allRooms.containsKey(numberRoom2));
        assertTrue(allRooms.containsValue(room2));
    }

    @Test
    void addRoom_alreadyExists() {
        reservationService.addRoom(room1);
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.addRoom(room1)
        );
        assertEquals("Room number " + room1.getRoomNumber() + " already exists",
                exception.getMessage());
    }

    @Test
    void getARoom_ok() {
        reservationService.addRoom(room1);
        IRoom roomSame = reservationService.getARoom(numberRoom1);
        assertEquals(room1, roomSame);
    }

    @Test
    void getARoom_absent() {
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.getARoom(numberRoom1)
        );
        assertEquals("There is no room with number " + numberRoom1, exception.getMessage());
    }

    @Test
    void reserveARoom() {
        Reservation reservation = reservationService.reserveARoom(customer, room1, checkIn, checkOut);
        assertEquals(customer, reservation.getCustomer());
        assertEquals(room1, reservation.getRoom());
        assertEquals(checkIn, reservation.getCheckInDate());
        assertEquals(checkOut, reservation.getCheckOutDate());

        // Check reservation was added to all reservations
        Set<Reservation> allReservations = reservationService.getReservations();
        assertEquals(1, allReservations.size());
        assertTrue(allReservations.contains(reservation));

        // Check throwing exception on attempt to reserve same room for same dates
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> reservationService.reserveARoom(customer, room1, checkIn, checkOut)
        );
        assertEquals("This room is already reserved for these days", exception.getMessage());
    }

    @Test
    void findRooms_empty() {
        assertTrue(reservationService.findRooms(checkIn, checkOut).isEmpty());
    }

    @Test
    void findRooms_noReservations_roomAvailable() {
        reservationService.addRoom(room1);
        Collection<IRoom> availableRooms = reservationService.findRooms(checkIn, checkOut);
        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
    }

    @ParameterizedTest(name = "[{index}] 20-27 booked, {0}-{1} available")
    @MethodSource("provide_availableDates")
    void findRooms_20_27Booked_desiredAvailable(int checkInDay, int checkOutDay) {
        reserve20_27();
        cal.set(YEAR, MONTH, checkInDay);
        Date checkInDesired = cal.getTime();
        cal.set(YEAR, MONTH, checkOutDay);
        Date checkOutDesired = cal.getTime();
        Collection<IRoom> availableRooms = reservationService.findRooms(checkInDesired, checkOutDesired);
        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
    }

    private static Stream<Arguments> provide_availableDates() {
        return Stream.of(
                Arguments.of(18, 19),
                Arguments.of(19, 20),
                Arguments.of(27, 28),
                Arguments.of(28, 29)
        );
    }

    private void reserve20_27() {
        reservationService.addRoom(room1);
        reservationService.reserveARoom(customer, room1, checkIn, checkOut);
    }

    @ParameterizedTest(name = "[{index}] 20-27 booked, {0}-{1} not available")
    @MethodSource("provide_notAvailableDates")
    void findRooms_20_27Booked_desiredNotAvailable(int checkInDay, int checkOutDay) {
        reserve20_27();
        cal.set(YEAR, MONTH, checkInDay);
        Date checkInDesired = cal.getTime();
        cal.set(YEAR, MONTH, checkOutDay);
        Date checkOutDesired = cal.getTime();
        Collection<IRoom> availableRooms = reservationService.findRooms(checkInDesired, checkOutDesired);
        assertEquals(0, availableRooms.size());
    }

    private static Stream<Arguments> provide_notAvailableDates() {
        return Stream.of(
                Arguments.of(19, 21),
                Arguments.of(20, 21),
                Arguments.of(22, 23),
                Arguments.of(26, 27),
                Arguments.of(26, 28)
        );
    }

    @Test
    void getCustomersReservation() {
        // Check that initially empty
        assertTrue(reservationService.getCustomersReservation(customer).isEmpty());

        // Make a reservation
        reservationService.addRoom(room1);
        Reservation reservationOne = reservationService.reserveARoom(customer, room1, checkIn, checkOut);
        Collection<Reservation> customersReservations = reservationService.getCustomersReservation(customer);
        assertEquals(1, customersReservations.size());
        assertTrue(customersReservations.contains(reservationOne));

        // Add a reservation for a different customer
        var customerOther = new Customer("J", "R", "j@r.com");
        cal.set(YEAR, MONTH, 5);
        Date checkInTwo = cal.getTime();
        cal.set(YEAR, MONTH, 6);
        Date checkOutTwo = cal.getTime();
        Reservation reservationTwo = reservationService.reserveARoom(customerOther, room1, checkInTwo, checkOutTwo);
        Set<Reservation> allReservations = reservationService.getReservations();
        assertEquals(2, allReservations.size());
        assertTrue(allReservations.contains(reservationTwo));
        customersReservations = reservationService.getCustomersReservation(customer);
        assertEquals(1, customersReservations.size());
        assertTrue(customersReservations.contains(reservationOne));

        // Add another reservation for initial customer
        cal.set(YEAR, MONTH, 15);
        Date checkInTree = cal.getTime();
        cal.set(YEAR, MONTH, 16);
        Date checkOutTree = cal.getTime();
        Reservation reservationThree = reservationService.reserveARoom(customer, room1, checkInTree, checkOutTree);
        customersReservations = reservationService.getCustomersReservation(customer);
        assertEquals(2, customersReservations.size());
        assertTrue(customersReservations.contains(reservationThree));
    }

    @Test
    void printAllReservations() {
        var outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Initially empty
        reservationService.printAllReservations();
        assertEquals("There are still no reservations\r\n", outContent.toString());

        // Make a reservation
        reservationService.reserveARoom(customer, room1, checkIn, checkOut);
        reservationService.printAllReservations();
        assertTrue(outContent.toString().contains(customer.toString()));
        assertTrue(outContent.toString().contains(room1.toString()));
        assertTrue(outContent.toString().contains(checkIn.toString()));
        assertTrue(outContent.toString().contains(checkOut.toString()));

        // Restore the standard out
        System.setOut(System.out);
    }
}
