package com.concert.ticketing.controller;

import com.concert.ticketing.service.TicketingFacade;
import com.concert.ticketing.service.TicketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketingService ticketingService;
    private final TicketingFacade ticketingFacade;

    /**
    // 예약 요청 API
    // POST http://localhost:8080/booking
    @PostMapping("/booking")
    public String bookTicket(@RequestBody ReservationRequest request) {
        try {
            Long bookingId = ticketingService.reserveSeat(request.userId(), request.seatId());
            return "예약 성공! (v2) 예약 번호 : " + bookingId;
        } catch (Exception e) {
            // 실패
            return "예약 실패 : " + e.getMessage();
        }
    }
    */

    @PostMapping("/reserve")
    public ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {
        ticketingFacade.reserve(request.userId(), request.seatId());
        return ResponseEntity.ok("예약 성공!");
    }

    // 요청 데이터 받을 DTO
    public record ReservationRequest(Long userId, Long seatId) {}

    // 좌석 상태 조회 API
    @GetMapping("/seats/{seatId}/status")
    public String getSeatStatus(@PathVariable Long seatId) {
        return ticketingService.getSeatStatus(seatId);
    }
}
