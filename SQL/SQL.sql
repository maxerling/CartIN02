DROP PROCEDURE IF EXISTS addToCart;

DELIMITER //
CREATE PROCEDURE addToCart(
IN _customerid INT, 
IN _invoiceid INT,
IN _productid INT ,
OUT resultt VARCHAR(50)
)
BEGIN
	DECLARE invoiceCount INT DEFAULT 0;
	DECLARE productQuantityCount INT DEFAULT 0;
    DECLARE shoeInvoiceCount INT DEFAULT 0;
    DECLARE _to_street VARCHAR(50);
    DECLARE _to_zip INT;
    DECLARE _to_city VARCHAR(50);
    
   DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		ROLLBACK;
		RESIGNAL SET MESSAGE_TEXT = 'ERROR: Product wasnt added to your cart';
	END;
		
    START TRANSACTION;
    SELECT customer.street_address INTO _to_street FROM customer WHERE _customerid = customer.id;
    SELECT customer.zip INTO _to_zip FROM customer WHERE _customerid = customer.id;
    SELECT customer.city INTO _to_city FROM customer WHERE _customerid = customer.id;
    
	SELECT COUNT(*) INTO invoiceCount FROM invoice WHERE _invoiceid = invoice.id and _customerid = invoice.customerid;
    
		
		IF invoiceCount = 0 OR NULL THEN 
        
			INSERT INTO invoice(customerid,to_street,to_zip,to_city)
			VALUES (_customerid,_to_street,_to_zip,_to_city);
			
			INSERT INTO shoeinvoice(invoiceid,shoeid) 
			VALUES (last_insert_id(),_productid);
            
            UPDATE shoe
			SET shoe.quantity = quantity - 1
			WHERE _productid = shoe.id;
			
		ELSE
        SELECT COUNT(*) INTO productQuantityCount FROM shoe WHERE _productid = shoe.id AND shoe.quantity > 0;
        SELECT COUNT(*) INTO shoeInvoiceCount FROM shoeinvoice WHERE  _invoiceid = shoeinvoice.invoiceid AND _productid = shoeinvoice.shoeid;
        
        
            IF productQuantityCount = 1 and shoeInvoiceCount = 1 THEN
            
				UPDATE shoeinvoice
				SET shoeinvoice.quantity = quantity + 1
				WHERE _invoiceid = shoeinvoice.invoiceid AND _productid = shoeinvoice.shoeid;
                
				UPDATE shoe
				SET shoe.quantity = quantity - 1
				WHERE _productid = shoe.id;
            
			ELSE
				INSERT INTO shoeinvoice(invoiceid,shoeid) 
				VALUES (_invoiceid,_productid);
                
				UPDATE shoe
				SET shoe.quantity = quantity - 1
				WHERE _productid = shoe.id;
                
			END IF;
            
            
		END IF;
        
        SET resultt = "Product was added to your cart";
    -- ROLLBACK;
	 COMMIT;	
END//
DELIMITER ;

DROP TRIGGER IF EXISTS after_shoe_update;

DELIMITER //
CREATE TRIGGER after_shoe_update
AFTER UPDATE ON shoe
FOR EACH ROW 
BEGIN
    IF (NEW.quantity = 0) THEN
        INSERT INTO outOfStock(shoeID, outOfStockDate) 
        VALUES (OLD.id, CURRENT_DATE());
    END IF;

    IF (NEW.quantity > 0) THEN
            DELETE FROM outofstock WHERE OLD.id = shoeID;
        END IF;

END //
DELIMITER ;



DROP FUNCTION IF EXISTS avgScore;
	DELIMITER //
	CREATE FUNCTION avgScore(_productid INT)
	RETURNS DECIMAL(5,2	)
	DETERMINISTIC
	BEGIN
	DECLARE average DECIMAL(5,2);
    
    SELECT AVG(grade.rating) INTO average FROM shoe
    LEFT JOIN feedback ON shoe.id = feedback.shoeid
	LEFT JOIN grade ON feedback.gradeid = grade.id
    WHERE (SELECT name from shoe WHERE _productid = shoe.id) = shoe.name
	GROUP BY shoe.name;
	RETURN average;
	END//
	DELIMITER ;


DELIMITER //
CREATE PROCEDURE Rate(
IN _customerID INT,
IN _shoeID INT,
IN _gradeID INT,
IN _comment VARCHAR(100))
BEGIN

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            RESIGNAL SET MESSAGE_TEXT = 'ERROR: Your review wasnt added';
        END;


    START TRANSACTION;

        SELECT shoe.id INTO _shoeID FROM shoe WHERE _shoeID = shoe.id;
        SELECT customer.id INTO _customerID FROM customer WHERE _customerID = customer.id;
        SELECT grade.id INTO _gradeID FROM grade WHERE _gradeID = grade.id;

        INSERT INTO feedback(comment,customerid,shoeid,gradeid) 
        VALUES (_comment,_customerID,_shoeID,_gradeID);


    -- ROLLBACK;
    COMMIT;

END//
DELIMITER ;
    