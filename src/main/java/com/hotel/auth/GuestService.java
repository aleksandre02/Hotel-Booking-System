package com.hotel.auth;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of guest repository with in-memory storage.
 */
public class GuestService implements IGuestRepository {
    private final List<Guest> guests = new ArrayList<>();
    private int nextGuestId = 1;

    @Override
    public void addGuest(Guest guest) {
        guest.setGuestId(nextGuestId++);
        guests.add(guest);
    }

    @Override
    public Guest getGuestById(int id) {
        for (Guest g : guests) {
            if (g.getGuestId() == id) {
                return g;
            }
        }
        return null;
    }

    @Override
    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }
}