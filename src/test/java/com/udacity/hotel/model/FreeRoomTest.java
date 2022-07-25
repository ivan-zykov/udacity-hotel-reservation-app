package com.udacity.hotel.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreeRoomTest {

    @Test
    void testToString() {
        var freeRoom = new FreeRoom("1", RoomType.SINGLE);
        assertEquals("Free of charge. Room number: 1, price: 0.0, type: SINGLE.", freeRoom.toString());
    }
}
