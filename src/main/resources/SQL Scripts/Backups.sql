DROP TABLE IF EXISTS movebackup;
DROP TABLE IF EXISTS locationnamebackup;
DROP TABLE IF EXISTS nodebackup;

CREATE TABLE nodebackup(
                           nodeID INT, xcoord INT, ycoord INT, floor VARCHAR(255), building varchar(255), primary key (nodeID));

CREATE TABLE locationnamebackup(
                                   longName VARCHAR(255), shortName VARCHAR(255), nodetype VARCHAR(255), primary key (longName));

CREATE TABLE movebackup (
                            nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date),
                            constraint fk_nodeID foreign key(nodeID) references nodebackup(nodeID),
                            constraint fk_longName foreign key(longName) references locationnamebackup(longName));

INSERT INTO nodebackup SELECT * FROM nodes;
INSERT INTO locationnamebackup SELECT * FROM locationNames;
INSERT INTO movebackup SELECT * FROM moves;