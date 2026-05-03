package com.hotel.reservation;

import com.hotel.auth.Guest;
import java.time.LocalDate;

public class Reservation {
    private int reservationId;
    private Guest guest;
    private int roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    public Reservation(int reservationId, Guest guest, int roomId, LocalDate startDate, LocalDate endDate) {
        if (guest == null) {
            throw new IllegalArgumentException("Guest required");
        }

        if (startDate == null || endDate == null || !startDate.isBefore(endDate)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        this.reservationId = reservationId;
        this.guest = guest;
        this.roomId = roomId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
    }

    public boolean checkConflict(Reservation other) {
        if (other == null || this.roomId != other.roomId) {
            return false;
        }

        return this.startDate.isBefore(other.endDate) && other.startDate.isBefore(this.endDate);
    }

    public void cancelReservation() {
        this.active = false;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Guest getGuest() {
        return guest;
    }

    public int getRoomId() {
        return roomId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isActive() {
        return active;
    }
}
