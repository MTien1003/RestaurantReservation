package com.restaurant.reservationsystem.dao;


import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private static final String INSERT_SQL = "INSERT INTO Customers (Name, Phone, Email, Address) VALUES (?, ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Customers WHERE CustomerID = ?";
    private static final String SELECT_BY_PHONE_SQL = "SELECT * FROM Customers WHERE Phone = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Customers";
    private static final String UPDATE_SQL = "UPDATE Customers SET Name = ?, Phone = ?, Email = ?, Address = ? WHERE CustomerID = ?";
    private static final String DELETE_SQL = "DELETE FROM Customers WHERE CustomerID = ?";

    public int addCustomer(Customer customer) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Customer getCustomerById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public Customer getCustomerByPhone(String phone) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_PHONE_SQL)) {

            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    public boolean updateCustomer(Customer customer) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5, customer.getCustomerId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCustomer(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("CustomerID"));
        customer.setName(rs.getString("Name"));
        customer.setPhone(rs.getString("Phone"));
        customer.setEmail(rs.getString("Email"));
        customer.setAddress(rs.getString("Address"));
        return customer;
    }
}
