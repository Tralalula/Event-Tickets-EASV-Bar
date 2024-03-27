USE EventManager_TEST;
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