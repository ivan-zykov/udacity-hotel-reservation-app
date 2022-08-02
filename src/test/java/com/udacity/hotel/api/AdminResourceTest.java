package com.udacity.hotel.api;

import com.udacity.hotel.model.Customer;
import com.udacity.hotel.model.IRoom;
import com.udacity.hotel.model.Room;
import com.udacity.hotel.model.RoomType;
import com.udacity.hotel.service.CustomerService;
import com.udacity.hotel.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminResourceTest {

    private AdminResource adminResource;

    private IRoom room1;
    private IRoom room2;

    @Mock
    private CustomerService customerService;
    @Mock
    private ReservationService reservationService;

    @BeforeEach
    void init() {
        adminResource = new AdminResource(customerService, reservationService);
        room1 = new Room("1", 100.0D, RoomType.SINGLE);
        room2 = new Room("2", 200.0D, RoomType.DOUBLE);
    }

    @Test
    void getCustomer() {
        String email = "i@z.com";
        var customerIz = new Customer("I", "Z", email);
        when(customerService.getCustomer(email)).thenReturn(customerIz);
        assertEquals(customerIz, adminResource.getCustomer(email));
    }

    @Test
    void addRoom() {
        adminResource.addRoom(List.of(room1, room2));
        verify(reservationService, times(1)).addRoom(room1);
        verify(reservationService, times(1)).addRoom(room2);
    }

    @Test
    void getAllRooms() {
        Map<String, IRoom> allRoomsMap = new HashMap<>();
        allRoomsMap.put(room1.getRoomNumber(), room1);
        allRoomsMap.put(room2.getRoomNumber(), room2);
        when(reservationService.getRooms()).thenReturn(allRoomsMap);
        Collection<IRoom> allRoomsList = adminResource.getAllRooms();
        assertEquals(2, allRoomsList.size());
        assertTrue(allRoomsList.contains(room1));
        assertTrue(allRoomsList.contains(room2));
    }

    @Test
    void getAllCustomers() {
        Collection<Customer> allCustomers = new ArrayList<>();
        when(customerService.getAllCustomers()).thenReturn(allCustomers);
        assertEquals(allCustomers, adminResource.getAllCustomers());
    }

    @Test
    void displayAllReservations() {
        adminResource.displayAllReservations();
        verify(reservationService, times(1)).printAllReservations();
    }
}
