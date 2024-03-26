USE EventManager;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
GO

CREATE TABLE Event (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    startDate DATE NOT NULL,
    startTime TIME NOT NULL,
    endDate DATE,
    endTime TIME,
    imageName NVARCHAR(255)
);
GO

INSERT INTO Event (title, startDate, startTime, endDate, endTime, imageName)
VALUES
    ('International Food Festival', '2024-04-05', '10:00', '2024-04-05', '20:00', 'sample.png'),
    ('Vegan Cooking Workshop', '2024-05-10', '09:00', '2024-05-10', '12:00', 'sample.png'),
    ('Farm to Table Dinner', '2024-06-15', '18:00', '2024-06-17', NULL, 'sample.png'),
    ('Wine and Cheese Night', '2024-07-20', '17:00', '2024-07-20', '21:00', 'sample.png'),
    ('Italian Pasta Making Class', '2024-08-25', '11:00', '2024-08-25', '14:00', 'sample.png'),
    ('French Cuisine Tasting', '2024-09-30', '16:00', NULL, '21:00', 'sample.png'),
    ('Sushi Rolling Workshop', '2024-10-05', '12:00', '2024-10-05', '15:00', 'sample.png'),
    ('Chocolate Making Class', '2024-11-10', '10:00', NULL, NULL, 'sample.png'),
    ('BBQ and Grill Cook-off', '2024-12-15', '11:00', '2024-12-15', '18:00', 'sample.png'),
    ('Farmers Market Tour', '2024-01-20', '09:00', '2024-01-21', NULL, 'sample.png'),
    ('Pastry and Baking Workshop', '2024-02-25', '10:00', '2024-02-25', '13:00', 'sample.png'),
    ('Coffee Tasting Experience', '2024-03-02', '09:00', NULL, '17:00', 'sample.png'),
    ('Beer Brewing Demonstration', '2024-04-07', '14:00', '2024-04-07', '17:00', 'sample.png'),
    ('Gourmet Burger Festival', '2024-05-12', '12:00', '2024-05-12', '20:00', 'sample.png'),
    ('Mexican Fiesta Night', '2024-06-17', '17:00', NULL, NULL, 'sample.png'),
    ('Street Food Extravaganza', '2024-07-22', '16:00', '2024-07-22', '23:00', 'sample.png'),
    ('Ice Cream Social', '2024-08-27', '13:00', NULL, NULL, 'sample.png'),
    ('Pizza Making Party', '2024-09-01', '11:00', '2024-09-01', '14:00', 'sample.png'),
    ('Seafood Feast', '2024-10-06', '18:00', '2024-10-06', '21:00', 'sample.png'),
    ('Culinary Arts Festival', '2024-11-11', '10:00', NULL, NULL, 'sample.png');
GO