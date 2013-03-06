-- Note that comment lines need to end with a semicolon for CreateDB.java to work;

-- The primary keys (id) should really be CHAR(40), not VARCHAR(40), but;
-- to make life easier in testing, I've placed them as VARCHAR(40) for now;

-- First drop everything (order matters here for foreign keys!);
DROP TABLE personcar;
DROP TABLE car;
DROP TABLE dog;
DROP TABLE employee;
DROP TABLE person;
DROP TABLE businessobject;



-- BUSINESSOBJECT TABLE (everything extends this);
CREATE TABLE businessobject (
  id           VARCHAR(40) PRIMARY KEY,
  botype       VARCHAR(250)
);	




-- PERSON TABLE;
CREATE TABLE person (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  firstname    VARCHAR(250),
  lastname     VARCHAR(250),
  phone        VARCHAR(100)
);
INSERT INTO businessobject(id, botype) VALUES ('person1', 'edu.byu.isys413.fbrooke.Person');
INSERT INTO person(id, firstname, lastname, phone) VALUES ('person1', 'Lisa', 'Simpson', '801-555-1234');
INSERT INTO businessobject(id, botype) VALUES ('person2', 'edu.byu.isys413.fbrooke.Person');
INSERT INTO person(id, firstname, lastname, phone) VALUES ('person2', 'Master', 'Chief', '801-555-4321');



-- EMPLOYEE TABLE (extends PERSON table);
CREATE TABLE employee (
  id             VARCHAR(40) PRIMARY KEY REFERENCES person(id),
  username       VARCHAR(250),
  password       VARCHAR(250),
  birthdate      DATE,
  salary         NUMERIC(8,2),
  favoritenumber INT,
  iq             INT,
  distance       INT
);
INSERT INTO businessobject(id, botype) VALUES ('employee1', 'edu.byu.isys413.fbrooke.Employee');
INSERT INTO person(id, firstname, lastname, phone) VALUES ('employee1', 'Bart', 'Simpson', '801-555-0222');
INSERT INTO employee(id, username, password) VALUES ('employee1', 'bartsipmson', 'DontHaveACowMan');
INSERT INTO businessobject(id, botype) VALUES ('employee2', 'edu.byu.isys413.fbrooke.Employee');
INSERT INTO person(id, firstname, lastname, phone) VALUES ('employee2', 'Homer', 'Simpson', '801-555-3456');
INSERT INTO employee(id, username, password) VALUES ('employee2', 'homersimpson', 'Doh!');




-- DOG TABLE;
CREATE TABLE dog (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  personid     VARCHAR(40) REFERENCES person(id),
  dogname      VARCHAR(250),
  breed        VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('dog1', 'edu.byu.isys413.fbrooke.Dog');
INSERT INTO dog(id, personid, dogname, breed) VALUES ('dog1', 'person1', 'Fluffy', 'Great Dane');
INSERT INTO businessobject(id, botype) VALUES ('dog2', 'edu.byu.isys413.fbrooke.Dog');
INSERT INTO dog(id, personid, dogname, breed) VALUES ('dog2', 'person1', 'Dasher', 'Beagle');
INSERT INTO businessobject(id, botype) VALUES ('dog3', 'edu.byu.isys413.fbrooke.Dog');
INSERT INTO dog(id, personid, dogname, breed) VALUES ('dog3', 'employee1', 'Nacho',  'Bloodhound');
INSERT INTO businessobject(id, botype) VALUES ('dog4', 'edu.byu.isys413.fbrooke.Dog');
INSERT INTO dog(id, personid, dogname, breed) VALUES ('dog4', 'employee2', 'T-Bone', 'Terrier');



-- CAR TABLE;
CREATE TABLE car (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  brand        VARCHAR(250),
  model        VARCHAR(250)
);
INSERT INTO businessobject(id, botype) VALUES ('car1', 'edu.byu.isys413.fbrooke.Car');
INSERT INTO car(id, brand, model) VALUES ('car1', 'Honda', 'Accord');
INSERT INTO businessobject(id, botype) VALUES ('car2', 'edu.byu.isys413.fbrooke.Car');
INSERT INTO car(id, brand, model) VALUES ('car2', 'Dodge', 'Ram');
INSERT INTO businessobject(id, botype) VALUES ('car3', 'edu.byu.isys413.fbrooke.Car');
INSERT INTO car(id, brand, model) VALUES ('car3', 'Mazda', 'Miata');




-- PERSONCAR TABLE (many-to-many table that links owners to cars);
CREATE TABLE personcar (
  id           VARCHAR(40) PRIMARY KEY REFERENCES businessobject(id),
  carid        VARCHAR(40) REFERENCES car(id),
  ownerid      VARCHAR(40) REFERENCES person(id)
);
INSERT INTO businessobject(id, botype) VALUES ('personcar1', 'edu.byu.isys413.fbrooke.PersonCar');
INSERT INTO personcar(id, carid, ownerid) VALUES ('personcar1', 'car1', 'person1');
INSERT INTO businessobject(id, botype) VALUES ('personcar2', 'edu.byu.isys413.fbrooke.PersonCar');
INSERT INTO personcar(id, carid, ownerid) VALUES ('personcar2', 'car1', 'person2');
INSERT INTO businessobject(id, botype) VALUES ('personcar3', 'edu.byu.isys413.fbrooke.PersonCar');
INSERT INTO personcar(id, carid, ownerid) VALUES ('personcar3', 'car2', 'person2');
INSERT INTO businessobject(id, botype) VALUES ('personcar4', 'edu.byu.isys413.fbrooke.PersonCar');
INSERT INTO personcar(id, carid, ownerid) VALUES ('personcar4', 'car3', 'person2');
