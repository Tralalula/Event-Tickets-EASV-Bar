USE EventManager_TEST;
GO

-- Drop order
-- 1. 'TickedGenerated' because it references 'TicketEvent'
-- 2. 'TicketEvent' because it references 'Ticket' and 'Event'
-- 3. 'EventUser' because it references 'Event' and 'Users'
-- 4. 'Ticket' because it references 'TicketCategory'
-- 5. 'Customers' ..
-- 6. 'Event' can be dropped now, all references removed.
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

IF OBJECT_ID('dbo.Customers', 'U') IS NOT NULL
    DROP TABLE dbo.Customers;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
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

CREATE TABLE Customers (
    id   INT IDENTITY(1,1) PRIMARY KEY ,
    mail NVARCHAR(100)
);
GO
