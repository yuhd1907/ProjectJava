-- create database PhoneStore_DB;
-- create schema storedb;

-- 1. Tạo bảng Admin (quản trị viên)
create table Admin
(
    id       serial primary key,
    username varchar(50)  not null unique,
    password varchar(255) not null
);

-- 2. Tạo bảng  Product(Sản phẩm)
create table Product
(
    id    serial primary key,
    name  varchar(100)   not null,
    brand varchar(50)    not null,
    price decimal(10, 2) not null,
    stock int            not null
);

-- 3. Tạo bảng customer(Khách hàng)
create table Customer
(
    id      serial primary key,
    name    varchar(100) not null,
    phone   varchar(20),
    email   varchar(100) unique,
    address varchar(255)
);

-- 4. Tạo bảng invoice (hóa đơn)
create table Invoice
(
    id           serial primary key,
    customer_id  int references Customer (id),
    created_at   timestamp default current_timestamp,
    total_amount decimal(10, 2) not null
);

-- 5. Tạo bảng invoice_details ( chi tiết hóa đơn)
create table invoice_details
(
    id         serial primary key,
    invoice_id int references Invoice (id),
    product_id int references Product (id),
    quantity   int            not null,
    unit_price decimal(10, 2) not null,
    subtotal   decimal(10, 2) not null
);
