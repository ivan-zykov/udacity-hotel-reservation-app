package com.udacity.hotel.api;

import com.udacity.hotel.model.*;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AdminResourceTest {

    private AdminResource adminResource;

    private IRoom room1;
    private IRoom room2;

    private static CustomerService customerService;
    private static ReservationService reservationService;

    @BeforeAll
    static void initAll() {
        customerService = CustomerService.getInstance();
        var reservationFactory = new ReservationFactory();
        reservationService = ReservationService.getInstance(reservationFactory);
    }

    @BeforeEach
    void reset() throws NoSuchFieldException, IllegalAccessException {
        // Reset CustomerService singleton
        Field customers = CustomerService.class.getDeclaredField("customers");
        customers.setAccessible(true);
        customers.set(customerService, new HashMap<>());

        // Reset ReservationService singleton
        Field reservations = ReservationService.class.getDeclaredField("reservations");
        reservations.setAccessible(true);
        reservations.set(reservationService, new HashSet<>());
        Field rooms = ReservationService.class.getDeclaredField("rooms");
        rooms.setAccessible(true);
        rooms.set(reservationService, new HashMap<>());

        // Instantiate SUT
        adminResource = new AdminResource(customerService, reservationService);

        room1 = new Room("1", 100.0D, RoomType.SINGLE);
        room2 = new Room("2", 200.0D, RoomType.DOUBLE);
    }

    @Test
    void getCustomer() {
        String email = "i@z.com";
        String firstName = "I";
        String lastName = "Z";
        var customerIz = new Customer(firstName, lastName, email);
        customerService.addCustomer(email, firstName, lastName);

        assertEquals(customerIz, adminResource.getCustomer(email));
    }

    @Test
    void addRoom() {
        adminResource.addRoom(List.of(room1, room2));

        Map<String, IRoom> allRooms = reservationService.getRooms();
        assertAll(
                () -> assertEquals(2, allRooms.size()),
                () -> assertTrue(allRooms.containsValue(room1)),
                () -> assertTrue(allRooms.containsValue(room2))
        );
    }

    @Test
    void getAllRooms() {
        reservationService.addRoom(room1);
        reservationService.addRoom(room2);

        List<IRoom> expected = List.of(room1, room2);
        assertEquals(expected, adminResource.getAllRooms());
    }

    @Test
    void getAllCustomers() {
        String izFirst = "I";
        String izLast = "Z";
        String izEmail = "i@z.com";
        customerService.addCustomer(izEmail, izFirst, izLast);
        String jrFirst = "J";
        String jrLast = "R";
        String jrEmail = "j@r.com";
        customerService.addCustomer(jrEmail, jrFirst, jrLast);

        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        var iZ = new Customer(izFirst, izLast, izEmail);
        var jR = new Customer(jrFirst, jrLast, jrEmail);
        assertAll(
                () -> assertTrue(allCustomers.contains(iZ)),
                () -> assertTrue(allCustomers.contains(jR))
        );
    }

    @Test
    void getAllReservations() {
        var customer = new Customer("I", "Z", "i@z.com");
        Calendar cal = Calendar.getInstance();
        final int year = 2099;
        final int month = Calendar.MAY;
        cal.set(year, month, 20);
        Date checkIn = cal.getTime();
        cal.set(year, month, 27);
        Date checkOut = cal.getTime();

        Set<Reservation> expected = Set.of(reservationService.reserveARoom(customer, room1, checkIn, checkOut));
        assertEquals(expected, adminResource.getAllReservations());
    }
}
