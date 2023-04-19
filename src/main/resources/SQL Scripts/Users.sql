DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS(
                      USERNAME varchar(20) NOT NULL,
                      PASSWORD varchar(20) NOT NULL,
                      PERMISSIONLEVEL INTEGER NOT NULL,
                      POSITION VARCHAR(20) NOT NULL
);

INSERT INTO USERS VALUES('testAdmin','password', 0,'Doctor');
INSERT INTO USERS VALUES('testEmployee','password', 1,'Nurse');
INSERT INTO USERS VALUES('username','password', 2,'Generic NPC');