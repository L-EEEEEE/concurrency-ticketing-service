package com.concert.ticketing.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long concertId;

    private int seatNo;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;  // AVAILABLE, RESERVED, SOLD_OUT

    @Version
    private Long version;

    public Seat(Long concertId, int seatNo) {
        this.concertId = concertId;
        this.seatNo = seatNo;
        this.status = SeatStatus.AVAILABLE;
    }

    public enum SeatStatus {
        AVAILABLE, RESERVED, SOLD_OUT
    }

    public void reserve() {
        if(this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.status = SeatStatus.RESERVED;
    }
}
