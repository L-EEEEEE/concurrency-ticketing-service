package com.concert.ticketing.service;

import com.concert.ticketing.domain.Booking;
import com.concert.ticketing.domain.Seat;
import com.concert.ticketing.domain.User;
import com.concert.ticketing.repository.BookingRepository;
import com.concert.ticketing.repository.SeatRepository;
import com.concert.ticketing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketingService {

    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;

    /**
     * 좌석예약
     * 1. 유저 조회
     * 2. 좌석 조회
     * 3. 좌석 점유
     * 4. 예약 정보 저장
     */

    @Transactional
    public Long reserveSeat(Long userId, Long seatId) {
        // 1. 유저 조회
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        /**
         * 낙관적 락 사용 시
        // 2. 좌석 조회
        Seat seat = seatRepository.findById(seatId).orElseThrow(()-> new IllegalArgumentException("좌석을 찾을 수 없습니다."));
         */

        // 비관적 락 조회
        // 2. 좌석 조회
        Seat seat = seatRepository.findByIdWithLock(seatId).orElseThrow(()-> new IllegalArgumentException("좌석을 찾을 수 없습니다."));

        //3. 좌석 예약 -> 이미 예약된 경우 예외 발생
        seat.reserve();

        // 4. 예약 생성 및 저장
        Booking booking = new Booking(user, seat);
        bookingRepository.save(booking);

        return booking.getId();
    }
}
