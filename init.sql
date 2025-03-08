CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(255),
                          brand VARCHAR(255),
                          purchase_price NUMERIC(10,2) NOT NULL,
                          sale_price NUMERIC(10,2) NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT NULL,
                          deleted_at TIMESTAMP DEFAULT NULL,
                          is_active BOOLEAN DEFAULT TRUE
);
