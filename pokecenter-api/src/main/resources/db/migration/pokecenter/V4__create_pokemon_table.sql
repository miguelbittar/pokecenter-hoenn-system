CREATE TABLE pokemon (
    id VARCHAR(36) PRIMARY KEY,
    pokemon_id VARCHAR(9) UNIQUE,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    trainer_id VARCHAR(50) NOT NULL,
    condition VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    nurse_joy_id VARCHAR(36) NOT NULL,
    admission_date TIMESTAMP NOT NULL,
    release_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_pokemon_nurse_joy FOREIGN KEY (nurse_joy_id) REFERENCES nurse_joy(id)
);
