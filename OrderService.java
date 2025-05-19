package com.example.service;

import com.example.entity.Order;
import java.util.List;

public interface OrderService {
    String addOrder(Order order);
    String deleteOrder(int orderId);
    List<Order> getAllOrders();
}
