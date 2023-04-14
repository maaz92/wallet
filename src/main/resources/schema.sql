CREATE TABLE if not exists wallet(
    id BIGINT auto_increment,
    customer_id BIGINT NOT NULL,
    balance NUMERIC(20, 2) default 0,
    created_at TIMESTAMP(9) WITH TIME ZONE default CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(9) WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE if not exists transaction(
    id BIGINT auto_increment,
    wallet_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    description VARCHAR(50) NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    created_at TIMESTAMP(9) WITH TIME ZONE NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);

CREATE TABLE if not exists idempotency_tracker(
    unique_key VARCHAR(50) NOT NULL,
    created_at TIMESTAMP(9) WITH TIME ZONE NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (unique_key)
);