package com.concert.ticketing.repository;

import com.concert.ticketing.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT s.id FROM Seat s Where s.status = 'RESERVED'")
    List<Long> findAllReservedSeatsIds();
}
