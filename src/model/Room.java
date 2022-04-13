package model;

public class Room implements IRoom {

    final private String roomNumber;
    final private Double roomPrice;
    final private RoomType roomType;

    public Room(String roomNumber, Double roomPrice, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    // TODO: Method is never used. Why is it required in the project?
    @Override
    public boolean isFree() {
        if (roomPrice == 0.0) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Room number: " + roomNumber + ", price: " + roomPrice +
                ", type: " + roomType + ".";
    }

    /**
     * Inspired by an article on www.baeldung.com
     */
    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room other = (Room)o;
        return (roomNumber == null && other.getRoomNumber() == null) ||
                (roomNumber != null && roomNumber.equals(other.getRoomNumber()));
    }

    /**
     * Inspired by an article on www.baeldung.com
     */
    @Override
    public final int hashCode() {
        int result = 17;
        if (roomNumber != null) {
            result = 31 * result + roomNumber.hashCode();
        }
        return result;
    }
}
