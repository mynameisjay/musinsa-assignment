CREATE TABLE IF NOT EXISTS brand (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(50) NOT NULL,
    bundle_price int NOT NULL DEFAULT 0,
    is_deleted tinyint NOT NULL DEFAULT 0,
    aggregated_at datetime DEFAULT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    modified_at datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS category (
    id bigint NOT NULL AUTO_INCREMENT,
    name varchar(20) NOT NULL,
    depth tinyint NOT NULL DEFAULT 0,
    parent_id bigint DEFAULT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    modified_at datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS product (
    id bigint NOT NULL AUTO_INCREMENT,
    brand_id bigint DEFAULT NULL,
    category_id bigint NOT NULL,
    name varchar(100) NOT NULL,
    price int NOT NULL,
    is_deleted tinyint NOT NULL DEFAULT 0,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    modified_at datetime DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (brand_id) REFERENCES brand (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);
