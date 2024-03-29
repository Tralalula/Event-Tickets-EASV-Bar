USE EventManager_TEST;
GO

-- Drop order
-- 1. 'GeneratedTicket' because it references 'TicketEventAssociation'
-- 2. 'TicketEventAssociation' because it references 'Ticket' and 'Event'
-- 3. 'EventUser' because it references 'Event' and 'Users'
-- 4. 'Ticket' because it references 'TicketCategory'
-- 5. 'Event' can be dropped now, all references removed.
-- 6. 'TicketCategory' can be dropped now (after Ticket is dropped)
-- 7. 'Users' can be dropped at any time (no references to it)

IF OBJECT_ID('dbo.GeneratedTicket', 'U') IS NOT NULL
    DROP TABLE dbo.GeneratedTicket;
GO

IF OBJECT_ID('dbo.TicketEventAssociation', 'U') IS NOT NULL
    DROP TABLE dbo.TicketEventAssociation;
GO

IF OBJECT_ID('dbo.EventUser', 'U') IS NOT NULL
    DROP TABLE dbo.EventUser;
GO

IF OBJECT_ID('dbo.Ticket', 'U') IS NOT NULL
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
    id       INT PRIMARY KEY IDENTITY(1,1),
    username NVARCHAR(50),
    password NVARCHAR(200)
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
    categoryId     INT,
    FOREIGN KEY (categoryId) REFERENCES TicketCategory(id)
);
GO

CREATE TABLE EventUser (
    EventId INT,
    UserId INT,
    PRIMARY KEY (EventId, UserId),
    FOREIGN KEY (EventId) REFERENCES Event(id),
    FOREIGN KEY (UserId) REFERENCES Users(id)
);
GO

CREATE TABLE TicketEventAssociation (
    id       INT PRIMARY KEY IDENTITY(1,1),
    ticketId INT,
    eventId  INT NULL,
    price    DECIMAL(10, 2) NULL,
    quantity INT,
    FOREIGN KEY (ticketId) REFERENCES Ticket(id),
    FOREIGN KEY (eventId) REFERENCES Event(id)
);
GO

CREATE TABLE GeneratedTicket (
    id                       INT PRIMARY KEY IDENTITY(1,1),
    ticketEventAssociationId INT,
    customerId               INT NULL,
    assigned                 BIT DEFAULT 0,
    used                     BIT DEFAULT 0,
    barcode                  NVARCHAR(255),
    qrcode                   NVARCHAR(255),
    FOREIGN KEY (ticketEventAssociationId) REFERENCES TicketEventAssociation(id),
    -- FOREIGN KEY (customerId) REFERENCES Customer(id)
);
GO
