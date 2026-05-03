package com.hotel.room;

/**
 * Represents a single-occupancy hotel room.
 * Extends {@link Room} for rooms designed for one guest.
 */
public class SingleRoom extends Room {

    /**
     * Constructs a SingleRoom after persistence (with DB-assigned ID).
     *
     * @param roomId the room ID
     * @param price  the price per night
     * @param status the current room status
     */
    public SingleRoom(int roomId, double price, RoomStatus status) {
        super(roomId, "Single", price, status);
    }

    /**
     * Constructs a SingleRoom before persistence (no ID yet).
     * Status defaults to {@link RoomStatus#AVAILABLE}.
     *
     * @param price the price per night
     */
    public SingleRoom(double price) {
        super("Single", price);
    }

    /**
     * Returns a formatted description of this single room.
     *
     * @return a string with room ID, price, and status
     */
    @Override
    public String getDetails() {
        return "SingleRoom{id=" + getRoomId() + ", price=" + getPrice() + ", status=" + getStatus() + "}";
    }
}