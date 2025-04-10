package com.restaurant.reservationsystem.dao;

import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Customer;
import com.restaurant.reservationsystem.models.Reservation;
import com.restaurant.reservationsystem.models.Table;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static final String INSERT_SQL = "INSERT INTO Reservations (CustomerID, TableID, ReservationTime, EndTime, NumberOfGuests, Status, SpecialRequests) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Reservations WHERE ReservationID = ?";
    private static final String SELECT_BY_DATE_SQL = "SELECT * FROM Reservations WHERE CAST(ReservationTime AS DATE) = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Reservations";
    private static final String UPDATE_SQL = "UPDATE Reservations SET CustomerID = ?, TableID = ?, ReservationTime = ?, EndTime = ?, NumberOfGuests = ?, Status = ?, SpecialRequests = ? WHERE ReservationID = ?";
    private static final String DELETE_SQL = "DELETE FROM Reservations WHERE ReservationID = ?";

    private final CustomerDAO customerDao = new CustomerDAO();
    private final TableDAO tableDao = new TableDAO();

    public int addReservation(Reservation reservation) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reservation.getCustomer().getCustomerId());
            stmt.setInt(2, reservation.getTable().getTableId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndTime()));
            stmt.setInt(5, reservation.getNumberOfGuests());
            stmt.setString(6, reservation.getStatus());
            stmt.setString(7, reservation.getSpecialRequests());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Reservation getReservationById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        }
        return null;
    }

    public List<Reservation> getReservationsByDate(LocalDateTime date) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_DATE_SQL)) {

            stmt.setDate(1, Date.valueOf(date.toLocalDate()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
        }
        return reservations;
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }

    public boolean updateReservation(Reservation reservation) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setInt(1, reservation.getCustomer().getCustomerId());
            stmt.setInt(2, reservation.getTable().getTableId());
            stmt.setTimestamp(3, Timestamp.valueOf(reservation.getReservationTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(reservation.getEndTime()));
            stmt.setInt(5, reservation.getNumberOfGuests());
            stmt.setString(6, reservation.getStatus());
            stmt.setString(7, reservation.getSpecialRequests());
            stmt.setInt(8, reservation.getReservationId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteReservation(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("ReservationID"));

        Customer customer = customerDao.getCustomerById(rs.getInt("CustomerID"));
        Table table = tableDao.getTableById(rs.getInt("TableID"));

        reservation.setCustomer(customer);
        reservation.setTable(table);
        reservation.setReservationTime(rs.getTimestamp("ReservationTime").toLocalDateTime());
        reservation.setEndTime(rs.getTimestamp("EndTime").toLocalDateTime());
        reservation.setNumberOfGuests(rs.getInt("NumberOfGuests"));
        reservation.setStatus(rs.getString("Status"));
        reservation.setSpecialRequests(rs.getString("SpecialRequests"));

        return reservation;
    }
}