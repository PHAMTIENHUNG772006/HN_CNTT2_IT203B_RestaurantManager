drop database restaurant_manager;
create database restaurant_manager;
--
USE restaurant_manager;

--  bảng tài khoản
CREATE TABLE IF NOT EXISTS Account(
    account_id INT AUTO_INCREMENT PRIMARY KEY ,
    account_name VARCHAR(100) NOT NULL UNIQUE ,
    password VARCHAR(200) NOT NULL ,
    isban BOOLEAN,
    role ENUM('CUSTOMER','CHEF','MANAGER')
    );

-- bảng món ản
CREATE TABLE IF NOT EXISTS Menu_items (
    food_id INT PRIMARY KEY AUTO_INCREMENT,
    food_name VARCHAR(255) NOT NULL,
    price DECIMAL(15,2) NOT NULL CHECK (price > 0),
    category ENUM('FOOD','DRINK'),
    stock INT NOT NULL CHECK (stock >= 0)
    );


CREATE TABLE IF NOT EXISTS Tables (
    table_id INT PRIMARY KEY AUTO_INCREMENT,
    number_seats INT NOT NULL CHECK (number_seats >= 0),
    status ENUM('FREE','OCCUPIED','RESERVED')
    );

-- bảng orders
CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    table_id INT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL CHECK (total_amount >= 0),
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('PENDING', 'PAID', 'CANCEL') DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES Account(account_id),
    FOREIGN KEY (table_id) REFERENCES Tables(table_id)
    );

-- bảng Order_Details
CREATE TABLE IF NOT EXISTS Order_Details (
   id INT PRIMARY KEY AUTO_INCREMENT,
   order_id INT NOT NULL,
   food_id INT NOT NULL,
   quantity INT CHECK (quantity >= 0),
   unit_price DECIMAL(15,2) NOT NULL CHECK (unit_price >= 0),
   status ENUM('PENDING', 'COOKING', 'READY','SERVED') DEFAULT 'PENDING',
   FOREIGN KEY (food_id) REFERENCES Menu_items(food_id),
   FOREIGN KEY (order_id) REFERENCES Orders(order_id)
    );
    
    
    
    
    