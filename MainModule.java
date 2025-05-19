package com.example;

import com.example.entity.Order;
import com.example.service.OrderService;
import com.example.service.OrderServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static final OrderService orderService = new OrderServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    addOrder(orderService);
                    break;
                case "2":
                    deleteOrder(orderService);
                    break;
                case "3":
                    viewAllOrders(orderService);
                    break;
                case "4":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== Order Management System ===");
        System.out.println("1. Add Order");
        System.out.println("2. Delete Order");
        System.out.println("3. View All Orders");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addOrder(OrderService orderService) {
        try {
            System.out.print("Enter customer name: ");
            String customerName = scanner.nextLine().trim();
            if (customerName.isEmpty()) {
                System.out.println("Customer name cannot be empty.");
                return;
            }

            System.out.print("Enter order date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine().trim();
            LocalDate orderDate = LocalDate.parse(dateStr, dateFormatter);

            System.out.print("Enter total amount: ");
            String amountStr = scanner.nextLine().trim();
            BigDecimal totalAmount = new BigDecimal(amountStr);
            if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Total amount must be greater than 0.");
                return;
            }

            System.out.print("Enter order status (Pending/Shipped/Delivered): ");
            String orderStatus = scanner.nextLine().trim();
            if (orderStatus.isEmpty()) {
                System.out.println("Order status cannot be empty.");
                return;
            }

            Order order = new Order(0, customerName, orderDate, totalAmount, orderStatus);
            String result = orderService.addOrder(order);
            System.out.println(result);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteOrder(OrderService orderService) {
        try {
            System.out.print("Enter order ID to delete: ");
            int orderId = Integer.parseInt(scanner.nextLine().trim());
            String result = orderService.deleteOrder(orderId);
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID. Enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllOrders(OrderService orderService) {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            System.out.println("\n=== All Orders ===");
            for (Order order : orders) {
                System.out.println(order);
            }
        }
    }
}
