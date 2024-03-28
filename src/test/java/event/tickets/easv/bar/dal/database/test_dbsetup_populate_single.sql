USE EventManager_TEST;
GO

INSERT INTO dbo.Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('Single', 'sample.png', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', '', '');
GO

-- Password: test
INSERT INTO dbo.Users (username, password)
VALUES
    ('test', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy');
GO