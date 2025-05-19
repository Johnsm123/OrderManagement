CREATE DATABASE appdb;
USE appdb;

CREATE TABLE orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    customerName VARCHAR(255) NOT NULL,
    orderDate DATE NOT NULL,
    totalAmount DECIMAL(10,2) NOT NULL,
    orderStatus VARCHAR(50) NOT NULL
);
