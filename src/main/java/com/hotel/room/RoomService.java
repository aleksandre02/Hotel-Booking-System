package com.hotel.room;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for room availability checks and filtering operations.
 * Works with a list of {@link Room} objects managed by the Hotel controller.
 */
public class RoomService {

    /**
     * Returns all rooms that are currently available for booking.
     *
     * @param rooms the full list of rooms to search
     * @return a list of rooms with status {@link RoomStatus#AVAILABLE}
     */
    public List<Room> getAvailableRooms(List<Room> rooms) {
        return rooms.stream()
                .filter(Room::checkAvailability)
                .collect(Collectors.toList());
    }

    /**
     * Filters rooms by type (case-insensitive).
     *
     * @param rooms the full list of rooms to search
     * @param type  the room type to filter by (e.g. "Single", "Double", "Suite")
     * @return a list of rooms matching the given type
     */
    public List<Room> filterByType(List<Room> rooms, String type) {
        return rooms.stream()
                .filter(r -> r.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Filters rooms by maximum price per night.
     *
     * @param rooms    the full list of rooms to search
     * @param maxPrice the maximum acceptable price per night (inclusive)
     * @return a list of rooms priced at or below {@code maxPrice}
     */
    public List<Room> filterByMaxPrice(List<Room> rooms, double maxPrice) {
        return rooms.stream()
                .filter(r -> r.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filters rooms by both type and maximum price.
     *
     * @param rooms    the full list of rooms to search
     * @param type     the room type to filter by (case-insensitive)
     * @param maxPrice the maximum acceptable price per night (inclusive)
     * @return a list of available rooms matching both criteria
     */
    public List<Room> filterByTypeAndPrice(List<Room> rooms, String type, double maxPrice) {
        return rooms.stream()
                .filter(r -> r.getType().equalsIgnoreCase(type) && r.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Marks a room as reserved if it is currently available.
     *
     * @param rooms  the full list of rooms to search
     * @param roomId the ID of the room to reserve
     * @return {@code true} if the room was found and successfully reserved,
     *         {@code false} if the room was not found or was not available
     */
    public boolean reserveRoom(List<Room> rooms, int roomId) {
        for (Room r : rooms) {
            if (r.getRoomId() == roomId && r.checkAvailability()) {
                r.updateAvailability(RoomStatus.RESERVED);
                return true;
            }
        }
        return false;
    }

    /**
     * Releases a room back to {@link RoomStatus#AVAILABLE} (e.g. after cancellation).
     *
     * @param rooms  the full list of rooms to search
     * @param roomId the ID of the room to release
     */
    public void releaseRoom(List<Room> rooms, int roomId) {
        for (Room r : rooms) {
            if (r.getRoomId() == roomId) {
                r.updateAvailability(RoomStatus.AVAILABLE);
                return;
            }
        }
    }
}