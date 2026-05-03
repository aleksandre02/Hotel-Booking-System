package com.hotel.reservation;

import com.hotel.auth.Guest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();

    public Reservation createReservation(int reservationId, Guest guest, int roomId, LocalDate startDate, LocalDate endDate) {
        Reservation newReservation = new Reservation(reservationId, guest, roomId, startDate, endDate);

        for (Reservation existing : reservations) {
            if (existing.isActive() && existing.checkConflict(newReservation)) {
                throw new IllegalStateException("Room already booked");
            }
        }

        reservations.add(newReservation);
        return newReservation;
    }

    public void cancelReservation(int reservationId) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservationId() == reservationId) {
                reservation.cancelReservation();
                return;
            }
        }

        throw new IllegalArgumentException("Reservation not found");
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
}
