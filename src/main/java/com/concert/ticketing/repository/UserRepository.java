package com.concert.ticketing.repository;

import com.concert.ticketing.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
