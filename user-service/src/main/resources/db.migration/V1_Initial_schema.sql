CREATE TABLE IF NOT EXISTS users (
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL
);

INSERT INTO users (first_name, last_name, email, phone, password_hash, user_role)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '123-456-7890', 'hashed_password_1', 'USER'),
    ('George', 'Meshveliani', 'giorgimeshve@gmail.com', '123-456-7891', 'hashed_password_2', 'ADMIN'),
    ('Mark', 'Johnson', 'mark.johnson@example.com', '123-456-7892', 'hashed_password_3', 'USER'),
    ('Emily', 'Davis', 'emily.davis@example.com', '123-456-7893', 'hashed_password_4', 'USER'),
    ('Michael', 'Brown', 'michael.brown@example.com', '123-456-7894', 'hashed_password_5', 'USER''),
    ('Sophia', 'Williams', 'sophia.williams@example.com', '123-456-7895', 'hashed_password_6', 'USER''),
    ('James', 'Miller', 'james.miller@example.com', '123-456-7896', 'hashed_password_7', 'USER''),
    ('Olivia', 'Wilson', 'olivia.wilson@example.com', '123-456-7897', 'hashed_password_8', 'USER''),
    ('Daniel', 'Moore', 'daniel.moore@example.com', '123-456-7898', 'hashed_password_9', 'USER''),
    ('Isabella', 'Taylor', 'isabella.taylor@example.com', '123-456-7899', 'hashed_password_10', 'USER'');
