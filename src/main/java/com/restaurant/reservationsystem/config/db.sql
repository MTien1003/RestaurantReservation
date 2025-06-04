Create table Product(
	product_id varchar(50) primary key,
	product_name varchar(50),
	type varchar(50),
	price float,
	status varchar(50)
)

Create table Customer(
	customer_id int identity(1,1),
	customer_name varchar(100),
	phone varchar(20) primary key,
	address varchar(100)
)

Create table Orders(
	order_id int identity(1,1) primary key,
	product_id varchar (50),
	phone varchar(20),
	date datetime2(3),
	quantity int,
	foreign key (product_id) references Product(product_id),
	foreign key (phone) references Customer(phone)
)

create table Invoice(
	invoice_id int identity(1,1) primary key,
	phone varchar(20),
	total float,
	date date,
	foreign key (phone) references Customer(phone)
)

Create table Admin (
	id int primary key,
	username varchar(50),
	password varchar(50)
)

drop table Orders
drop table Invoice
drop table Customer

DELETE FROM Orders;
DELETE FROM Invoice;
DELETE FROM Customer;


CREATE TRIGGER trg_check_phone_after_insert
ON Customer
AFTER INSERT
AS
BEGIN
    IF EXISTS (
        SELECT 1
        FROM inserted
        WHERE LEN(phone) != 10
           OR PATINDEX('%[^0-9]%', phone) > 0
           OR LEFT(phone, 1) != '0'
    )
    BEGIN
        RAISERROR('Số điện thoại không hợp lệ! Phải có đúng 10 chữ số và bắt đầu bằng số 0.', 16, 1);
        ROLLBACK TRANSACTION;
    END
END;


CREATE FUNCTION fn_TinhTongTienDonHang (
    @Phone NVARCHAR(20),
    @Date datetime2(3)
)
RETURNS DECIMAL(18, 2)
AS
BEGIN
    DECLARE @TotalPrice DECIMAL(18, 2);

    SELECT @TotalPrice = SUM(o.quantity * p.price)
    FROM Orders o
    JOIN Product p ON o.product_id = p.product_id
    WHERE o.phone = @Phone AND o.date = @Date;

    RETURN ISNULL(@TotalPrice, 0);  --đảm bảo nếu không có kết quả thì trả về 0 thay vì NULL.
END;



CREATE PROCEDURE GetOrdersByDateAndPhone
    @in_date datetime2(3),
    @in_phone VARCHAR(20)
AS
BEGIN
    SELECT o.order_id, o.product_id, p.product_name, p.type, p.price, o.quantity
    FROM Orders o
    JOIN Product p ON o.product_id = p.product_id
    WHERE o.date = @in_date AND o.phone = @in_phone;
END;




