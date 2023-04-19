DROP TABLE IF EXISTS conferencerequests;
DROP TABLE IF EXISTS MEALREQUESTS;
DROP TABLE IF EXISTS REQUESTS;
CREATE TABLE requests (
                          id int NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          employee VARCHAR(255) NOT NULL,
                          floor VARCHAR(255) NOT NULL,
                          roomNumber VARCHAR(255) NOT NULL,
                          dateSubmitted DATE NOT NULL,
                          requestStatus VARCHAR(255) NOT NULL,
                          requestType VARCHAR(255) NOT NULL
);

INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('John Smith', '1', '101', '2019-01-01', 'Pending', 'Meal');
INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('Jane Doe', '2', '201', '2019-01-02', 'Pending', 'Meal');
INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('John Smither', '1', '101', '2019-01-03', 'Pending', 'Meal');
INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('Jane Doer', '2', '201', '2019-01-04', 'Pending', 'Conference');
INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('John Smithy', '1', '101', '2019-01-05', 'Pending', 'Conference');
INSERT INTO requests (employee, floor, roomNumber, dateSubmitted, requestStatus, requestType) VALUES ('Jane Doey', '2', '201', '2019-01-06', 'Pending', 'Conference');

CREATE TABLE mealrequests (
                              id int,
                              orderFrom VARCHAR(255) NOT NULL,
                              food VARCHAR(255) NOT NULL,
                              drink VARCHAR(255) NOT NULL,
                              snack VARCHAR(255) NOT NULL,
                              mealModification VARCHAR(255) NOT NULL, CONSTRAINT FK_MealRequests_Requests FOREIGN KEY (id) REFERENCES requests(id)
);

INSERT INTO mealrequests (id, orderFrom, food, drink, snack, mealModification) VALUES (1, 'Location1', 'Pizza', 'Coke', 'Fries', 'No ketchup');
INSERT INTO mealrequests (id, orderFrom, food, drink, snack, mealModification) VALUES (3, 'Location2', 'Burger', 'Sprite', 'Onion Rings', 'No');
INSERT INTO mealrequests (id, orderFrom, food, drink, snack, mealModification) VALUES (2, 'Location3', 'Salad', 'Water', 'Fries', 'No');

SELECT * FROM requests, mealrequests WHERE requests.id = mealrequests.id;

CREATE TABLE conferencerequests (
                                    id int,
                                    dateRequested DATE NOT NULL,
                                    eventName VARCHAR(255) NOT NULL,
                                    bookingReason VARCHAR(255) NOT NULL, CONSTRAINT FK_ConferenceRequests_Requests FOREIGN KEY (id) REFERENCES requests(id)
);

INSERT INTO conferencerequests (id, dateRequested, eventName, bookingReason) VALUES (4, '2019-01-04', 'Event1', 'Reason1');
INSERT INTO conferencerequests (id, dateRequested, eventName, bookingReason) VALUES (5, '2019-01-05', 'Event2', 'Reason2');
INSERT INTO conferencerequests (id, dateRequested, eventName, bookingReason) VALUES (6, '2019-01-06', 'Event3', 'Reason3');