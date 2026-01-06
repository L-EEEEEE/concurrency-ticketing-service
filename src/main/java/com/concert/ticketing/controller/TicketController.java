package com.concert.ticketing.controller;

import com.concert.ticketing.service.TicketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketingService ticketingService;

    // 예약 요청 API
    // POST http://localhost:8080/booking
    @PostMapping("/booking")
    public String bookTicket(@RequestBody ReservationRequest request) {
        try {
            Long bookingId = ticketingService.reserveSeat(request.userId(), request.seatId());
            return "예약 성공! 예약 번호 : " + bookingId;
        } catch (Exception e) {
            // 실패
            return "예약 실패 : " + e.getMessage();
        }
    }

    // 요청 데이터 받을 DTO
    public record ReservationRequest(Long userId, Long seatId) {}

}
