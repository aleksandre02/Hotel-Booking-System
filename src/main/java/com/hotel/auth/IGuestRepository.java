package com.hotel.auth;

import java.util.List;

/**
 * Interface for guest repository operations.
 */
public interface IGuestRepository {

    /**
     * Adds a guest.
     * @param guest the guest to add
     */
    void addGuest(Guest guest);

    /**
     * Gets a guest by ID.
     * @param id the guest ID
     * @return the guest or null if not found
     */
    Guest getGuestById(int id);

    /**
     * Gets all guests.
     * @return list of all guests
     */
    List<Guest> getAllGuests();
}