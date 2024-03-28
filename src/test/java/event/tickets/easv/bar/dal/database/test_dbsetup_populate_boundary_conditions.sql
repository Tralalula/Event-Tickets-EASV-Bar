USE EventManager_TEST;
GO

INSERT INTO dbo.Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('A', 'sample.png', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', '', ''),
    (REPLICATE('A', 255), 'sample.png', '6700, Esbjerg', '2024-05-10', '2024-05-10', '09:00', '12:00', '', '');
GO
