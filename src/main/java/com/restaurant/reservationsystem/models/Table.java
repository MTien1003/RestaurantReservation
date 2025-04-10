package com.restaurant.reservationsystem.models;

public class Table {
        private int tableId;
        private String tableName;
        private int capacity;
        private String location;
        private String status; // "AVAILABLE", "RESERVED", "OCCUPIED"

        // Constructors
        public Table() {}

        public Table(String tableName, int capacity, String location) {
            this.tableName = tableName;
            this.capacity = capacity;
            this.location = location;
            this.status = "AVAILABLE";
        }

        // Getters and Setters
        public int getTableId() {
            return tableId;
        }

        public void setTableId(int tableId) {
            this.tableId = tableId;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        // Business logic methods
        public boolean isAvailable() {
            return "AVAILABLE".equals(status);
        }

        @Override
        public String toString() {
            return tableName + " (" + capacity + " người) - " + location;
        }

}
