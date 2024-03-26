USE EventManager_TEST;
GO

INSERT INTO Event (title, startDate, endDate, startTime, endTime, imageName)
VALUES
    ('A', '2024-04-05', '2024-04-05',' 10:00', '20:00', 'sample.png'),
    (REPLICATE('A', 255), '2024-05-10', '2024-05-10', '09:00', '12:00', 'sample.png');
GO