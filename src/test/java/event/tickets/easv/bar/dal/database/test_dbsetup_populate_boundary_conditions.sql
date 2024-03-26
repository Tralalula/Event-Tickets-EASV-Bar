USE EventManager_TEST;
GO

INSERT INTO Event (title)
VALUES
    ('A'), -- Single length title
    (REPLICATE('A', 255)); -- Max length title (255)
GO