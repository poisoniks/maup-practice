CREATE TABLE baskets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_user_basket FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE basket_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    basket_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_basket_item_basket FOREIGN KEY (basket_id) REFERENCES baskets (id) ON DELETE CASCADE,
    CONSTRAINT fk_basket_item_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

CREATE INDEX idx_basket_item_basket_id ON basket_items (basket_id);
CREATE INDEX idx_basket_item_product_id ON basket_items (product_id);
