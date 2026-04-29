package com.hotel.room;

/**
 * Represents a double-occupancy hotel room.
 * Extends {@link Room} for rooms designed for two guests.
 */
public class DoubleRoom extends Room {

    /**
     * Constructs a DoubleRoom after persistence (with DB-assigned ID).
     *
     * @param roomId the room ID
     * @param price  the price per night
     * @param status the current room status
     */
    public DoubleRoom(int roomId, double price, RoomStatus status) {
        super(roomId, "Double", price, status);
    }

    /**
     * Constructs a DoubleRoom before persistence (no ID yet).
     * Status defaults to {@link RoomStatus#AVAILABLE}.
     *
     * @param price the price per night
     */
    public DoubleRoom(double price) {
        super("Double", price);
    }

    /**
     * Returns a formatted description of this double room.
     *
     * @return a string with room ID, price, and status
     */
    @Override
    public String getDetails() {
        return "DoubleRoom{id=" + getRoomId() + ", price=" + getPrice() + ", status=" + getStatus() + "}";
    }
}