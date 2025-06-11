
DROP TABLE IF EXISTS order_details;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS users;

--  جدول کاربران
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- جدول محصولات منو
CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
    category VARCHAR(50),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  جدول سفارشات
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    total_price NUMERIC(10, 2) NOT NULL CHECK (total_price >= 0),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID', 'CANCELLED', 'COMPLETED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  جدول جزئیات سفارش
CREATE TABLE order_details (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--  جدول پرداخت‌ها
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id),
    amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    payment_method VARCHAR(20) NOT NULL CHECK (payment_method IN ('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'ONLINE')),
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED', 'REFUNDED')),
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_id VARCHAR(100)
);

-- ایندکس برای بهبود عملکرد
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_order_details_order_id ON order_details(order_id);
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_menu_items_category ON menu_items(category);

-- 8. درج داده‌های اولیه

-- کاربران
INSERT INTO users (username, password, email) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4H9hPHMfwZ2QDK7WnEZg7dDU6XyK', 'admin@restaurant.com'), -- پسورد: 1234
('user1', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4H9hPHMfwZ2QDK7WnEZg7dDU6XyK', 'user1@example.com'),
('user2', '$2a$10$N9qo8uLOickgx2ZMRZoMy.Mrq4H9hPHMfwZ2QDK7WnEZg7dDU6XyK', 'user2@example.com');

-- محصولات منو
INSERT INTO menu_items (name, description, price, category) VALUES
('پیتزا مخصوص', 'پیتزا با پنیر، قارچ، ژامبون و زیتون', 180000, 'پیتزا'),
('همبرگر ویژه', 'همبرگر با نان تست، پنیر چدار و سس مخصوص', 120000, 'فست فود'),
('سالاد سزار', 'سالاد سزار با سس مخصوص و نان تست', 80000, 'سالاد'),
('پاستا آلفردو', 'پاستا با سس آلفردو و قارچ', 150000, 'پاستا'),
('نوشابه', 'نوشابه گازدار 330 میلی‌لیتری', 30000, 'نوشیدنی');

-- سفارشات
INSERT INTO orders (user_id, total_price, status) VALUES
(1, 360000, 'PAID'),
(2, 200000, 'PENDING');

-- جزئیات سفارش
INSERT INTO order_details (order_id, menu_item_id, quantity, price) VALUES
(1, 1, 2, 180000), -- 2 پیتزا مخصوص
(1, 5, 2, 30000),  -- 2 نوشابه
(2, 2, 1, 120000), -- 1 همبرگر
(2, 3, 1, 80000);  -- 1 سالاد

-- پرداخت‌ها
INSERT INTO payments (order_id, amount, payment_method, status) VALUES
(1, 360000, 'CREDIT_CARD', 'SUCCESS'),
(2, 200000, 'CASH', 'PENDING');

