drop database if exists restaurant_manager;
create database restaurant_manager;
use restaurant_manager;

-- ================== account ==================
create table account
(
    account_id   int auto_increment primary key,
    account_name varchar(100)                       not null unique,
    password     varchar(200)                       not null,
    isban        boolean default false,
    role         enum ('customer','chef','manager') not null
);

-- ================== menu ==================
create table menu_items
(
    food_id   int primary key auto_increment,
    food_name varchar(255)          not null,
    price     decimal(15, 2)        not null check (price > 0),
    category  enum ('food','drink') not null,
    stock     int                   not null check (stock >= 0)
);

-- ================== table ==================
create table tables
(
    table_id     int primary key auto_increment,
    number_seats int not null check (number_seats > 0),
    status       enum ('free','occupied','reserved','damaged')
                     not null default 'free'
);

-- ================== orders ==================
create table orders
(
    order_id     int primary key auto_increment,
    user_id      int            not null,
    table_id     int            not null,
    total_amount decimal(15, 2) not null            default 0 check (total_amount >= 0),
    order_date   datetime                           default current_timestamp,
    status       enum ('pending', 'paid', 'cancel') default 'pending',

    foreign key (user_id) references account (account_id),
    foreign key (table_id) references tables (table_id)
);

-- ================== order details ==================
create table if not exists order_details
(
    id         int primary key auto_increment,
    order_id   int            not null,
    food_id    int            not null,
    quantity   int            not null check (quantity > 0),
    unit_price decimal(15, 2) not null check (unit_price >= 0),
    status     enum ('waiting_approval', 'pending', 'cooking', 'ready','served','cancel')
                              not null default 'waiting_approval',

    foreign key (food_id) references menu_items (food_id),
    foreign key (order_id) references orders (order_id) on delete cascade
);
