CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    due_date DATE,
    payment_date DATE,
    value DECIMAL(10, 2),
    description VARCHAR(255),
    status VARCHAR(50)
);

INSERT INTO accounts (due_date, payment_date, value, description, status)
VALUES
('2024-06-01', '2024-06-02', 100.00, 'Payment for invoice #12345', 'PAID'),
('2024-06-05', '2024-06-06', 250.50, 'Payment for invoice #12346', 'PAID'),
('2024-06-10', NULL, 300.75, 'Payment for invoice #12347', 'UNPAID'),
('2024-06-15', '2024-06-16', 450.00, 'Payment for invoice #12348', 'PAID'),
('2024-06-20', NULL, 500.00, 'Payment for invoice #12349', 'UNPAID');