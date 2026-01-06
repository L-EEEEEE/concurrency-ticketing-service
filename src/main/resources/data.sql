-- 1. 테스트 유저
INSERT IGNORE INTO users(id, namd, point) VALUES (1, 'Tester', 10000);

-- 2. 좌석 데이터
INSERT IGNORE INTO seat(id, concert_id, seat_no, status) VALUES (1, 1, 1, 'AVAILABLE');