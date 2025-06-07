Create table Product(
	ProductId varchar(50) primary key,
	ProductName varchar(50),
	Price float,
	Status varchar(50),
	CategoryName varchar(50),
	ManagerId int,
	foreign key (CategoryName) references Categories(CategoryName),
	foreign key (ManagerId)  references Managers(ManagerId)
)

Create table Customer(
	CustomerId int identity(1,1),
	CustomerName varchar(100),
	CustomerPhone varchar(20) primary key,
	CustomerAddress varchar(100)
)

Create table Orders(
	OrderId int identity(1,1) primary key,
	ProductId varchar (50),
	CustomerPhone varchar(20),
	Date datetime2(3),
	Quantity int,
	foreign key (ProductId) references Product(ProductId),
	foreign key (CustomerPhone) references Customer(CustomerPhone)
)

create table Invoice(
	InvoiceId int identity(1,1) primary key,
	EmployeeId int,
	CustomerPhone varchar(20),
	Total float,
	Date date,
	foreign key (CustomerPhone) references Customer(CustomerPhone),
	foreign key (EmployeeId)    references Employees(EmployeeId)
)

Create table Managers (
	ManagerId int primary key,
	ManagerName varchar(50),
	Username varchar(50),
	Password varchar(50)
)

Create table Categories(
	CategoryName varchar(50) primary key
)


Create table Employees(
	EmployeeId int primary key,
	EmployeeName varchar(50),
	EmployeePhone varchar(50),
	EmployeeAddress varchar(50),
	ManagerId int,
	foreign key (ManagerId)  references Managers(ManagerId)
)
drop table Categories
drop table Admin
drop table Product
drop table Orders
drop table Invoice
drop table Customer
DROP TRIGGER trg_check_phone_after_insert;
DROP FUNCTION fn_TinhTongTienDonHang;
DROP PROCEDURE GetOrdersByDateAndPhone
DELETE FROM Orders;
DELETE FROM Invoice;
DELETE FROM Customer;
DELETE FROM Categories
DELETE FROM Product


CREATE TRIGGER trg_check_phone_after_insert
ON Customer
AFTER INSERT
AS
BEGIN
    IF EXISTS (
        SELECT 1
        FROM inserted
        WHERE LEN(CustomerPhone) != 10
           OR PATINDEX('%[^0-9]%', CustomerPhone) > 0
           OR LEFT(CustomerPhone, 1) != '0'
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

    SELECT @TotalPrice = SUM(o.Quantity * p.Price)
    FROM Orders o
    JOIN Product p ON o.ProductId = p.ProductId
    WHERE o.CustomerPhone = @Phone AND o.Date = @Date;

    RETURN ISNULL(@TotalPrice, 0);  --đảm bảo nếu không có kết quả thì trả về 0 thay vì NULL.
END;




CREATE PROCEDURE GetOrdersByDateAndPhone
    @in_date datetime2(3),
    @in_phone VARCHAR(20)
AS
BEGIN
    SELECT o.OrderId, o.ProductId, p.ProductName, c.CategoryName, p.Price, o.Quantity
    FROM Orders o
    JOIN Product p ON o.ProductId = p.ProductId
	JOIN Categories c ON c.CategoryName= p.CategoryName
    WHERE o.Date = @in_date AND o.CustomerPhone = @in_phone;
END;



INSERT INTO Managers VALUES (1,'Tien','Tien','123')
INSERT INTO Managers VALUES (2,'Thinh','Thinh','123')
INSERT INTO Categories VALUES ('Drink')
INSERT INTO Categories VALUES ('Food')
-- Sản phẩm thuộc danh mục cake
INSERT INTO Product (ProductId, ProductName, Price, Status, CategoryName, ManagerId)
VALUES 
('F001', 'Chocolate Cake', 15.99, 'Available', 'Food', 1),
('F002', 'Vanilla Cake', 13.49, 'Available', 'Food', 1),
('F003', 'Strawberry Cake', 14.25, 'Unavailable', 'Food', 1),
('F004', 'Cheesecake', 17.50, 'Available', 'Food', 1),
('F005', 'Red Velvet Cake', 16.00, 'Unavailable', 'Food', 1),
('F006', 'Lemon Tart', 12.75, 'Available', 'Food', 1);

-- Sản phẩm thuộc danh mục drink
INSERT INTO Product (ProductId, ProductName, Price, Status, CategoryName, ManagerId)
VALUES 
('D001', 'Green Tea', 2.99, 'Available', 'Drink', 1),
('D002', 'Black Coffee', 3.49, 'Available', 'Drink', 1),
('D003', 'Orange Juice', 3.99, 'Unavailable', 'Drink', 1),
('D004', 'Latte', 3.99, 'Available', 'Drink', 1),
('D005', 'Espresso', 2.49, 'Unavailable', 'Drink', 1),
('D006', 'Iced Tea', 3.25, 'Available', 'Drink', 1);

