package com.hotel.room;

/**
 * Represents a suite — a premium hotel room type.
 * Extends {@link Room} with an additional jacuzzi feature flag.
 */
public class Suite extends Room {
    private boolean hasJacuzzi;

    /**
     * Constructs a Suite after persistence (with DB-assigned ID).
     *
     * @param roomId     the room ID
     * @param price      the price per night
     * @param status     the current room status
     * @param hasJacuzzi whether this suite includes a jacuzzi
     */
    public Suite(int roomId, double price, RoomStatus status, boolean hasJacuzzi) {
        super(roomId, "Suite", price, status);
        this.hasJacuzzi = hasJacuzzi;
    }

    /**
     * Constructs a Suite before persistence (no ID yet).
     * Status defaults to {@link RoomStatus#AVAILABLE}.
     *
     * @param price      the price per night
     * @param hasJacuzzi whether this suite includes a jacuzzi
     */
    public Suite(double price, boolean hasJacuzzi) {
        super("Suite", price);
        this.hasJacuzzi = hasJacuzzi;
    }

    /**
     * Gets whether this suite has a jacuzzi.
     *
     * @return {@code true} if the suite has a jacuzzi
     */
    public boolean isHasJacuzzi() {
        return hasJacuzzi;
    }

    /**
     * Sets whether this suite has a jacuzzi.
     *
     * @param hasJacuzzi the jacuzzi flag
     */
    public void setHasJacuzzi(boolean hasJacuzzi) {
        this.hasJacuzzi = hasJacuzzi;
    }

    /**
     * Returns a formatted description of this suite.
     *
     * @return a string with room ID, price, jacuzzi flag, and status
     */
    @Override
    public String getDetails() {
        return "Suite{id=" + getRoomId() + ", price=" + getPrice() + ", hasJacuzzi=" + hasJacuzzi + ", status=" + getStatus() + "}";
    }
}