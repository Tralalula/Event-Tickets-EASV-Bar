USE EventManager_TEST;
GO

INSERT INTO dbo.Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('Single', 'sample.png', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', '', '');
GO

-- Password: test
INSERT INTO dbo.Users (username, mail, password, firstName, lastName, location, phoneNumber, imageName, rank, theme, language, fontSize)
VALUES
    ('test', 'test@test.dk', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy', 'Test fornavn', 'Test efternavn', '6700, Esbjerg', '+4512345678', 'profileImage.jpeg', 'Admin', 'Light', 'en-GB', 14);
GO