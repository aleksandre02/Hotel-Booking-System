package com.hotel.ui;

import com.hotel.auth.*;
import com.hotel.reservation.Reservation;
import com.hotel.reservation.ReservationService;
import com.hotel.room.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing-based graphical user interface for the Hotel Booking System.
 * Replaces the console-based ConsoleUI.
 */
public class SwingUI extends JFrame {
    private final AuthService authService = new AuthService();
    private final GuestService guestService = new GuestService();
    private final RoomService roomService = new RoomService();
    private final ReservationService reservationService = new ReservationService();

    private final List<Room> rooms = new ArrayList<>();
    private int nextReservationId = 1;

    private JPanel mainPanel;
    private JLabel userStatusLabel;
    private CardLayout cardLayout;

    public SwingUI() {
        setTitle("Hotel Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        seedRooms();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createMainMenuPanel(), "menu");
        mainPanel.add(createRoomsPanel(), "rooms");
        mainPanel.add(createAvailableRoomsPanel(), "availableRooms");
        mainPanel.add(createFilterPanel(), "filter");
        mainPanel.add(createReservationPanel(), "reservation");
        mainPanel.add(createViewReservationsPanel(), "viewReservations");
        mainPanel.add(createSearchReservationsPanel(), "searchReservations");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    private void seedRooms() {
        rooms.add(new SingleRoom(101, 80.00, RoomStatus.AVAILABLE));
        rooms.add(new SingleRoom(102, 90.00, RoomStatus.AVAILABLE));
        rooms.add(new DoubleRoom(201, 130.00, RoomStatus.AVAILABLE));
        rooms.add(new DoubleRoom(202, 150.00, RoomStatus.AVAILABLE));
        rooms.add(new Suite(301, 220.00, RoomStatus.AVAILABLE, false));
        rooms.add(new Suite(302, 280.00, RoomStatus.AVAILABLE, true));
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Login", createLoginTabPanel());
        tabbedPane.addTab("Register", createRegisterTabPanel());

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLoginTabPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JButton loginButton = new JButton("Login");
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User user = authService.login(username, password);
                JOptionPane.showMessageDialog(this, "Logged in as: " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
                updateUserStatus();
                cardLayout.show(mainPanel, "menu");
            } catch (AuthException ex) {
                JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createRegisterTabPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Role:"), gbc);

        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Guest", "Admin"});
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JButton registerButton = new JButton("Register");
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            Role role = roleCombo.getSelectedIndex() == 0 ? Role.GUEST : Role.ADMIN;

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                User user = authService.register(username, password, role);
                JOptionPane.showMessageDialog(this, "Registered successfully: " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            } catch (AuthException ex) {
                JOptionPane.showMessageDialog(this, "Registration failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userStatusLabel = new JLabel();
        updateUserStatus();
        topPanel.add(userStatusLabel);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        topPanel.add(logoutButton);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMenuButton(buttonPanel, "Show All Rooms", e -> cardLayout.show(mainPanel, "rooms"));
        addMenuButton(buttonPanel, "Show Available Rooms", e -> cardLayout.show(mainPanel, "availableRooms"));
        addMenuButton(buttonPanel, "Filter Rooms", e -> cardLayout.show(mainPanel, "filter"));
        addMenuButton(buttonPanel, "Create Reservation", e -> cardLayout.show(mainPanel, "reservation"));
        addMenuButton(buttonPanel, "View Reservations", e -> showViewReservationsPanel());
        addMenuButton(buttonPanel, "Search Reservations", e -> cardLayout.show(mainPanel, "searchReservations"));
        addMenuButton(buttonPanel, "Back to Login", e -> {
            authService.logout();
            updateUserStatus();
            cardLayout.show(mainPanel, "login");
        });

        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addMenuButton(JPanel panel, String buttonText, java.awt.event.ActionListener action) {
        JButton button = new JButton(buttonText);
        button.addActionListener(action);
        button.setPreferredSize(new Dimension(150, 50));
        panel.add(button);
    }

    private JPanel createRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTable roomTable = createRoomTable(rooms);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAvailableRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Available Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        List<Room> availableRooms = roomService.getAvailableRooms(rooms);
        JTable roomTable = createRoomTable(availableRooms);
        panel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterInputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        filterInputPanel.add(new JLabel("Room Type:"), gbc);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Any", "Single", "Double", "Suite"});
        gbc.gridx = 1;
        filterInputPanel.add(typeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        filterInputPanel.add(new JLabel("Max Price:"), gbc);
        JTextField priceField = new JTextField(10);
        gbc.gridx = 1;
        filterInputPanel.add(priceField, gbc);

        DefaultTableModel resultModel = new DefaultTableModel(new String[]{"Room ID", "Type", "Price", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable resultTable = new JTable(resultModel);

        JButton filterButton = new JButton("Apply Filter");
        filterButton.addActionListener(e -> {
            try {
                String selectedType = (String) typeCombo.getSelectedItem();
                String priceText = priceField.getText().trim();
                double maxPrice = Double.MAX_VALUE;

                if (!priceText.isEmpty()) {
                    maxPrice = Double.parseDouble(priceText);
                }

                List<Room> filtered = rooms;

                if (!"Any".equals(selectedType)) {
                    filtered = roomService.filterByType(filtered, selectedType);
                }

                if (!priceText.isEmpty()) {
                    filtered = roomService.filterByMaxPrice(filtered, maxPrice);
                }

                updateRoomTable(resultModel, filtered);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 2;
        filterInputPanel.add(filterButton, gbc);

        panel.add(filterInputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Guest Name:"), gbc);
        JTextField guestNameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(guestNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Contact Info:"), gbc);
        JTextField contactField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Room ID:"), gbc);
        JSpinner roomIdSpinner = new JSpinner(new SpinnerNumberModel(101, 101, 302, 1));
        gbc.gridx = 1;
        inputPanel.add(roomIdSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        JTextField startDateField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        JTextField endDateField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(endDateField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton createButton = new JButton("Create Reservation");
        createButton.addActionListener(e -> {
            if (!authService.isLoggedIn()) {
                JOptionPane.showMessageDialog(this, "You must log in first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String guestName = guestNameField.getText().trim();
                String contact = contactField.getText().trim();
                int roomId = (Integer) roomIdSpinner.getValue();
                LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

                if (guestName.isEmpty() || contact.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Room selectedRoom = findRoomById(roomId);
                if (selectedRoom == null || !selectedRoom.checkAvailability()) {
                    JOptionPane.showMessageDialog(this, "Room not available.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User currentUser = authService.getCurrentUser();
                Guest guest = new Guest(currentUser.getUserId(), guestName, contact);
                guestService.addGuest(guest);

                Reservation reservation = reservationService.createReservation(nextReservationId++, guest, roomId, startDate, endDate);
                roomService.reserveRoom(rooms, roomId);

                JOptionPane.showMessageDialog(this, "Reservation created!\nID: " + reservation.getReservationId(), "Success", JOptionPane.INFORMATION_MESSAGE);
                guestNameField.setText("");
                contactField.setText("");
                startDateField.setText("");
                endDateField.setText("");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        inputPanel.add(createButton, gbc);

        panel.add(inputPanel, BorderLayout.NORTH);

        DefaultTableModel availableModel = new DefaultTableModel(new String[]{"Room ID", "Type", "Price", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        List<Room> availableRooms = roomService.getAvailableRooms(rooms);
        updateRoomTable(availableModel, availableRooms);
        JTable availableTable = new JTable(availableModel);
        panel.add(new JScrollPane(availableTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All Reservations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Guest", "Room ID", "Start Date", "End Date", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Reservation res : reservationService.getReservations()) {
            model.addRow(new Object[]{
                res.getReservationId(),
                res.getGuest().getName(),
                res.getRoomId(),
                res.getStartDate(),
                res.getEndDate(),
                res.isActive()
            });
        }

        JTable reservationTable = new JTable(model);
        panel.add(new JScrollPane(reservationTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton cancelButton = new JButton("Cancel Selected");
        cancelButton.addActionListener(e -> {
            int selectedRow = reservationTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int reservationId = (int) model.getValueAt(selectedRow, 0);
            try {
                Reservation res = findReservationById(reservationId);
                if (res != null) {
                    reservationService.cancelReservation(reservationId);
                    roomService.releaseRoom(rooms, res.getRoomId());
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Reservation cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(cancelButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showViewReservationsPanel() {
        JPanel panel = (JPanel) ((BorderLayout) mainPanel.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        if (panel != null && panel == mainPanel.getComponent(6)) {
            DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) ((BorderLayout) panel.getLayout()).getLayoutComponent(BorderLayout.CENTER)).getViewport().getView()).getModel();
            model.setRowCount(0);
            for (Reservation res : reservationService.getReservations()) {
                model.addRow(new Object[]{
                    res.getReservationId(),
                    res.getGuest().getName(),
                    res.getRoomId(),
                    res.getStartDate(),
                    res.getEndDate(),
                    res.isActive()
                });
            }
        }
        cardLayout.show(mainPanel, "viewReservations");
    }

    private JPanel createSearchReservationsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Search Type:"), gbc);
        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{"By Room ID", "By Date Range", "Active Only"});
        gbc.gridx = 1;
        searchPanel.add(searchTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        searchPanel.add(new JLabel("Room ID:"), gbc);
        JSpinner roomIdSpinner = new JSpinner(new SpinnerNumberModel(101, 101, 302, 1));
        gbc.gridx = 1;
        searchPanel.add(roomIdSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        searchPanel.add(new JLabel("Start Date (YYYY-MM-DD):"), gbc);
        JTextField startDateField = new JTextField(15);
        gbc.gridx = 1;
        searchPanel.add(startDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        searchPanel.add(new JLabel("End Date (YYYY-MM-DD):"), gbc);
        JTextField endDateField = new JTextField(15);
        gbc.gridx = 1;
        searchPanel.add(endDateField, gbc);

        DefaultTableModel resultModel = new DefaultTableModel(new String[]{"ID", "Guest", "Room ID", "Start Date", "End Date", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable resultTable = new JTable(resultModel);

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {
            resultModel.setRowCount(0);
            String searchType = (String) searchTypeCombo.getSelectedItem();

            try {
                if ("By Room ID".equals(searchType)) {
                    int roomId = (Integer) roomIdSpinner.getValue();
                    for (Reservation res : reservationService.getReservations()) {
                        if (res.getRoomId() == roomId) {
                            addReservationRow(resultModel, res);
                        }
                    }
                } else if ("By Date Range".equals(searchType)) {
                    LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
                    LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

                    for (Reservation res : reservationService.getReservations()) {
                        boolean overlaps = res.getStartDate().isBefore(endDate) && startDate.isBefore(res.getEndDate());
                        if (overlaps) {
                            addReservationRow(resultModel, res);
                        }
                    }
                } else {
                    for (Reservation res : reservationService.getReservations()) {
                        if (res.isActive()) {
                            addReservationRow(resultModel, res);
                        }
                    }
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 4;
        searchPanel.add(searchButton, gbc);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private JTable createRoomTable(List<Room> roomList) {
        DefaultTableModel model = new DefaultTableModel(new String[]{"Room ID", "Type", "Price", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        updateRoomTable(model, roomList);
        return new JTable(model);
    }

    private void updateRoomTable(DefaultTableModel model, List<Room> roomList) {
        model.setRowCount(0);
        for (Room room : roomList) {
            String type = room.getClass().getSimpleName();
            model.addRow(new Object[]{
                room.getRoomId(),
                type,
                "$" + String.format("%.2f", room.getPrice()),
                room.getStatus()
            });
        }
    }

    private void addReservationRow(DefaultTableModel model, Reservation res) {
        model.addRow(new Object[]{
            res.getReservationId(),
            res.getGuest().getName(),
            res.getRoomId(),
            res.getStartDate(),
            res.getEndDate(),
            res.isActive()
        });
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

    private void logout() {
        authService.logout();
        updateUserStatus();
        cardLayout.show(mainPanel, "login");
    }

    private void updateUserStatus() {
        if (authService.isLoggedIn()) {
            User user = authService.getCurrentUser();
            userStatusLabel.setText("Logged in as: " + user.getUsername() + " (" + user.getRole() + ")");
        } else {
            userStatusLabel.setText("Not logged in");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingUI::new);
    }
}
