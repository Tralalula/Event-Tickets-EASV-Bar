USE EventManager;
GO

IF OBJECT_ID('dbo.Event', 'U') IS NOT NULL
    DROP TABLE dbo.Event;
GO

CREATE TABLE Event (
    id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL
);
GO

INSERT INTO Event (title)
VALUES
    ('International Food Festival'),
    ('Vegan Cooking Workshop'),
    ('Farm to Table Dinner'),
    ('Wine and Cheese Night'),
    ('Italian Pasta Making Class'),
    ('French Cuisine Tasting'),
    ('Sushi Rolling Workshop'),
    ('Chocolate Making Class'),
    ('BBQ and Grill Cook-off'),
    ('Farmers Market Tour'),
    ('Pastry and Baking Workshop'),
    ('Coffee Tasting Experience'),
    ('Beer Brewing Demonstration'),
    ('Gourmet Burger Festival'),
    ('Mexican Fiesta Night'),
    ('Street Food Extravaganza'),
    ('Ice Cream Social'),
    ('Pizza Making Party'),
    ('Seafood Feast'),
    ('Culinary Arts Festival');
GO