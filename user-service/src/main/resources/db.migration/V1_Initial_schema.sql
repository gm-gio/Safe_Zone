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
    ('John',     'Doe',        'success+john@simulator.amazonses.com',     '+4917600000001', 'hashed_password_1',  'USER'),
    ('George',   'Meshveliani','giorgimeshve@gmail.com',                   '123-456-7891',   'hashed_password_2',  'ADMIN'),
    ('Mark',     'Johnson',    'success+mark@simulator.amazonses.com',     '+4917600000003', 'hashed_password_3',  'USER'),
    ('Emily',    'Davis',      'success+emily@simulator.amazonses.com',    '+4917600000004', 'hashed_password_4',  'USER'),
    ('Michael',  'Brown',      'success+michael@simulator.amazonses.com',  '+4917600000005', 'hashed_password_5',  'USER'),
    ('Sophia',   'Williams',   'success+sophia@simulator.amazonses.com',   '+4917600000006', 'hashed_password_6',  'USER'),
    ('James',    'Miller',     'success+james@simulator.amazonses.com',    '+4917600000007', 'hashed_password_7',  'USER'),
    ('Olivia',   'Wilson',     'success+olivia@simulator.amazonses.com',   '+4917600000008', 'hashed_password_8',  'USER'),
    ('Daniel',   'Moore',      'success+daniel@simulator.amazonses.com',   '+4917600000009', 'hashed_password_9',  'USER'),
    ('Isabella', 'Taylor',     'success+isabella@simulator.amazonses.com', '+4917600000010', 'hashed_password_10', 'USER')
ON CONFLICT (email) DO NOTHING;