package model;

public class Room implements IRoom {

    private String roomNumber;
    private Double roomPrice;
    private RoomType roomType;

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

//    TODO: finish implementation
    @Override
    public boolean isFree() {
        return true;
    }

//    TODO: edit after isFree() done
    @Override
    public String toString() {
        return "Room number: " + roomNumber + ", price: " + roomPrice +
                ", type: " + roomType + ".";
    }
}
