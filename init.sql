CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    brand VARCHAR(255),
    purchase_price NUMERIC(10,2) NOT NULL,
    sale_price NUMERIC(10,2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE addresses (
    id BIGSERIAL PRIMARY KEY,
    zip_code VARCHAR(10) NOT NULL,
    street VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    number VARCHAR(20) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(50) NOT NULL,
    complement VARCHAR(255)
);

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date VARCHAR(12) NOT NULL,
    age INTEGER,
    phone VARCHAR(20),
    email VARCHAR(255) UNIQUE,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    address_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_customers_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

CREATE TABLE motorcycles (
    id UUID PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year VARCHAR(20) NOT NULL,
    color VARCHAR(50) NOT NULL,
    license_plate VARCHAR(20) UNIQUE NOT NULL,
    customer_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_motorcycles_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE employees (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(500) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(20) UNIQUE,
    birth_date VARCHAR(12),
    age INTEGER,
    cpf VARCHAR(14) UNIQUE,
    role VARCHAR(50) NOT NULL,
    address_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,

CONSTRAINT fk_employees_address FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL
);

CREATE TABLE service_orders (
    id BIGSERIAL PRIMARY KEY,
    motorcycle_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    mechanic_id UUID NOT NULL,
    description TEXT,
    labor_price NUMERIC(10,2) NOT NULL,
    total_cost NUMERIC(10,2) NOT NULL,
    service_order_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    started_at TIMESTAMP DEFAULT NULL,
    updated_at TIMESTAMP DEFAULT NULL,
    finished_at TIMESTAMP DEFAULT NULL,
    canceled_at TIMESTAMP DEFAULT NULL,

    CONSTRAINT fk_service_orders_motorcycle FOREIGN KEY (motorcycle_id) REFERENCES motorcycles(id) ON DELETE CASCADE,
    CONSTRAINT fk_service_orders_seller FOREIGN KEY (seller_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_service_orders_mechanic FOREIGN KEY (mechanic_id) REFERENCES employees(id) ON DELETE CASCADE
);

CREATE TABLE service_order_products (
    id BIGSERIAL PRIMARY KEY,
    service_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unitary_price NUMERIC(10,2) NOT NULL,
    final_price NUMERIC(10,2) NOT NULL,

    CONSTRAINT fk_service_order_products_order FOREIGN KEY (service_order_id) REFERENCES service_orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_service_order_products_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);