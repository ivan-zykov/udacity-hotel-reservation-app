package com.udacity.hotel.model;

/**
 * An object of a normal room that is not for free and holds basic information about it.
 *
 * @author Ivan V. Zykov
 */
public class Room implements IRoom {

    private final String roomNumber;
    private final Double roomPrice;
    private final RoomType roomType;

    /**
     * Constructor for this class.
     *
     * @param roomNumber    string, room number
     * @param roomPrice     string, room price
     * @param roomType      string, type of the room
     */
    public Room(String roomNumber, Double roomPrice, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Includes basic data about the room and formats its string representation.
     *
     * @return      string representation of the customer's data
     */
    @Override
    public String toString() {
        return "Room number: " + roomNumber + ", price: " + roomPrice +
                ", type: " + roomType + ".";
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Room other)) {
            return false;
        }
        return (roomNumber == null && other.getRoomNumber() == null) ||
                (roomNumber != null && roomNumber.equals(other.getRoomNumber()));
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (roomNumber != null) {
            result = 31 * result + roomNumber.hashCode();
        }
        return result;
    }
}
