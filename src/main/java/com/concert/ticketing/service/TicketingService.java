package com.concert.ticketing.service;

import com.concert.ticketing.domain.Booking;
import com.concert.ticketing.domain.Seat;
import com.concert.ticketing.domain.User;
import com.concert.ticketing.repository.BookingRepository;
import com.concert.ticketing.repository.SeatRepository;
import com.concert.ticketing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TicketingService {

    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 좌석예약
     * 1. 유저 조회
     * 2. 좌석 조회
     * 3. 좌석 점유
     * 4. 예약 정보 저장
     */

    @Transactional(readOnly = true)
    public String getSeatStatus(Long seatId) {
        String cacheKey = "seat:status" + seatId;

        // 1. Redis 확인
        String status = (String) redisTemplate.opsForValue().get(cacheKey);
        if (status != null) {
            return status;  // 캐시 히트
        }

        // 2. DB 조회 (캐시 미스)
        Seat seat = seatRepository.findById(seatId).orElseThrow(()-> new IllegalArgumentException("좌석을 찾을 수 없습니다."));
        status = seat.getStatus().toString();

        // 3. 캐시 저장 (유효시간 5분)
        redisTemplate.opsForValue().set(cacheKey, status, Duration.ofMinutes(5));

        return status;
    }

    /**
     * 일반 메서드로 변경(더 이상 여기에서 락 걸지 않음)
     */
    @Transactional
    public Long reserveSeat(Long userId, Long seatId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 좌석 조회
        Seat seat = seatRepository.findById(seatId).orElseThrow(()-> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

        // 이미 예약된 좌석인지 더블체크
        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }

        // 비관적 락 조회
        // 2. 좌석 조회
        // Seat seat = seatRepository.findByIdWithLock(seatId).orElseThrow(()-> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

        //3. 좌석 예약
        seat.reserve();

        // 4. 예약 생성 및 저장
        Booking booking = new Booking(user, seat);
        bookingRepository.save(booking);

        // 상태 변경 -> 캐시 삭제 -> 삭제하지 않을 시 Redis(가능), DB(예약됨)상태 발생
        String cacheKey = "seat:status" + seatId;
        redisTemplate.delete(cacheKey);

        return booking.getId();
    }
}
