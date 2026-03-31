DROP DATABASE restaurant_manager;
CREATE DATABASE restaurant_manager;
USE restaurant_manager;

-- ================== ACCOUNT ==================
CREATE TABLE Account
(
    account_id   INT AUTO_INCREMENT PRIMARY KEY,
    account_name VARCHAR(100)                       NOT NULL UNIQUE,
    password     VARCHAR(200)                       NOT NULL,
    isban        BOOLEAN DEFAULT FALSE,
    role         ENUM ('CUSTOMER','CHEF','MANAGER') NOT NULL
);

-- ================== MENU ==================
CREATE TABLE Menu_items
(
    food_id   INT PRIMARY KEY AUTO_INCREMENT,
    food_name VARCHAR(255)          NOT NULL,
    price     DECIMAL(15, 2)        NOT NULL CHECK (price > 0),
    category  ENUM ('FOOD','DRINK') NOT NULL,
    stock     INT                   NOT NULL CHECK (stock >= 0)
);

-- ================== TABLE ==================
CREATE TABLE Tables
(
    table_id     INT PRIMARY KEY AUTO_INCREMENT,
    number_seats INT NOT NULL CHECK (number_seats > 0),
    status       ENUM ('FREE','OCCUPIED','RESERVED','DAMAGED')
                     NOT NULL DEFAULT 'FREE'
);

-- ================== ORDERS ==================
CREATE TABLE Orders
(
    order_id     INT PRIMARY KEY AUTO_INCREMENT,
    user_id      INT            NOT NULL,
    table_id     INT            NOT NULL,
    total_amount DECIMAL(15, 2) NOT NULL            DEFAULT 0 CHECK (total_amount >= 0),
    order_date   DATETIME                           DEFAULT CURRENT_TIMESTAMP,
    status       ENUM ('PENDING', 'PAID', 'CANCEL') DEFAULT 'PENDING',

    FOREIGN KEY (user_id) REFERENCES Account (account_id),
    FOREIGN KEY (table_id) REFERENCES Tables (table_id)
);

-- ================== ORDER DETAILS (FIX CHÍNH) ==================
CREATE TABLE IF NOT EXISTS Order_Details
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    order_id   INT            NOT NULL,
    food_id    INT            NOT NULL,
    quantity   INT            NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(15, 2) NOT NULL CHECK (unit_price >= 0),
    status     ENUM ('WAITING_APPROVAL', 'PENDING', 'COOKING', 'READY','SERVED','CANCEL')
                              NOT NULL DEFAULT 'WAITING_APPROVAL',

    FOREIGN KEY (food_id) REFERENCES Menu_items (food_id),
    FOREIGN KEY (order_id) REFERENCES Orders (order_id) ON DELETE CASCADE
);

-- ================== FEEDBACK ==================
CREATE TABLE reviews
(
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    account_id  INT NOT NULL,
    order_id    INT NOT NULL,
    rate        INT CHECK (rate BETWEEN 1 AND 5),
    comment     TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (account_id) REFERENCES Account (account_id),
    FOREIGN KEY (order_id) REFERENCES Orders (order_id),


    UNIQUE (account_id, order_id)
);

