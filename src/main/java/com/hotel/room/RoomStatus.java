package com.hotel.room;

/**
 * Represents the availability status of a hotel room.
 */
public enum RoomStatus {
    /** Room is available for booking. */
    AVAILABLE,

    /** Room has been reserved but guest has not yet checked in. */
    RESERVED,

    /** Room is currently occupied by a guest. */
    OCCUPIED
}