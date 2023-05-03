TRUNCATE TABLE movebackup;
TRUNCATE TABLE edgebackup;
TRUNCATE TABLE locationnamebackup;
TRUNCATE TABLE nodebackup;

INSERT INTO nodebackup SELECT * FROM nodes;
INSERT INTO locationnamebackup SELECT * FROM locationNames;
INSERT INTO edgebackup SELECT * FROM edges;
INSERT INTO movebackup SELECT * FROM moves;