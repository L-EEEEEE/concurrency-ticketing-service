package com.concert.ticketing;

import com.concert.ticketing.domain.Seat;
import com.concert.ticketing.domain.User;
import com.concert.ticketing.repository.SeatRepository;
import com.concert.ticketing.repository.UserRepository;
import com.concert.ticketing.service.TicketingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketingApplicationTests {

	@Autowired
	private TicketingService ticketingService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SeatRepository seatRepository;

	@Test
	@DisplayName("동시에 100명이 예약 요청을 하면, 딱 1명만 성공해야 한다.")
	void reserve_concurrency() throws InterruptedException {
		// Given
		int threadCount = 100;
		// 멀티스레드 환경(32개 스레드 풀 생성)
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// 테스트용 데이터 준비
		Seat seat = seatRepository.save(new Seat(1L, 100));

		// 성공, 실패 횟수 count
		AtomicInteger successCount = new AtomicInteger(0);
		AtomicInteger failCount = new AtomicInteger(0);

		// When
		for(int i=0; i<threadCount; i++) {
			User user = userRepository.save(new User("팬" + i, 1000L));

			executorService.submit(() -> {
				try {
					ticketingService.reserveSeat(user.getId(), seat.getId());
					successCount.incrementAndGet();
				} catch (Exception e) {
					failCount.incrementAndGet();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// Then
		// 현재 로직은 락이 없어 여러명 성공해버림 -> 테스트 실패
		System.out.println("예약 성공한 인원: " + successCount.get());
		System.out.println("예약 실패한 인원: " + failCount.get());

		// 목표 : 성공 1명
		 assertEquals(1, successCount.get());
	}

	@Test
	@DisplayName("좌석예약이 정상완료되어야 한다.")
	void reserve_success() {
		// Given
		User user = userRepository.save(new User("철수", 1000L));
		Seat seat = seatRepository.save(new Seat(1L, 1));	// 1번 공연, 1번 좌석

		// When
		Long bookingId = ticketingService.reserveSeat(user.getId(), seat.getId());

		// Then
		// 1. 예약ID 반환 필요
		assertNotNull(bookingId);

		// 2. 좌석상태가 'RESERVED'로 변경되었는지 확인
		Seat findSeat = seatRepository.findById(seat.getId()).get();
		assertEquals(Seat.SeatStatus.RESERVED, findSeat.getStatus());
	}

	@Test
	@DisplayName("이미 예약된 좌석을 선택하면 실패해야 한다.")
	void reserve_fail_already_booked() {
		// Given
		User user1 = userRepository.save(new User("철수", 1000L));
		User user2 = userRepository.save(new User("영희", 2000L));
		Seat seat = seatRepository.save(new Seat(1L, 2));	// 1번 공연, 2번 좌석

		// 철수가 먼저 예약
		ticketingService.reserveSeat(user1.getId(), seat.getId());

		// When & Then
		// 같은 자리 예약 시도시 에러 발생해야 함.
		assertThrows(IllegalStateException.class, ()-> {
			ticketingService.reserveSeat(user2.getId(), seat.getId());
		});
	}

}
