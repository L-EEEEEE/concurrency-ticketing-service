-- 1. 테스트 유저
INSERT IGNORE INTO users(id, name, point) VALUES (1, 'Tester1', 1000);
INSERT IGNORE INTO users(id, name, point) VALUES (2, 'Tester2', 1000);
INSERT IGNORE INTO users(id, name, point) VALUES (3, 'Tester3', 1000);
INSERT IGNORE INTO users(id, name, point) VALUES (4, 'Tester4', 1000);
INSERT IGNORE INTO users(id, name, point) VALUES (5, 'Tester5', 1000);
INSERT IGNORE INTO users(id, name, point) VALUES (6, 'Tester6', 1000);

-- 2. 좌석 데이터
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (1, 1, 1, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (2, 1, 2, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (3, 1, 3, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (4, 1, 4, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (5, 1, 5, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (6, 1, 6, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (7, 1, 7, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (8, 1, 8, 'AVAILABLE');
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (9, 1, 9, 'AVAILABLE');