package model;

/**
 * A room that is free of charge to book and holds basic information about it.
 */
public class FreeRoom extends Room {

    /**
     * Constructor of this class.
     *
     * @param roomNumber    string, room number
     * @param roomType      string, room type
     */
    public FreeRoom(String roomNumber, RoomType roomType) {
        super(roomNumber, 0.0, roomType);
    }

    @Override
    public String toString() {
        return "Free of charge. " + super.toString();
    }
}
