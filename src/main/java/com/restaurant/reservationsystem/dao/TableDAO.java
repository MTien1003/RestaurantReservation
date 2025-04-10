package com.restaurant.reservationsystem.dao;



import com.restaurant.reservationsystem.config.DatabaseConfig;
import com.restaurant.reservationsystem.models.Table;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    private static final String INSERT_SQL = "INSERT INTO Tables (TableName, Capacity, Location) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Tables WHERE TableID = ?";
    private static final String SELECT_AVAILABLE_SQL = "SELECT * FROM Tables WHERE Status = 'AVAILABLE'";
    private static final String SELECT_ALL_SQL = "SELECT * FROM Tables";
    private static final String UPDATE_SQL = "UPDATE Tables SET TableName = ?, Capacity = ?, Location = ? WHERE TableID = ?";
    private static final String UPDATE_STATUS_SQL = "UPDATE Tables SET Status = ? WHERE TableID = ?";
    private static final String DELETE_SQL = "DELETE FROM Tables WHERE TableID = ?";

    public int addTable(Table table) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, table.getTableName());
            stmt.setInt(2, table.getCapacity());
            stmt.setString(3, table.getLocation());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public Table getTableById(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTable(rs);
                }
            }
        }
        return null;
    }

    public List<Table> getAvailableTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_AVAILABLE_SQL)) {

            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        }
        return tables;
    }

    public List<Table> getAllTables() throws SQLException {
        List<Table> tables = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                tables.add(mapResultSetToTable(rs));
            }
        }
        return tables;
    }

    public boolean updateTable(Table table) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, table.getTableName());
            stmt.setInt(2, table.getCapacity());
            stmt.setString(3, table.getLocation());
            stmt.setInt(4, table.getTableId());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateTableStatus(int tableId, String status) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            stmt.setString(1, status);
            stmt.setInt(2, tableId);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteTable(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Table mapResultSetToTable(ResultSet rs) throws SQLException {
        Table table = new Table();
        table.setTableId(rs.getInt("TableID"));
        table.setTableName(rs.getString("TableName"));
        table.setCapacity(rs.getInt("Capacity"));
        table.setLocation(rs.getString("Location"));
        table.setStatus(rs.getString("Status"));
        return table;
    }
}
