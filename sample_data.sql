-- =========================================================
-- sample_data.sql
-- Dữ liệu mẫu cho Library Management System: thể loại, tác giả, 10 cuốn sách.
-- Chạy sau khi đã tạo bảng (schema.sql hoặc để Hibernate tự tạo khi start app).
-- Cách dùng:
--   mysql -u root -p library_db < sample_data.sql
-- =========================================================

USE library_db;

-- ---- Thể loại (Categories) ----
INSERT INTO categories (name, description) VALUES
('Công nghệ', 'Sách về lập trình, phần mềm và công nghệ thông tin'),
('Văn học', 'Tiểu thuyết và tác phẩm văn học kinh điển'),
('Khoa học', 'Sách phổ biến kiến thức khoa học'),
('Kỹ năng sống', 'Sách phát triển bản thân và kỹ năng mềm'),
('Lịch sử', 'Sách về lịch sử thế giới và Việt Nam');

-- ---- Tác giả (Authors) ----
INSERT INTO authors (name, biography) VALUES
('Robert C. Martin', 'Kỹ sư phần mềm nổi tiếng với các nguyên tắc Clean Code, Clean Architecture'),
('Nam Cao', 'Nhà văn hiện thực phê phán nổi tiếng của Việt Nam'),
('Yuval Noah Harari', 'Sử gia và tác giả người Israel, nổi tiếng với Sapiens'),
('Dale Carnegie', 'Tác giả người Mỹ, chuyên về kỹ năng giao tiếp và phát triển bản thân'),
('Eric Matthes', 'Tác giả sách lập trình Python cho người mới bắt đầu'),
('Ngô Tất Tố', 'Nhà văn, nhà báo Việt Nam đầu thế kỷ 20'),
('Stephen Hawking', 'Nhà vật lý lý thuyết, tác giả A Brief History of Time');

-- ---- Sách (Books) - 10 cuốn ----
INSERT INTO books (title, isbn, published_year, description, quantity, available_quantity, author_id, category_id, created_at, updated_at) VALUES
('Clean Code', '9780132350884', 2008, 'Hướng dẫn viết mã nguồn sạch, dễ bảo trì cho lập trình viên', 5, 5,
    (SELECT id FROM authors WHERE name = 'Robert C. Martin'), (SELECT id FROM categories WHERE name = 'Công nghệ'), NOW(), NOW()),
('Clean Architecture', '9780134494166', 2017, 'Nguyên tắc thiết kế kiến trúc phần mềm bền vững', 3, 3,
    (SELECT id FROM authors WHERE name = 'Robert C. Martin'), (SELECT id FROM categories WHERE name = 'Công nghệ'), NOW(), NOW()),
('Python Crash Course', '9781593279288', 2019, 'Nhập môn lập trình Python nhanh, thực hành nhiều dự án', 4, 4,
    (SELECT id FROM authors WHERE name = 'Eric Matthes'), (SELECT id FROM categories WHERE name = 'Công nghệ'), NOW(), NOW()),
('Chí Phèo', '9786041012345', 1941, 'Tập truyện ngắn hiện thực phê phán nổi tiếng của Nam Cao', 6, 6,
    (SELECT id FROM authors WHERE name = 'Nam Cao'), (SELECT id FROM categories WHERE name = 'Văn học'), NOW(), NOW()),
('Lão Hạc', '9786041012369', 1943, 'Truyện ngắn cảm động về số phận người nông dân Việt Nam', 4, 4,
    (SELECT id FROM authors WHERE name = 'Nam Cao'), (SELECT id FROM categories WHERE name = 'Văn học'), NOW(), NOW()),
('Tắt Đèn', '9786041098765', 1937, 'Tiểu thuyết phản ánh cuộc sống người nông dân dưới ách sưu thuế', 3, 3,
    (SELECT id FROM authors WHERE name = 'Ngô Tất Tố'), (SELECT id FROM categories WHERE name = 'Văn học'), NOW(), NOW()),
('Sapiens: Lược Sử Loài Người', '9780062316097', 2011, 'Hành trình phát triển của loài người từ thời kỳ đồ đá đến hiện đại', 5, 5,
    (SELECT id FROM authors WHERE name = 'Yuval Noah Harari'), (SELECT id FROM categories WHERE name = 'Lịch sử'), NOW(), NOW()),
('Homo Deus: Lược Sử Tương Lai', '9780062464316', 2015, 'Dự đoán tương lai của loài người trong kỷ nguyên công nghệ', 3, 3,
    (SELECT id FROM authors WHERE name = 'Yuval Noah Harari'), (SELECT id FROM categories WHERE name = 'Khoa học'), NOW(), NOW()),
('Đắc Nhân Tâm', '9780671027032', 1936, 'Cuốn sách kinh điển về nghệ thuật giao tiếp và ứng xử', 8, 8,
    (SELECT id FROM authors WHERE name = 'Dale Carnegie'), (SELECT id FROM categories WHERE name = 'Kỹ năng sống'), NOW(), NOW()),
('Lược Sử Thời Gian', '9780553380163', 1988, 'Giới thiệu các khái niệm vũ trụ học cho độc giả phổ thông', 2, 2,
    (SELECT id FROM authors WHERE name = 'Stephen Hawking'), (SELECT id FROM categories WHERE name = 'Khoa học'), NOW(), NOW());
