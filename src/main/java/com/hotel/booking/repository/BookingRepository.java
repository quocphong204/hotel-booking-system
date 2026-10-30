package com.hotel.booking.repository;

import com.hotel.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Optional<BookingEntity> findByBookingCode(String bookingCode);

    List<BookingEntity> findByRoomIdOrderByCreatedAtDesc(Long roomId);

    List<BookingEntity> findByStatus(String status);

    Optional<BookingEntity> findFirstByRoomIdAndStatusInOrderByCreatedAtDesc(Long roomId, List<String> statuses);

    List<BookingEntity> findAllByOrderByCreatedAtDesc();
}
