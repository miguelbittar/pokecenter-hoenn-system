CREATE TABLE nurse_joy(
    id VARCHAR(36) PRIMARY KEY,
    nurse_joy_id VARCHAR(9) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);