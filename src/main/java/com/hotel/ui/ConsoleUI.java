package com.hotel.ui;

import com.hotel.auth.AuthException;
import com.hotel.auth.AuthService;
import com.hotel.auth.Guest;
import com.hotel.auth.GuestService;
import com.hotel.auth.Role;
import com.hotel.auth.User;
import com.hotel.reservation.Reservation;
import com.hotel.reservation.ReservationService;
import com.hotel.room.DoubleRoom;
import com.hotel.room.Room;
import com.hotel.room.RoomService;
import com.hotel.room.RoomStatus;
import com.hotel.room.SingleRoom;
import com.hotel.room.Suite;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the Hotel Booking System.
 *
 * This class is Person 5's part of the project.
 * It connects authentication, room management, and reservation logic
 * through a menu-driven console application.
 */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);

    private final AuthService authService = new AuthService();
    private final GuestService guestService = new GuestService();
    private final RoomService roomService = new RoomService();
    private final ReservationService reservationService = new ReservationService();

    private final List<Room> rooms = new ArrayList<>();

    private int nextReservationId = 1;

    public ConsoleUI() {
        seedRooms();
    }

    /**
     * Starts the console menu loop.
     */
    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Choose option: ");

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    logout();
                    break;
                case 4:
                    showAllRooms();
                    break;
                case 5:
                    showAvailableRooms();
                    break;
                case 6:
                    filterRoomsByType();
                    break;
                case 7:
                    filterRoomsByMaxPrice();
                    break;
                case 8:
                    filterRoomsByTypeAndPrice();
                    break;
                case 9:
                    createReservation();
                    break;
                case 10:
                    cancelReservation();
                    break;
                case 11:
                    viewReservations();
                    break;
                case 12:
                    searchReservationsByRoomId();
                    break;
                case 13:
                    searchReservationsByDateRange();
                    break;
                case 14:
                    showActiveReservations();
                    break;
                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Creates sample rooms so the UI can demonstrate searching,
     * filtering, and reservation behavior.
     */
    private void seedRooms() {
        rooms.add(new SingleRoom(101, 80.00, RoomStatus.AVAILABLE));
        rooms.add(new SingleRoom(102, 90.00, RoomStatus.AVAILABLE));
        rooms.add(new DoubleRoom(201, 130.00, RoomStatus.AVAILABLE));
        rooms.add(new DoubleRoom(202, 150.00, RoomStatus.AVAILABLE));
        rooms.add(new Suite(301, 220.00, RoomStatus.AVAILABLE, false));
        rooms.add(new Suite(302, 280.00, RoomStatus.AVAILABLE, true));
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("=== Hotel Booking System ===");

        if (authService.isLoggedIn()) {
            User user = authService.getCurrentUser();
            System.out.println("Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            System.out.println("Not logged in");
        }

        System.out.println();
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Logout");
        System.out.println("4. Show all rooms");
        System.out.println("5. Show available rooms");
        System.out.println("6. Filter rooms by type");
        System.out.println("7. Filter rooms by max price");
        System.out.println("8. Filter rooms by type and max price");
        System.out.println("9. Create reservation");
        System.out.println("10. Cancel reservation");
        System.out.println("11. View reservations");
        System.out.println("12. Search reservations by room ID");
        System.out.println("13. Search reservations by date range");
        System.out.println("14. Show active reservations");
        System.out.println("0. Exit");
    }

    private void register() {
        System.out.println("\n--- Register ---");

        String username = readText("Username: ");
        String password = readText("Password: ");

        System.out.println("Choose role:");
        System.out.println("1. Guest");
        System.out.println("2. Admin");

        int roleChoice = readInt("Role option: ");
        Role role = roleChoice == 2 ? Role.ADMIN : Role.GUEST;

        try {
            User user = authService.register(username, password, role);
            System.out.println("Registered successfully:");
            System.out.println(user);
        } catch (AuthException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");

        String username = readText("Username: ");
        String password = readText("Password: ");

        try {
            User user = authService.login(username, password);
            System.out.println("Logged in successfully:");
            System.out.println(user);
        } catch (AuthException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void logout() {
        if (!authService.isLoggedIn()) {
            System.out.println("No user is currently logged in.");
            return;
        }

        authService.logout();
        System.out.println("Logged out successfully.");
    }

    private void showAllRooms() {
        System.out.println("\n--- All Rooms ---");
        printRooms(rooms);
    }

    private void showAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        List<Room> availableRooms = roomService.getAvailableRooms(rooms);
        printRooms(availableRooms);
    }

    private void filterRoomsByType() {
        System.out.println("\n--- Filter Rooms By Type ---");
        String type = readText("Enter room type: Single, Double, or Suite: ");

        List<Room> filteredRooms = roomService.filterByType(rooms, type);
        printRooms(filteredRooms);
    }

    private void filterRoomsByMaxPrice() {
        System.out.println("\n--- Filter Rooms By Max Price ---");
        double maxPrice = readDouble("Enter max price: ");

        List<Room> filteredRooms = roomService.filterByMaxPrice(rooms, maxPrice);
        printRooms(filteredRooms);
    }

    private void filterRoomsByTypeAndPrice() {
        System.out.println("\n--- Filter Rooms By Type And Price ---");

        String type = readText("Enter room type: Single, Double, or Suite: ");
        double maxPrice = readDouble("Enter max price: ");

        List<Room> filteredRooms = roomService.filterByTypeAndPrice(rooms, type, maxPrice);
        printRooms(filteredRooms);
    }

    private void createReservation() {
        System.out.println("\n--- Create Reservation ---");

        if (!authService.isLoggedIn()) {
            System.out.println("You must log in before creating a reservation.");
            return;
        }

        showAvailableRooms();

        int roomId = readInt("Enter room ID to reserve: ");
        Room selectedRoom = findRoomById(roomId);

        if (selectedRoom == null) {
            System.out.println("Room not found.");
            return;
        }

        if (!selectedRoom.checkAvailability()) {
            System.out.println("Room is not available.");
            return;
        }

        String guestName = readText("Guest name: ");
        String contactInfo = readText("Contact info: ");

        LocalDate startDate = readDate("Start date (YYYY-MM-DD): ");
        LocalDate endDate = readDate("End date (YYYY-MM-DD): ");

        try {
            User currentUser = authService.getCurrentUser();

            Guest guest = new Guest(
                    currentUser.getUserId(),
                    guestName,
                    contactInfo
            );

            guestService.addGuest(guest);

            Reservation reservation = reservationService.createReservation(
                    nextReservationId++,
                    guest,
                    roomId,
                    startDate,
                    endDate
            );

            roomService.reserveRoom(rooms, roomId);

            System.out.println("Reservation created successfully.");
            printReservation(reservation);

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Could not create reservation: " + e.getMessage());
        }
    }

    private void cancelReservation() {
        System.out.println("\n--- Cancel Reservation ---");

        if (!authService.isLoggedIn()) {
            System.out.println("You must log in before cancelling a reservation.");
            return;
        }

        int reservationId = readInt("Reservation ID: ");

        Reservation reservation = findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        try {
            reservationService.cancelReservation(reservationId);
            roomService.releaseRoom(rooms, reservation.getRoomId());
            System.out.println("Reservation cancelled successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Could not cancel reservation: " + e.getMessage());
        }
    }

    private void viewReservations() {
        System.out.println("\n--- All Reservations ---");
        printReservations(reservationService.getReservations());
    }

    private void searchReservationsByRoomId() {
        System.out.println("\n--- Search Reservations By Room ID ---");

        int roomId = readInt("Room ID: ");
        boolean found = false;

        for (Reservation reservation : reservationService.getReservations()) {
            if (reservation.getRoomId() == roomId) {
                printReservation(reservation);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No reservations found for room ID " + roomId + ".");
        }
    }

    private void searchReservationsByDateRange() {
        System.out.println("\n--- Search Reservations By Date Range ---");

        LocalDate searchStart = readDate("Search start date (YYYY-MM-DD): ");
        LocalDate searchEnd = readDate("Search end date (YYYY-MM-DD): ");

        if (!searchStart.isBefore(searchEnd)) {
            System.out.println("Invalid date range. Start date must be before end date.");
            return;
        }

        boolean found = false;

        for (Reservation reservation : reservationService.getReservations()) {
            boolean overlaps =
                    reservation.getStartDate().isBefore(searchEnd)
                            && searchStart.isBefore(reservation.getEndDate());

            if (overlaps) {
                printReservation(reservation);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No reservations found in that date range.");
        }
    }

    private void showActiveReservations() {
        System.out.println("\n--- Active Reservations ---");

        boolean found = false;

        for (Reservation reservation : reservationService.getReservations()) {
            if (reservation.isActive()) {
                printReservation(reservation);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No active reservations found.");
        }
    }

    private Room findRoomById(int roomId) {
        for (Room room : rooms) {
            if (room.getRoomId() == roomId) {
                return room;
            }
        }

        return null;
    }

    private Reservation findReservationById(int reservationId) {
        for (Reservation reservation : reservationService.getReservations()) {
            if (reservation.getReservationId() == reservationId) {
                return reservation;
            }
        }

        return null;
    }

    private void printRooms(List<Room> roomsToPrint) {
        if (roomsToPrint.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }

        for (Room room : roomsToPrint) {
            System.out.println(room.getDetails());
        }
    }

    private void printReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            return;
        }

        for (Reservation reservation : reservations) {
            printReservation(reservation);
        }
    }

    private void printReservation(Reservation reservation) {
        System.out.println("-----------------------------");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Guest: " + reservation.getGuest().getName());
        System.out.println("Contact: " + reservation.getGuest().getContactInfo());
        System.out.println("Room ID: " + reservation.getRoomId());
        System.out.println("Start date: " + reservation.getStartDate());
        System.out.println("End date: " + reservation.getEndDate());
        System.out.println("Active: " + reservation.isActive());
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("Input cannot be empty.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD.");
            }
        }
    }
}