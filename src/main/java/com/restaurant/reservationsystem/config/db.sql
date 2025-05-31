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
	date date,
	quantity int,
	foreign key (product_id) references Product(product_id),
	foreign key (customer_id) references Customer(customer_id)
)

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


