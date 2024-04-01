USE EventManager;
GO

-- Drop order
-- 1. 'TickedGenerated' because it references 'TicketEvent'
-- 2. 'TicketEvent' because it references 'Ticket' and 'Event'
-- 3. 'EventUser' because it references 'Event' and 'Users'
-- 4. 'Ticket' because it references 'TicketCategory'
-- 5. 'Event' can be dropped now, all references removed.
-- 6. 'TicketCategory' can be dropped now (after Ticket is dropped)
-- 7. 'Users' can be dropped at any time (no references to it)

IF OBJECT_ID('dbo.TicketGenerated', 'U') IS NOT NULL
    DROP TABLE dbo.TicketGenerated;
GO

IF OBJECT_ID('dbo.TicketEvent', 'U') IS NOT NULL
    DROP TABLE dbo.TicketEvent;
GO

IF OBJECT_ID('dbo.EventUser', 'U') IS NOT NULL
    DROP TABLE dbo.EventUser;
GO

IF OBJECT_ID('dbo.Ticket', 'U') IS NOT NULL
    DROP TABLE dbo.Ticket;
GO

IF OBJECT_ID('dbo.Customer', 'U') IS NOT NULL
DROP TABLE dbo.Ticket;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
GO


IF OBJECT_ID('dbo.TicketCategory', 'U') IS NOT NULL
    DROP TABLE dbo.TicketCategory;
GO


IF OBJECT_ID('dbo.Users', 'U') IS NOT NULL
    DROP TABLE dbo.Users;
GO

-- Create table order (reverse of drop order)
CREATE TABLE dbo.Users (
    id          INT PRIMARY KEY IDENTITY(1,1),
    username    NVARCHAR(50)  NOT NULL UNIQUE,
    mail        NVARCHAR(255) NOT NULL UNIQUE,
    password    NVARCHAR(255) NOT NULL,
    firstName   NVARCHAR(50)  NOT NULL,
    lastName    NVARCHAR(50),
    location    NVARCHAR(255),
    phoneNumber NVARCHAR(30),
    imageName   NVARCHAR(255),
    rank        NVARCHAR(50) NOT NULL DEFAULT 'EventCoordinator' CHECK (rank in ('Admin', 'Event Coordinator')),
    theme       NVARCHAR(50) NOT NULL DEFAULT 'Light' CHECK (theme in ('Light', 'Dark')),
    language    NVARCHAR(50) NOT NULL DEFAULT 'en-GB' CHECK (language in ('en-GB', 'da-DK')),
    fontSize    INT          NOT NULL DEFAULT 14 CHECK (fontSize BETWEEN 8 and 24)
);
GO

CREATE TABLE TicketCategory (
    id   INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(255)
);
GO

CREATE TABLE Event (
    id               INT PRIMARY KEY IDENTITY(1,1),
    title            NVARCHAR(255) NOT NULL,
    imageName        NVARCHAR(255),
    location         NVARCHAR(255),
    startDate        DATE NOT NULL,
    endDate          DATE,
    startTime        TIME NOT NULL,
    endTime          TIME,
    locationGuidance NVARCHAR(255),
    extraInfo        NVARCHAR(255)
);
GO

CREATE TABLE Ticket (
    id             INT PRIMARY KEY IDENTITY(1,1),
    title          NVARCHAR(255),
    classification NVARCHAR(50) CHECK (classification IN ('PAID', 'PROMOTIONAL')),
);
GO

CREATE TABLE EventUser (
    EventId INT,
    UserId  INT,
    PRIMARY KEY (EventId, UserId),
    FOREIGN KEY (EventId) REFERENCES Event(id),
    FOREIGN KEY (UserId) REFERENCES Users(id)
);
GO

CREATE TABLE TicketEvent (
     id       INT PRIMARY KEY IDENTITY(1,1),
     ticketId INT,
     eventId  INT NULL,
     price    DECIMAL(10, 2) NULL,
     quantity INT,
     FOREIGN KEY (ticketId) REFERENCES Ticket(id),
     FOREIGN KEY (eventId) REFERENCES Event(id)
);
GO

CREATE TABLE TicketGenerated (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    eventId    INT, -- Can be null, null defines that its available for all events.
    customerId INT NULL,
    assigned   BIT DEFAULT 0,
    used       BIT DEFAULT 0,
    barcode    NVARCHAR(255),
    qrcode     NVARCHAR(255),
    FOREIGN KEY (eventId) REFERENCES TicketEvent(id),
    -- FOREIGN KEY (customerId) REFERENCES Customer(id)
);
GO

create table Customers
(
    id   int identity,
    mail varchar(100)
);

INSERT INTO Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('International Food Festival', 'card1.jpg', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', 'Go through the head entrance, take a right and look for the big stairs going down to the basement.', 'Prepare the food at 1200'),
    ('Vegan Cooking Workshop', 'card2.jpg', '6700, Esbjerg', '2024-05-10', '2024-05-10', '09:00', '12:00', 'Go through the head entrance, take a right and look for the big stairs going down to the basement.', ''),
    ('Farm to Table Dinner', 'card3.jpg', '6700, Esbjerg', '2024-06-15', '2024-06-17', '18:00', NULL, '', 'Prepare the food at 1200'),
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

-- Password: test
INSERT INTO dbo.Users (username, mail, password, firstName, lastName, location, phoneNumber, imageName, rank, theme, language, fontSize)
VALUES
    ('test', 'test@test.dk', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy', 'Test fornavn', 'Test efternavn', '6700, Esbjerg', '+4512345678', 'profileImage.jpeg', 'Admin', 'Light', 'en-GB', 14);
GO

INSERT INTO dbo.EventUser (EventId, UserId)
VALUES
    (1, 1), (2, 1), (3, 1);
GO

INSERT INTO Ticket (title, classification) VALUES ('Rock Band Live', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Shakespeare Play', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Football Match', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Tech Conference 2024', 'PROMOTIONAL');
INSERT INTO Ticket (title, classification) VALUES ('Jazz Night', 'PAID');
INSERT INTO Ticket (title, classification) VALUES ('Broadway Musical', 'PAID');