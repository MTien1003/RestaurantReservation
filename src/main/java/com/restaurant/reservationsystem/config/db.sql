Create table Product(
	product_id varchar(50) primary key,
	product_name varchar(50),
	type varchar(50),
	price float,
	status varchar(50)
)

Create table Customer(
	customer_id int primary key,
	customer_name varchar(100),
	phone varchar(20),
	address varchar(100)
)

Create table Orders(
	order_id int identity(1,1) primary key,
	product_id varchar (50),
	customer_id int,
	date datetime,
	quantity int,
	foreign key (product_id) references Product(product_id),
	foreign key (customer_id) references Customer(customer_id)
)
drop table Orders
-- Thêm sản phẩm
INSERT INTO Product (product_id, product_name, type, price, status)
VALUES ('P001', 'Laptop HP Pavilion', 'Electronics', 850.0, 'In Stock');

-- Thêm khách hàng
INSERT INTO Customer (customer_id, customer_name, phone, address)
VALUES (101, 'Nguyen Van A', '0901234567', '123 Le Loi, Ha Noi');

-- Thêm đơn hàng
INSERT INTO Orders (product_id, customer_id, date, quantity)
VALUES ('P001', 101, GETDATE(), 2);


Create table Admin (
	id int primary key,
	username varchar(50),
	password varchar(50)
)

create table Invoice(
	invoice_id int identity(1,1) primary key,
	customer_id int,
	total float,
	date date,
	foreign key (customer_id) references Customer(customer_id)
)
INSERT INTO Admin
VALUES (1,'Manh Tien','123')

INSERT INTO Customer (customer_id)
VALUES (1)

truncate table customer
DELETE FROM Orders;
DELETE FROM Invoice;
DELETE FROM Customer;


SELECT * FROM Orders o JOIN Product p ON o.product_id=p.product_id WHERE customer_id = 1 AND date = '2025-06-02 16:08:15.070'
ALTER TABLE Orders ALTER COLUMN date datetime2(3);

