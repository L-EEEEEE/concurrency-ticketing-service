package com.concert.ticketing.service;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TicketingFacade {

    private final RedissonClient redissonClient;
    private final TicketingService ticketingService;

    public void reserve(Long userId, Long seatId) {
        // 1. 락 이름 정의(좌석별로 락)
        String lockName = "lock:seat:" + seatId;
        RLock lock = redissonClient.getLock(lockName);

        try {
            // 2. 락 획득 시도
            // tryLock(대기시간, 점유시간, 시간단위)
            boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if(!available) {
                System.out.println("락 획득 실패 (다른 사람이 예약 중)");
                throw  new IllegalStateException("현재 접속자가 많습니다. 다시 시도해주세요.");
            }

            // 3. 락을 얻은 사람만 예약 수행
            ticketingService.reserveSeat(userId, seatId);

        } catch (InterruptedException e) {
            throw new RuntimeException("서버 에러 발생");
        } finally {
            // 4. 작업 끝나면 무조건 락 해제
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }
    }
}
