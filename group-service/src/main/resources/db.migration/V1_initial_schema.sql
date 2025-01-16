CREATE TABLE IF NOT EXISTS groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    group_description TEXT
);


CREATE TABLE IF NOT EXISTS group_user (
    group_user_id SERIAL PRIMARY KEY,
    group_id INT NOT NULL,
    user_id BIGINT NOT NULL
);

INSERT INTO groups (group_name, group_description)
VALUES
    ('New Users', 'Group for new users'),
    ('Test Group 1', 'Description for Test Group 1'),
    ('Test Group 2', 'Description for Test Group 2'),
    ('Test Group 3', 'Description for Test Group 3'),
    ('Test Group 4', 'Description for Test Group 4'),
    ('Test Group 5', 'Description for Test Group 5'),
    ('Test Group 6', 'Description for Test Group 6'),
    ('Test Group 7', 'Description for Test Group 7'),
    ('Test Group 8', 'Description for Test Group 8'),
    ('Test Group 9', 'Description for Test Group 9'),
    ('Test Group 10', 'Description for Test Group 10');