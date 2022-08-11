package com.udacity.hotel.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private Room room;

    @BeforeEach
    void init() {
        room = new Room("1", 10.0D, RoomType.SINGLE);
    }

    @Test
    void toStringTest() {
        assertEquals("Room number: 1, price: 10.0, type: SINGLE.", room.toString());
    }

    @ParameterizedTest(name = "[{index}] This room number: 1, Other room number: {0}")
    @MethodSource("provide_roomNumbers")
    void equalsTest_sameAndDifferentNumbers(String roomNumber, boolean expectedRes) {
        var otherRoom = new Room(roomNumber, 20.0D, RoomType.DOUBLE);
        assertEquals(expectedRes, room.equals(otherRoom));
    }

    private static Stream<Arguments> provide_roomNumbers() {
        return Stream.of(
          Arguments.of("1", true),
          Arguments.of("2", false)
        );
    }

    @Test
    void equalsTest_nullNumbers() {
        var nullNumber1 = new Room(null, 10.0D, RoomType.SINGLE);
        var nullNumber2 = new Room(null, 10.0D, RoomType.SINGLE);
        assertEquals(nullNumber1, nullNumber2);
    }
}
