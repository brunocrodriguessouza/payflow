CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    due_date DATE,
    payment_date DATE,
    value DECIMAL(10, 2),
    description VARCHAR(255),
    status VARCHAR(50)
);