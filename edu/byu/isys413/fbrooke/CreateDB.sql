-- Note that comment lines need to end with a semicolon for CreateDB.java to work;

-- The primary keys (id) should really be CHAR(40), not VARCHAR(40), but;
-- to make life easier in testing, I've placed them as VARCHAR(40) for now;

-- First drop everything (order matters here for foreign keys!);


DROP TABLE IF EXISTS computer;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS generalledger;
DROP TABLE IF EXISTS physicalprod;
DROP TABLE IF EXISTS sale;
--DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS revenuesource;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS creditdebit;
DROP TABLE IF EXISTS journalentry;

DROP TABLE IF EXISTS storeproduct;
DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS store;
DROP TABLE IF EXISTS cproduct;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS commission;

DROP TABLE IF EXISTS businessobject;


-- BUSINESSOBJECT TABLE (everything extends this);
CREATE TABLE businessobject (
  id           VARCHAR(40) PRIMARY KEY,
  botype       VARCHAR(250)
);	




-- PERSON TABLE;
CREATE TABLE store (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  location    VARCHAR(250),
  managerid     VARCHAR(40) REFERENCES employee(id),
  address	VARCHAR(250),
  state		VARCHAR(250),
  city		VARCHAR(250),
  zip		VARCHAR(250),
  phone        VARCHAR(100),
  subnetid		VARCHAR(250),
  salestax		DOUBLE
);
INSERT INTO businessobject(id, botype) VALUES ('store1', 'edu.byu.isys413.fbrooke.Store');
INSERT INTO store(id, location, managerid, address, state, city, zip, phone, subnetid, salestax) VALUES ('store1', 'China', null, 'ntemple', 'utah', 'slc', '84601', '801-555-1234', '34', .067);
INSERT INTO businessobject(id, botype) VALUES ('store2', 'edu.byu.isys413.fbrooke.Store');
INSERT INTO store(id, location, managerid, address, state, city, zip, phone, subnetid, salestax) VALUES ('store2', 'Europe', null, 'stemple', 'utah2', 'june', '34433', '801-555-4321', '192.168.4.1', .55);



-- EMPLOYEE TABLE (extends PERSON table);
CREATE TABLE employee (
  id             VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  username       VARCHAR(250),
  name			VARCHAR(250),
  phone		     VARCHAR(100),
  salary         NUMERIC(8,2),
  hiredate      DATE,
  favoritenumber INT,
  commrate		DOUBLE,
  storeid		VARCHAR(40) REFERENCES store(id)
);
INSERT INTO businessobject(id, botype) VALUES ('employee1', 'edu.byu.isys413.fbrooke.Employee');
INSERT INTO employee(id, username, name, phone, salary, hiredate, favoritenumber, commrate, storeid) VALUES ('employee1', 'fbrooke', 'Lisa bunny Simpson', '801-555-1234', 334335, '2005-12-05 14:02:41.910', 36, .50, 'store1');
INSERT INTO businessobject(id, botype) VALUES ('employee2', 'edu.byu.isys413.fbrooke.Employee');
INSERT INTO employee(id, username, name, phone, salary, hiredate, favoritenumber, commrate, storeid) VALUES ('employee2', 'homersimpson', 'Master the Chief', '801-555-4321', 567654, '2003-11-05 14:02:41.910', 22, .30,'store2');

UPDATE store SET managerid='employee1' WHERE id='store1';
UPDATE store SET managerid='employee2' WHERE id='store2';


-- CUSTOMER TABLE;
CREATE TABLE customer (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  name      VARCHAR(250),
  phone        VARCHAR(250),
  email			VARCHAR(250),
  address		VARCHAR(250),
  membership	VARCHAR(250),
  backup		VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('cust1', 'edu.byu.isys413.fbrooke.Customer');
INSERT INTO customer(id, name, phone, email, address) VALUES ('cust1', 'Joe Josh', '8018312222', 'iamcute@gmail.com', '569 sesame street');
INSERT INTO businessobject(id, botype) VALUES ('cust2', 'edu.byu.isys413.fbrooke.Customer');
INSERT INTO customer(id, name, phone, email, address) VALUES ('cust2', 'Abe Apple', '5555555555', 'sugarisgood@gmail.com', 'Jurry Lane');
INSERT INTO businessobject(id, botype) VALUES ('cust3', 'edu.byu.isys413.fbrooke.Customer');
INSERT INTO customer(id, name, phone, email, address) VALUES ('cust3', 'Shawn Seamore', '294863902', 'okletsgo@gmail.com', 'Diagon Alley');
INSERT INTO businessobject(id, botype) VALUES ('cust4', 'edu.byu.isys413.fbrooke.Customer');
INSERT INTO customer(id, name, phone, email, address) VALUES ('cust4', 'Brandon Buttersworth', '5683939283', 'butter@gmail.com', 'Japan');

-- PRODUCT TABLE;
CREATE TABLE product (
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  price		DOUBLE,
  prodtype	VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('product1', 'edu.byu.isys413.fbrooke.PhysicalProd');
INSERT INTO product(id, price, prodtype) VALUES ('product1', 2.55, 'Physical');
INSERT INTO businessobject(id, botype) VALUES ('product2', 'edu.byu.isys413.fbrooke.PhysicalProd');
INSERT INTO product(id, price, prodtype) VALUES ('product2', 30.20, 'Physical');
INSERT INTO businessobject(id, botype) VALUES ('product3', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO product(id, price, prodtype) VALUES ('product3', 1000.10, 'Conceptual');
INSERT INTO businessobject(id, botype) VALUES ('product4', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO product(id, price, prodtype) VALUES ('product4', 500, 'Conceptual');
INSERT INTO businessobject(id, botype) VALUES ('product5', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO product(id, price, prodtype) VALUES ('product5', 2.55, 'Conceptual');
INSERT INTO businessobject(id, botype) VALUES ('product6', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO product(id, price, prodtype) VALUES ('product6', 30.20, 'Conceptual');

-- CONCEPTUAL PRODUCT TABLE;
CREATE TABLE cproduct (
  id           VARCHAR(40) PRIMARY KEY REFERENCES product(id),
  cprodid		VARCHAR(40),
  name        VARCHAR(250),
  description        VARCHAR(250),
  manufacturer		VARCHAR(250),
  avgcost			VARCHAR(250),
  commrate			VARCHAR(250),
  sku				VARCHAR(250)
);
--INSERT INTO businessobject(id, botype) VALUES ('cprod1', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO cproduct(id, cprodid, name, description, manufacturer, avgcost, commrate, sku) VALUES ('product3', 'cprod1', 'mp3', 'so cool', 'sony','20','.25','g3g3');
--INSERT INTO businessobject(id, botype) VALUES ('cprod2', 'edu.byu.isys413.fbrooke.CProduct');
INSERT INTO cproduct(id, cprodid, name, description, manufacturer, avgcost, commrate, sku) VALUES ('product4', 'cprod2', 'ipod', 'Even cooler', 'apple', '30', '.25', '55j');
INSERT INTO cproduct(id, cprodid, name, description, manufacturer, avgcost, commrate, sku) VALUES ('product5', 'cprod5', 'Stereo', 'Sony S3', 'Sony', '1200', '.25', '55Ph');
INSERT INTO cproduct(id, cprodid, name, description, manufacturer, avgcost, commrate, sku) VALUES ('product6', 'cprod6', 'Camera', 'Canon 2540', 'Canon', '600', '.25', '66Ph');


-- STOREPRODUCT TABLE (many-to-many table that links stores to products);
CREATE TABLE storeproduct (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  storeid        VARCHAR(40) REFERENCES store(id),
  cprodid      VARCHAR(40) REFERENCES cproduct(id),
  quantity		INT,
  shelfloc			VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('storeprod1', 'edu.byu.isys413.fbrooke.StoreProduct');
INSERT INTO storeproduct(id, storeid, cprodid, quantity, shelfloc) VALUES ('storeprod1', 'store1', 'cprod1', 55, 'Yukon');
INSERT INTO businessobject(id, botype) VALUES ('storeprod2', 'edu.byu.isys413.fbrooke.StoreProduct');
INSERT INTO storeproduct(id, storeid, cprodid, quantity, shelfloc) VALUES ('storeprod2', 'store2', 'cprod2', 123, 'A23');


-- JOURNAL ENTRY TABLE;
CREATE TABLE journalentry (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  entrydate        DATE,
  tranid		VARCHAR(40) REFERENCES trans(id),
  posted		BOOLEAN
);
INSERT INTO businessobject(id, botype) VALUES ('journal1', 'edu.byu.isys413.fbrooke.JournalEntry');
INSERT INTO journalentry(id, entrydate, tranid, posted) VALUES ('journal1', '2013-02-16 13:05:44', null, true);
INSERT INTO businessobject(id, botype) VALUES ('journal2', 'edu.byu.isys413.fbrooke.JournalEntry');
INSERT INTO journalentry(id, entrydate, tranid, posted) VALUES ('journal2', '2013-02-16 13:05:44', null, true);

-- CREDITDEBIT;
CREATE TABLE creditdebit (
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  journalid	VARCHAR(40) REFERENCES journalentry(id),
  dorc		BOOLEAN,
  generalname	VARCHAR(250),
  amount		DOUBLE
);
INSERT INTO businessobject(id, botype) VALUES ('dc1', 'edu.byu.isys413.fbrooke.CreditDebit');
INSERT INTO creditdebit(id, journalid, dorc, generalname, amount) VALUES ('dc1', 'journal1', true, 'Cash', 255);
INSERT INTO businessobject(id, botype) VALUES ('dc2', 'edu.byu.isys413.fbrooke.CreditDebit');
INSERT INTO creditdebit(id, journalid, dorc, generalname, amount) VALUES ('dc2', 'journal1', false, 'Sales Revenue', 255);
INSERT INTO businessobject(id, botype) VALUES ('dc3', 'edu.byu.isys413.fbrooke.CreditDebit');
INSERT INTO creditdebit(id, journalid, dorc, generalname, amount) VALUES ('dc3', 'journal2', true, 'Cash', 255);
INSERT INTO businessobject(id, botype) VALUES ('dc4', 'edu.byu.isys413.fbrooke.CreditDebit');
INSERT INTO creditdebit(id, journalid, dorc, generalname, amount) VALUES ('dc4', 'journal2', false, 'Sales Revenue', 255);

-- COMMISSION TABLE;
CREATE TABLE commission (
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  empid		VARCHAR(40) REFERENCES employee(id),
  amt	DOUBLE,
  entrydate	DATE,
  tranid	VARCHAR(40) REFERENCES trans(id)
);
INSERT INTO businessobject(id, botype) VALUES ('commission1', 'edu.byu.isys413.fbrooke.Commission');
INSERT INTO commission(id, empid, amt, entrydate, tranid) VALUES ('commission1', 'employee1', 45, '2013-01-02 12:02:22.910', null);
INSERT INTO businessobject(id, botype) VALUES ('commission2', 'edu.byu.isys413.fbrooke.Commission');
INSERT INTO commission(id, empid, amt, entrydate, tranid) VALUES ('commission2', 'employee2', 500, '2013-01-02 10:02:22.910', null);


-- TRANSACTION TABLE;
CREATE TABLE transaction (
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  journalid	VARCHAR(40) REFERENCES journalentry(id),
  custid	VARCHAR(40) REFERENCES customer(id),
  storeid		VARCHAR(40) REFERENCES store(id),
  empid			VARCHAR(40) REFERENCES employee(id),
  commissionid		VARCHAR(40) REFERENCES commission(id),
  entrydate		DATE,
  subtotal		DOUBLE,
  tax			DOUBLE,
  total			DOUBLE,
  paymentid		VARCHAR(40) REFERENCES payment(id)
);
INSERT INTO businessobject(id, botype) VALUES ('transaction1', 'edu.byu.isys413.fbrooke.Transaction');
INSERT INTO transaction(id, journalid, custid, storeid, empid, commissionid, entrydate, subtotal, tax, total, paymentid) VALUES ('transaction1', 'journal2', 'cust1', 'store1', 'employee2', 'commission2', '2013-02-02 04:13:22.210', 45, 2, 47, null);
INSERT INTO businessobject(id, botype) VALUES ('transaction2', 'edu.byu.isys413.fbrooke.Transaction');
INSERT INTO transaction(id, journalid, custid, storeid, empid, commissionid, entrydate, subtotal, tax, total, paymentid) VALUES ('transaction2', 'journal1', 'cust2', 'store2', 'employee1', 'commission1', '2013-03-01 04:13:22.210', 22, 1, 23, null);

UPDATE journalentry SET tranid='transaction1' WHERE id='journal1';
UPDATE journalentry SET tranid='transaction2' WHERE id='journal2';
UPDATE commission SET tranid='transaction1' WHERE id='commission1';
UPDATE commission SET tranid='transaction2' WHERE id='commission2';

CREATE TABLE revenuesource (
  id			VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  chargeamt		DOUBLE,
  rstype		VARCHAR(250),
  tranid		VARCHAR(40) REFERENCES trans(id)
);
INSERT INTO businessobject(id, botype) VALUES ('revenue1', 'edu.byu.isys413.fbrooke.Sale');
INSERT INTO revenuesource(id, chargeamt, rstype, tranid) VALUES ('revenue1', 22, 'Sale', 'transaction1');
INSERT INTO businessobject(id, botype) VALUES ('revenue2', 'edu.byu.isys413.fbrooke.Sale');
INSERT INTO revenuesource(id, chargeamt, rstype, tranid) VALUES ('revenue2', 25, 'Sale', 'transaction1');


--Create Sale Table;
CREATE TABLE sale (
  id		VARCHAR(40) PRIMARY KEY REFERENCES revenuesource(id),
  saleid	VARCHAR(40),
  quantity  INT,
  productid	VARCHAR(40) REFERENCES product(id)
);
--INSERT INTO businessobject(id, botype) VALUES ('revenue1', 'edu.byu.isys413.fbrooke.Sale');
INSERT INTO sale(id, saleid, quantity, productid) VALUES ('revenue1', 'sale1', 55, 'product2');
--INSERT INTO businessobject(id, botype) VALUES ('revenue2', 'edu.byu.isys413.fbrooke.Sale');
INSERT INTO sale(id, saleid, quantity, productid) VALUES ('revenue2', 'sale2', 65, 'product1');
--INSERT INTO revenuesource(id, saleid, quantitysale, prodid) VALUES ('revenue2', 'sale2', 65, 'product1');

CREATE TABLE physicalprod(
  id		VARCHAR(40) PRIMARY KEY REFERENCES product(id),
  serialnum	VARCHAR(250),
  shelfloc		VARCHAR(250),
  datepurch	DATE,
  cost			DOUBLE,
  status		VARCHAR(250),
  ppcommrate	DOUBLE,
  storeid		VARCHAR(40) REFERENCES store(id),
  cprodid		VARCHAR(40) REFERENCES cproduct(id)
);
--INSERT INTO businessobject(id, botype) VALUES ('pprod1', 'edu.byu.isys413.fbrooke.PhysicalProd');
INSERT INTO physicalprod(id, serialnum, shelfloc, datepurch, cost, status, ppcommrate, storeid, cprodid) VALUES ('product1', '66k', 'east', '2013-01-02 10:03:21.510', 2.55, 'sexy', .23, 'store1', 'product5');
--INSERT INTO businessobject(id, botype) VALUES ('pprod2', 'edu.byu.isys413.fbrooke.PhysicalProd');
INSERT INTO physicalprod(id, serialnum, shelfloc, datepurch, cost, status, ppcommrate, storeid, cprodid) VALUES ('product2', '22k', 'west', '2013-02-02 11:02:22.910', 30.20, 'notsexy', .23, 'store1', 'product6');

--Create General Ledger Table;
CREATE TABLE generalledger(
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  accname	VARCHAR(250),
  balance	DOUBLE,
  gltype	VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('general1', 'edu.byu.isys413.fbrooke.GeneralLedger');
INSERT INTO generalledger(id, accname, balance, gltype) VALUES ('general1', 'Cash', 0, 'Left');
INSERT INTO businessobject(id, botype) VALUES ('general2', 'edu.byu.isys413.fbrooke.GeneralLedger');
INSERT INTO generalledger(id, accname, balance, gltype) VALUES ('general2', 'Sales Revenue', 0, 'Right');
INSERT INTO businessobject(id, botype) VALUES ('general4', 'edu.byu.isys413.fbrooke.GeneralLedger');
INSERT INTO generalledger(id, accname, balance, gltype) VALUES ('general4', 'Comm Expense', 0, 'Left');
INSERT INTO businessobject(id, botype) VALUES ('general5', 'edu.byu.isys413.fbrooke.GeneralLedger');
INSERT INTO generalledger(id, accname, balance, gltype) VALUES ('general5', 'Tax Expense', 0, 'Right');
INSERT INTO businessobject(id, botype) VALUES ('general6', 'edu.byu.isys413.fbrooke.GeneralLedger');
INSERT INTO generalledger(id, accname, balance, gltype) VALUES ('general6', 'Comm Payable', 0, 'Right');

--Create Payment Table;
CREATE TABLE payment(
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  amount	DOUBLE,
  changeamt	 DOUBLE,
  paytype		VARCHAR(250),
  tranid	VARCHAR(40) REFERENCES transaction(id)
);
INSERT INTO businessobject(id, botype) VALUES ('payment1', 'edu.byu.isys413.fbrooke.Payment');
INSERT INTO payment(id, amount, changeamt, paytype, tranid) VALUES ('payment1', 500, 0, 'Cash', 'transaction1');
INSERT INTO businessobject(id, botype) VALUES ('payment2', 'edu.byu.isys413.fbrooke.Payment');
INSERT INTO payment(id, amount, changeamt, paytype, tranid) VALUES ('payment2', 200, 0, 'Cash', 'transaction2');
UPDATE transaction SET paymentid='payment1' WHERE id='transaction1';
UPDATE transaction SET paymentid='payment2' WHERE id='transaction2';

CREATE TABLE computer(
  id		VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  macaddress VARCHAR(250),
  storeid	VARCHAR(40) REFERENCES store(id)
);
INSERT INTO businessobject(id, botype) VALUES ('computer1', 'edu.byu.isys413.fbrooke.Computer');
INSERT INTO computer(id, macaddress, storeid) VALUES ('computer1','00-23-12-05-2E-19', 'store1');
