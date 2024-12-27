CREATE TABLE addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    order_date TIMESTAMP,
    status VARCHAR(50),
    total DECIMAL(10, 2),
    address_id BIGINT,
    FOREIGN KEY (address_id) REFERENCES addresses (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNIQUE NOT NULL,
    payment_date TIMESTAMP,
    amount DECIMAL(10, 2),
    payment_method VARCHAR(50) NOT NULL,
    card_number VARCHAR(255),
    card_holder VARCHAR(255),
    card_expiry VARCHAR(10),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);
