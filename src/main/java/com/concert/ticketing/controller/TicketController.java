package com.concert.ticketing.controller;

import com.concert.ticketing.service.TicketingFacade;
import com.concert.ticketing.service.TicketingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketingService ticketingService;
    private final TicketingFacade ticketingFacade;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 예약 요청 API
     */
    @PostMapping("/booking")
    public ResponseEntity<String> reserve(@RequestBody ReservationRequest request) {

        ticketingFacade.reserve(request.userId(), request.seatId());
        messagingTemplate.convertAndSend("/topic/seats", request.seatId());

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
