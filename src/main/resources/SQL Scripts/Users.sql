DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS(
                      NAME VARCHAR(20) NOT NULL,
                      USERNAME varchar(20) NOT NULL,
                      PASSWORD varchar(20) NOT NULL,
                      EMAIL VARCHAR(20) NOT NULL,
                      PERMISSIONLEVEL INTEGER NOT NULL

);

INSERT INTO USERS VALUES('Admin', 'admin', 'admin', 'sdholmes@wpi.edu', 0);
INSERT INTO USERS VALUES('Staff', 'staff', 'staff', 'sdholmes@wpi.edu', 1);
INSERT INTO USERS VALUES('Samara Holmes', 'holmes1000', 'password', 'sdholmes@wpi.edu', 1);