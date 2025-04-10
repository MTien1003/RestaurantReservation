package com.restaurant.reservationsystem;

import com.restaurant.reservationsystem.dao.CustomerDAO;
import com.restaurant.reservationsystem.models.Customer;
import java.sql.SQLException;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        CustomerDAO customerDao = new CustomerDAO();

        try {
            System.out.println("DANH SÁCH KHÁCH HÀNG");

            // Lấy tất cả khách hàng từ database
            List<Customer> customers = customerDao.getAllCustomers();

            if (customers.isEmpty()) {
                System.out.println("Không có khách hàng nào trong hệ thống");
            }
            else {
                for (Customer customer : customers) {
                    System.out.printf("%-5d | %-20s | %-12s | %-25s",
                            customer.getCustomerId(),
                            customer.getName(),
                            customer.getPhone(),
                            customer.getEmail() != null ? customer.getEmail() : "N/A");
                    System.out.println("\n");
                }

                System.out.println("\nTổng số khách hàng: " + customers.size());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn database:");
            e.printStackTrace();
        }
    }
}