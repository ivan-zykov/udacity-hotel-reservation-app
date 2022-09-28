package com.udacity.hotel.service;

import com.udacity.hotel.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    private static ReservationService reservationService;

    private String numberRoom1;
    private IRoom room1;
    private Calendar cal;
    private final int YEAR = 2099;
    private final int MONTH = Calendar.MAY;
    private Date checkIn;
    private Date checkOut;
    private Customer customer;

    @BeforeAll
    static void initAll() {
        var reservationFactory = new ReservationFactory();
        reservationService = ReservationService.getInstance(reservationFactory);
    }

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        // Reset the SUT which is a singleton
        Field reservations = ReservationService.class.getDeclaredField("reservations");
        reservations.setAccessible(true);
        reservations.set(reservationService, new HashSet<>());
        Field rooms = ReservationService.class.getDeclaredField("rooms");
        rooms.setAccessible(true);
        rooms.set(reservationService, new HashMap<>());

        numberRoom1 = "1";
        room1 = new Room(numberRoom1, 10.0D, RoomType.SINGLE);
        cal = Calendar.getInstance();
        final int DAY_IN = 20;
        cal.set(YEAR, MONTH, DAY_IN);
        checkIn = cal.getTime();
        final int DAY_OUT = 27;
        cal.set(YEAR, MONTH, DAY_OUT);
        checkOut = cal.getTime();
        customer = new Customer("I", "Z", "i@z.com");
    }

    @Test
    void getInstance() {
        var reservationFactory = new ReservationFactory();
        ReservationService reservationServiceOther = ReservationService.getInstance(reservationFactory);

        assertSame(reservationServiceOther, reservationService);
    }

    @Test
    void addRoom_ok() {
        reservationService.addRoom(room1);

        String numberRoom2 = "2";
        var room2 = new Room(numberRoom2, 15.0D, RoomType.DOUBLE);
        reservationService.addRoom(room2);

        Map<String, IRoom> allRooms = reservationService.getRooms();

        assertAll(
                () -> assertEquals(2, allRooms.size()),
                () -> assertTrue(allRooms.containsKey(numberRoom1)),
                () -> assertTrue(allRooms.containsValue(room1)),
                () -> assertTrue(allRooms.containsKey(numberRoom2)),
                () -> assertTrue(allRooms.containsValue(room2))
        );
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
    void reserveARoom_ok() {
        reservationService.addRoom(room1);
        var reservationFactory = new ReservationFactory();
        Reservation reservationExpected = reservationFactory.create(customer, room1, checkIn, checkOut);
        Reservation reservation = reservationService.reserveARoom(customer, room1, checkIn, checkOut);

        // Check reservation was added to all reservations
        Set<Reservation> allReservations = reservationService.getAllReservations();

        assertAll(
                () -> assertEquals(reservationExpected, reservation),
                () -> assertEquals(1, allReservations.size()),
                () -> assertTrue(allReservations.contains(reservation))
        );
    }

    @Test
    void reserveARoom_exception_roomAlreadyReservedSameDates() {
        reservationService.addRoom(room1);
        reservationService.reserveARoom(customer, room1, checkIn, checkOut);

        // Try reserving with all same arguments again
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

        assertAll(
                () -> assertEquals(1, availableRooms.size()),
                () -> assertTrue(availableRooms.contains(room1))
        );
    }

    @ParameterizedTest(name = "[{index}] 20-27 booked, {0}-{1} available")
    @MethodSource("provide_availableDates")
    void findRooms_20_27Booked_desiredAvailable(int checkInDay, int checkOutDay) {
        reservationService.addRoom(room1);
        reservationService.reserveARoom(customer, room1, checkIn, checkOut);

        cal.set(YEAR, MONTH, checkInDay);
        Date checkInDesired = cal.getTime();
        cal.set(YEAR, MONTH, checkOutDay);
        Date checkOutDesired = cal.getTime();
        Collection<IRoom> availableRooms = reservationService.findRooms(checkInDesired, checkOutDesired);

        assertAll(
                () -> assertEquals(1, availableRooms.size()),
                () -> assertTrue(availableRooms.contains(room1))
        );
    }

    private static Stream<Arguments> provide_availableDates() {
        return Stream.of(
                Arguments.of(18, 19),
                Arguments.of(19, 20),
                Arguments.of(27, 28),
                Arguments.of(28, 29)
        );
    }

    @ParameterizedTest(name = "[{index}] 20-27 booked, {0}-{1} not available")
    @MethodSource("provide_notAvailableDates")
    void findRooms_20_27Booked_desiredNotAvailable(int checkInDay, int checkOutDay) {
        reservationService.addRoom(room1);
        reservationService.reserveARoom(customer, room1, checkIn, checkOut);

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
        Set<Reservation> allReservations = reservationService.getAllReservations();

        assertEquals(2, allReservations.size());
        assertTrue(allReservations.contains(reservationTwo));

        // Check that the first reservation is still there
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
}
