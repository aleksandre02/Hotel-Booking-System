package com.hotel.room;

/**
 * Abstract base class representing a hotel room.
 * Serves as the parent class for all room types (Single, Double, Suite).
 */
public abstract class Room {
    private int roomId;
    private final String type;
    private double price;
    private RoomStatus status;

    /**
     * Constructs a Room after persistence (with ID assigned by DB).
     *
     * @param roomId the room ID
     * @param type   the room type label (e.g. "Single", "Double", "Suite")
     * @param price  the price per night
     * @param status the current room status
     */
    public Room(int roomId, String type, double price, RoomStatus status) {
        this.roomId = roomId;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    /**
     * Constructs a Room before persistence (no ID yet).
     * Status defaults to {@link RoomStatus#AVAILABLE}.
     *
     * @param type  the room type label
     * @param price the price per night
     */
    public Room(String type, double price) {
        this.type = type;
        this.price = price;
        this.status = RoomStatus.AVAILABLE;
    }

    /**
     * Checks whether the room is currently available for booking.
     *
     * @return {@code true} if status is {@link RoomStatus#AVAILABLE}
     */
    public boolean checkAvailability() {
        return this.status == RoomStatus.AVAILABLE;
    }

    /**
     * Updates the room's availability status.
     *
     * @param status the new {@link RoomStatus} to set
     */
    public void updateAvailability(RoomStatus status) {
        this.status = status;
    }

    /**
     * Returns a human-readable description of the room.
     * Each subclass provides its own implementation.
     *
     * @return a formatted string with room details
     */
    public abstract String getDetails();

    /**
     * Gets the room ID.
     *
     * @return the room ID
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Sets the room ID (used after DB insertion).
     *
     * @param roomId the assigned room ID
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * Gets the room type label.
     *
     * @return the type string
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the price per night.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price per night.
     *
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the current room status.
     *
     * @return the {@link RoomStatus}
     */
    public RoomStatus getStatus() {
        return status;
    }

    /**
     * Sets the room status directly.
     *
     * @param status the new status
     */
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{id=" + roomId + ", type='" + type + "', price=" + price + ", status=" + status + "}";
    }
}