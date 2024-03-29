USE EventManager;
GO

-- Drop

IF OBJECT_ID('dbo.Ticket', 'U') IS NOT NULL
    DROP TABLE dbo.Ticket;
GO

IF OBJECT_ID('dbo.TicketCategory', 'U') IS NOT NULL
    DROP TABLE dbo.TicketCategory;
GO

IF OBJECT_ID('dbo.GeneratedTicket', 'U') IS NOT NULL
    DROP TABLE dbo.GeneratedTicket;
GO

IF OBJECT_ID('dbo.TicketEventAssociation', 'U') IS NOT NULL
    DROP TABLE dbo.TicketEventAssociation;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
GO

CREATE TABLE Event (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    imageName NVARCHAR(255),
    location NVARCHAR(255),
    startDate DATE NOT NULL,
    endDate DATE,
    startTime TIME NOT NULL,
    endTime TIME,
    locationGuidance NVARCHAR(255),
    extraInfo NVARCHAR(255)
);
GO

INSERT INTO Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('International Food Festival', 'card1.jpg', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', '', ''),
    ('Vegan Cooking Workshop', 'card2.jpg', '6700, Esbjerg', '2024-05-10', '2024-05-10', '09:00', '12:00', '', ''),
    ('Farm to Table Dinner', 'card3.jpg', '6700, Esbjerg', '2024-06-15', '2024-06-17', '18:00', NULL, '', ''),
    ('Wine and Cheese Night', 'card4.jpg', '6700, Esbjerg', '2024-07-20', '2024-07-20', '17:00', '21:00', '', ''),
    ('Italian Pasta Making Class', 'card5.jpg', '6700, Esbjerg', '2024-08-25', '2024-08-25', '11:00', '14:00', '', ''),
    ('French Cuisine Tasting', 'card6.jpg', '6700, Esbjerg', '2024-09-30', NULL, '16:00', '21:00', '', ''),
    ('Sushi Rolling Workshop', 'card7.jpg', '6700, Esbjerg', '2024-10-05', '2024-10-05', '12:00', '15:00', '', ''),
    ('Chocolate Making Class', 'card8.jpg', '6700, Esbjerg', '2024-11-10', NULL, '10:00', NULL, '', ''),
    ('BBQ and Grill Cook-off', 'card9.jpg', '6700, Esbjerg', '2024-12-15', '2024-12-15', '11:00', '18:00', '', ''),
    ('Farmers Market Tour', 'card10.jpg', '6700, Esbjerg', '2024-01-20', '2024-01-21', '09:00', NULL, '', ''),
    ('Pastry and Baking Workshop', 'card11.jpg', '6700, Esbjerg', '2024-02-25', '2024-02-25', '10:00', '13:00', '', ''),
    ('Coffee Tasting Experience', 'card12.jpg', '6700, Esbjerg', '2024-03-02', NULL, '09:00', '17:00', '', ''),
    ('Beer Brewing Demonstration', 'card13.jpg', '6700, Esbjerg', '2024-04-07', '2024-04-07', '14:00', '17:00', '', ''),
    ('Gourmet Burger Festival', 'card14.jpg', '6700, Esbjerg', '2024-05-12', '2024-05-12', '12:00', '20:00', '', ''),
    ('Mexican Fiesta Night', 'card15.jpg', '6700, Esbjerg', '2024-06-17', NULL, '17:00', NULL, '', ''),
    ('Street Food Extravaganza', 'card16.jpg', '6700, Esbjerg', '2024-07-22', '2024-07-22', '16:00', '23:00', '', ''),
    ('Ice Cream Social', 'card17.jpg', '6700, Esbjerg', '2024-08-27', NULL, '13:00', NULL, '', ''),
    ('Pizza Making Party', 'card18.jpg', '6700, Esbjerg', '2024-09-01', '2024-09-01', '11:00', '14:00', '', ''),
    ('Seafood Feast', 'card19.jpg', '6700, Esbjerg', '2024-10-06', '2024-10-06', '18:00', '21:00', '', ''),
    ('Culinary Arts Festival', 'card20.jpg', '6700, Esbjerg', '2024-11-11', NULL, '10:00', NULL, '', '');
GO

IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL
    DROP TABLE dbo.Users;
GO

create table dbo.Users
(
    id       int identity (0, 1),
    username varchar(50),
    password varchar(200)
);

-- Password: test
INSERT INTO dbo.Users (username, password)
VALUES
    ('test', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy')
GO

CREATE TABLE Ticket (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255),
    classification NVARCHAR(50) CHECK (classification IN ('PAID', 'PROMOTIONAL')),
);

CREATE TABLE TicketEvent (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ticketId INT,
    eventId INT NULL,
    price DECIMAL(10, 2) NULL,
    quantity INT,
    FOREIGN KEY (ticketId) REFERENCES Ticket(id),
    FOREIGN KEY (eventId) REFERENCES Event(id)
);

CREATE TABLE TicketGenerated (
     id INT IDENTITY(1,1) PRIMARY KEY,
     ticketEventAssociationId INT,
     customerId INT NULL,
     assigned BIT DEFAULT 0,
     used BIT DEFAULT 0,
     barcode NVARCHAR(255),
     qrcode NVARCHAR(255),
     FOREIGN KEY (ticketEventAssociationId) REFERENCES TicketEvent(id),
     -- FOREIGN KEY (customerId) REFERENCES Customer(id)
);


INSERT INTO Ticket (title, classification) VALUES ('Rock Band Live', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Shakespeare Play', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Football Match', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Tech Conference 2024', 'PROMOTIONAL');
INSERT INTO Ticket (title, classification) VALUES ('Jazz Night', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Broadway Musical', 'PAID');