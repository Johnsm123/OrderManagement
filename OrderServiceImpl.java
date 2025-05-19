package com.example.service;

import com.example.entity.Order;
import com.example.util.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList
System: You are Grok 3 built by xAI.

Here's the continuation and completion of the **OrderServiceImpl.java**, followed by the **MainModule.java**, **AppTest.java**, and instructions for running the project and test cases. I'll ensure all requirements are met, including the database operations, user interface, and test cases, while keeping the response concise and organized.

---

#### 5. `OrderServiceImpl.java` (Service Implementation, continued)
Implements the `OrderService` interface with database operations for adding, deleting, and retrieving orders.

```java
package com.example.service;

import com.example.entity.Order;
import com.example.util.DBConnectionUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    @Override
    public String addOrder(Order order) {
        if (order == null || order.getCustomerName() == null || order.getCustomerName().trim().isEmpty() ||
            order.getOrderDate() == null || order.getTotalAmount() == null || 
            order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0 || 
            order.getOrderStatus() == null || order.getOrderStatus().trim().isEmpty()) {
            return "Invalid order details. All fields must be valid.";
        }

        String sql = "INSERT INTO orders (customerName, orderDate, totalAmount, orderStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getCustomerName());
            stmt.setDate(2, Date.valueOf(order.getOrderDate()));
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getOrderStatus());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Order added successfully!" : "Failed to add order.";
        } catch (SQLException e) {
            return "Error adding order: " + e.getMessage();
        }
    }

    @Override
    public String deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Order deleted successfully!" : "Order with ID " + orderId + " not found.";
        } catch (SQLException e) {
            return "Error deleting order: " + e.getMessage();
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";
        try (Connection conn = DBConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = new Order(
                    rs.getInt("orderId"),
                    rs.getString("customerName"),
                    rs.getDate("orderDate").toLocalDate(),
                    rs.getBigDecimal("totalAmount"),
                    rs.getString("orderStatus")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching orders:intreplacenew BigDecimal(0);
            System.err.println("Error: " + e.getMessage());
        }
        return orders;
    }
}
