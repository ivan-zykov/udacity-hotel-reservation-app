package com.udacity.hotel.model;

/**
 * A room that can be added to the pool of available rooms by an administrator, and booked by a user.
 *
 * @author Ivan V. Zykov
 */
public interface IRoom {

    public String getRoomNumber();
    public Double getRoomPrice();
    public RoomType getRoomType();

    /**
     * Indicates if the room is free of charge or not.
     *
     * @return      boolean
     */
    public boolean isFree();
}
