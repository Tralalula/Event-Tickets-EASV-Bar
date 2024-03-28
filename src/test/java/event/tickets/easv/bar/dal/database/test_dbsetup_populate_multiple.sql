USE EventManager_TEST;
GO

INSERT INTO dbo.Event (title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo)
VALUES
    ('International Food Festival', 'card1.jpg', '6700, Esbjerg', '2024-04-05', '2024-04-05',' 10:00', '20:00', '', ''),
    ('Vegan Cooking Workshop', 'card2.jpg', '6700, Esbjerg', '2024-05-10', '2024-05-10', '09:00', '12:00', '', ''),
    ('Farm to Table Dinner', 'card3.jpg', '6700, Esbjerg', '2024-06-15', '2024-06-17', '18:00', NULL, '', ''),
    ('Wine and Cheese Night', 'card4.jpg', '6700, Esbjerg', '2024-07-20', '2024-07-20', '17:00', '21:00', '', ''),
    ('Italian Pasta Making Class', 'card5.jpg', '6700, Esbjerg', '2024-08-25', '2024-08-25', '11:00', '14:00', '', ''),
    ('French Cuisine Tasting', 'card6.jpg', '6700, Esbjerg', '2024-09-30', NULL, '16:00', '21:00', '', ''),
    ('Sushi Rolling Workshop', 'card7.jpg', '6700, Esbjerg', '2024-10-05', '2024-10-05', '12:00', '15:00', '', ''),
    ('Chocolate Making Class', 'card8.jpg', '6700, Esbjerg', '2024-11-10', NULL, '10:00', NULL, '', ''),
    ('BBQ and Grill Cook-off', 'card9.jpg', '6700, Esbjerg', '2024-12-15', '2024-12-15', '11:00', '18:00', '', ''),
    ('Farmers Market Tour', 'card10.jpg', '6700, Esbjerg', '2024-01-20', '2024-01-21', '09:00', NULL, '', ''),
    ('Pastry and Baking Workshop', 'card11.jpg', '6700, Esbjerg', '2024-02-25', '2024-02-25', '10:00', '13:00', '', ''),
    ('Coffee Tasting Experience', 'card12.jpg', '6700, Esbjerg', '2024-03-02', NULL, '09:00', '17:00', '', ''),
    ('Beer Brewing Demonstration', 'card13.jpg', '6700, Esbjerg', '2024-04-07', '2024-04-07', '14:00', '17:00', '', ''),
    ('Gourmet Burger Festival', 'card14.jpg', '6700, Esbjerg', '2024-05-12', '2024-05-12', '12:00', '20:00', '', ''),
    ('Mexican Fiesta Night', 'card15.jpg', '6700, Esbjerg', '2024-06-17', NULL, '17:00', NULL, '', ''),
    ('Street Food Extravaganza', 'card16.jpg', '6700, Esbjerg', '2024-07-22', '2024-07-22', '16:00', '23:00', '', ''),
    ('Ice Cream Social', 'card17.jpg', '6700, Esbjerg', '2024-08-27', NULL, '13:00', NULL, '', ''),
    ('Pizza Making Party', 'card18.jpg', '6700, Esbjerg', '2024-09-01', '2024-09-01', '11:00', '14:00', '', ''),
    ('Seafood Feast', 'card19.jpg', '6700, Esbjerg', '2024-10-06', '2024-10-06', '18:00', '21:00', '', ''),
    ('Culinary Arts Festival', 'card20.jpg', '6700, Esbjerg', '2024-11-11', NULL, '10:00', NULL, '', '');
GO

-- Password: test
INSERT INTO dbo.Users (username, password)
VALUES
    ('test', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test1', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test2', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test3', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test4', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test5', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test6', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test7', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test8', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test9', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('test10', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy'),
    ('kakao', '$2a$10$CLYpJK6QyzLKEvKzgnYd4OgBDAhhI0tmlYb02HgWAmfo1icjo0nMy');
GO