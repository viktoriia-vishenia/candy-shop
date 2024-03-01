CREATE TABLE IF NOT EXISTS orders (
                                       id BIGSERIAL PRIMARY KEY,
                                       order_number VARCHAR(255)

);

CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL PRIMARY KEY,
                                           inv_code VARCHAR(255),
                                           price DOUBLE PRECISION,
                                           quantity INTEGER,
                                           order_id BIGINT,
                                           FOREIGN KEY (order_id) REFERENCES "orders"(id)
);

