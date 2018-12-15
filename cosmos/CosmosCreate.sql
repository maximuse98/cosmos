CREATE DATABASE cosmos;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE Client(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    surname VARCHAR(50),
    phone VARCHAR(12),
    phone2 VARCHAR(12),
    adress VARCHAR(200),
    email VARCHAR(50)
    );

CREATE TABLE Client_Archive(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    surname VARCHAR(50),
    phone VARCHAR(50),
	adress VARCHAR(200),
    email VARCHAR(50)
    );
    
CREATE TABLE Client_Request(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    client_id INT,
    FOREIGN KEY (client_id) REFERENCES Client(id),
    request VARCHAR(200),
    checked BOOLEAN,
    approved BOOLEAN
    );
    
CREATE TABLE Client_Order(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	client_id INT,
    FOREIGN KEY (client_id) REFERENCES Client(id),
    request VARCHAR(200),
    contract VARCHAR(200),
	payment BOOLEAN
    );

CREATE TABLE Product(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(200),
    category VARCHAR(50) NOT NULL,
    count INT
    );
    
CREATE TABLE Order_Product(
	order_id INT,
	product_id INT,
	FOREIGN KEY (order_id) REFERENCES Client_Order(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
    );
    
CREATE TABLE Invoice(
	id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	order_id INT,
    FOREIGN KEY (order_id) REFERENCES Client_Order(id),
    agreed BOOLEAN
    );

CREATE TABLE Invoice_Product(
	invoice_id INT,
    product_id INT,
	FOREIGN KEY (invoice_id) REFERENCES Invoice(id),
    FOREIGN KEY (product_id) REFERENCES Product(id),
    count INT,
	loaded BOOLEAN
    );

CREATE TABLE Request_Product(
	request_id INT,
    product_id INT,
    FOREIGN KEY (request_id) REFERENCES Client_Request(id),
    FOREIGN KEY (product_id) REFERENCES Product(id)
	);

CREATE VIEW Statement_Invoice AS
SELECT Client.adress, Product.name, Invoice_Product.count, Invoice_Product.loaded
FROM (((Invoice INNER JOIN Client_Order ON Invoice.order_id = Client_Order.id) 
	INNER JOIN Client ON Client_Order.client_id = Client.id)
	INNER JOIN Invoice_Product ON Invoice_Product.invoice_id = Invoice.id)
    INNER JOIN Product ON Invoice_Product.product_id = Product.id
WHERE loaded = FALSE && agreed = TRUE;

CREATE VIEW Client_Offer AS
SELECT Client.name AS Client_name, Client.surname, Client_Request.request, Client.email, Product.name AS Product
FROM ((Client INNER JOIN Client_Request ON Client.id = Client_Request.client_id)
	INNER JOIN Request_Product ON Request_Product.request_id = Client_Request.id)
    INNER JOIN Product ON Product.id = Request_Product.product_id;

CREATE TRIGGER delete_Client
AFTER DELETE 
	ON Client
    FOR EACH ROW
INSERT INTO Client_Archive(id, name, surname, hone, adress, email)
VALUES (OLD.name, OLD.surname, OLD.phone, OLD.adress, OLD.email);

CREATE TRIGGER update_Products
AFTER UPDATE
	ON Invoice_Product 
    FOR EACH ROW 
UPDATE Product
SET count = IF(NEW.loaded = TRUE,count - NEW.count,count)
WHERE NEW.product_id = Product.id;

delimiter 
|
CREATE TRIGGER create_Order
AFTER UPDATE 
	ON CLient_Request 
	FOR EACH ROW 
BEGIN
	INSERT INTO Client_Order
	SELECT client_id,request FROM NEW
	WHERE checked = TRUE AND approved = TRUE;
    
    SET @lastID := LAST_INSERT_ID();
    
	INSERT INTO Order_Product
	SELECT @lastID, Request_Product.product_id FROM NEW INNER JOIN Request_Product ON NEW.id = Request_Product.request_id
	WHERE checked = TRUE && approved = TRUE;
END;
|
delimiter ;




    
    