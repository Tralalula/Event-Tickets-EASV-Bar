USE EventManager;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
GO

CREATE TABLE Event (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE,
    startTime TIME NOT NULL,
    endTime TIME,
    imageName NVARCHAR(255)
);
GO

INSERT INTO Event (title, startDate, endDate, startTime, endTime, imageName)
VALUES
    ('International Food Festival', '2024-04-05', '2024-04-05',' 10:00', '20:00', 'card1.jpg'),
    ('Vegan Cooking Workshop', '2024-05-10', '2024-05-10', '09:00', '12:00', 'card2.jpg'),
    ('Farm to Table Dinner', '2024-06-15', '2024-06-17', '18:00', NULL, 'card3.jpg'),
    ('Wine and Cheese Night', '2024-07-20', '2024-07-20', '17:00', '21:00', 'card4.jpg'),
    ('Italian Pasta Making Class', '2024-08-25', '2024-08-25', '11:00', '14:00', 'card5.jpg'),
    ('French Cuisine Tasting', '2024-09-30', NULL, '16:00', '21:00', 'card6.jpg'),
    ('Sushi Rolling Workshop', '2024-10-05', '2024-10-05', '12:00', '15:00', 'card7.jpg'),
    ('Chocolate Making Class', '2024-11-10', NULL, '10:00', NULL, 'card8.jpg'),
    ('BBQ and Grill Cook-off', '2024-12-15', '2024-12-15', '11:00', '18:00', 'card9.jpg'),
    ('Farmers Market Tour', '2024-01-20', '2024-01-21', '09:00', NULL, 'card10.jpg'),
    ('Pastry and Baking Workshop', '2024-02-25', '2024-02-25', '10:00', '13:00', 'card11.jpg'),
    ('Coffee Tasting Experience', '2024-03-02', NULL, '09:00', '17:00', 'card12.jpg'),
    ('Beer Brewing Demonstration', '2024-04-07', '2024-04-07', '14:00', '17:00', 'card13.jpg'),
    ('Gourmet Burger Festival', '2024-05-12', '2024-05-12', '12:00', '20:00', 'card14.jpg'),
    ('Mexican Fiesta Night', '2024-06-17', NULL, '17:00', NULL, 'card15.jpg'),
    ('Street Food Extravaganza', '2024-07-22', '2024-07-22', '16:00', '23:00', 'card16.jpg'),
    ('Ice Cream Social', '2024-08-27', NULL, '13:00', NULL, 'card17.jpg'),
    ('Pizza Making Party', '2024-09-01', '2024-09-01', '11:00', '14:00', 'card18.jpg'),
    ('Seafood Feast', '2024-10-06', '2024-10-06', '18:00', '21:00', 'card19.jpg'),
    ('Culinary Arts Festival', '2024-11-11', NULL, '10:00', NULL, 'card20.jpg');
GO