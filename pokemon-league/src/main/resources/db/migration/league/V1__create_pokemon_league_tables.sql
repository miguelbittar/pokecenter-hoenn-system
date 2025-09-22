CREATE TABLE trainers (
    id VARCHAR(36) PRIMARY KEY,
    trainer_id VARCHAR(9) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    region VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE valid_cities (
    id VARCHAR(36) PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL,
    region VARCHAR(20) NOT NULL,
    authority_type VARCHAR(20) NOT NULL,
    authority_name VARCHAR(200),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE rvps (
    id VARCHAR(36) PRIMARY KEY,
    trainer_id VARCHAR(9) NOT NULL,
    target_region VARCHAR(20) NOT NULL,
    issuing_city_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    revoked_date DATE NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_rvp_trainer FOREIGN KEY (trainer_id) REFERENCES trainers(trainer_id),
    CONSTRAINT fk_rvp_issuing_city FOREIGN KEY (issuing_city_id) REFERENCES valid_cities(id)
);