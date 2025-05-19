package com.example;

import com.example.entity.Order;
import com.example.service.OrderService;
import com.example.service.OrderServiceImpl;
import com.example.util.DBConnectionUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    private static OrderService orderService;

    @BeforeAll
    public static void setup() {
        orderService = new OrderServiceImpl();
    }

    // Non-Functional Test Cases
    @Test
    public void test_Util_File_Exist() {
        File file = new File("src/main/java/com/example/util/DBConnectionUtil.java");
        assertTrue(file.exists(), "DBConnectionUtil.java should exist in the util folder.");
    }

    @Test
    public void test_Util_Folder_Exist() {
        File folder = new File("src/main/java/com/example/util");
        assertTrue(folder.exists() && folder.isDirectory(), "Util folder should exist.");
    }

    @Test
    public void test_Check_Method_Exist() {
        try {
            OrderServiceImpl.class.getDeclaredMethod("addOrder", Order.class);
            OrderServiceImpl.class.getDeclaredMethod("deleteOrder", int.class);
            OrderServiceImpl.class.getDeclaredMethod("getAllOrders");
            assertTrue(true, "All required methods exist in OrderServiceImpl.");
        } catch (NoSuchMethodException e) {
            fail("One or more required methods are missing in OrderServiceImpl: " + e.getMessage());
        }
    }

    // Functional Test Cases
    @Test
    public void test_Create_Query_Exist() throws Exception {
        Order order = new Order(0, "Test Customer", LocalDate.now(), new BigDecimal("99.99"), "Pending");
        String result = orderService.addOrder(order);

        assertEquals("Order added successfully!", result, "Order should be added successfully.");

        // Verify in database
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE customerName = ?")) {
            stmt.setString(1, "Test Customer");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next(), "Order should exist in the database.");
        }
    }

    @Test
    public void test_Delete_Query_Exist() throws Exception {
        // First, add an order
        Order order = new Order(0, "Test Delete", LocalDate.now(), new BigDecimal("49.99"), "Pending");
        orderService.addOrder(order);

        // Get the order ID
        int orderId;
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT orderId FROM orders WHERE customerName = ?")) {
            stmt.setString(1, "Test Delete");
            ResultSet rs = stmt.executeQuery();
            rs.next();
            orderId = rs.getInt("orderId");
        }

        // Delete the order
        String result = orderService.deleteOrder(orderId);
        assertEquals("Order deleted successfully!", result, "Order should be deleted successfully.");

        // Verify deletion
        try (Connection conn = DBConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM orders WHERE orderId = ?")) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            assertFalse(rs.next(), "Order should not exist in the database after deletion.");
        }
    }
}
