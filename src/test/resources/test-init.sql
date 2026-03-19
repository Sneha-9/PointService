CREATE TABLE points
(
    id               VARCHAR(255) PRIMARY KEY,
    recordid         VARCHAR(255),
    recordtype       VARCHAR(255),
    aggregatedpoints numeric,
    createdAt        TIMESTAMP WITHOUT TIME ZONE       NOT NULL,
    updatedAt        TIMESTAMP WITHOUT TIME ZONE       NOT NULL
);