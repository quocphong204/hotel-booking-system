package com.hotel.booking.repository;

import com.hotel.booking.entity.RoomEntity;
import com.hotel.booking.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {

    Optional<RoomEntity> findByRoomNumber(String roomNumber);

    List<RoomEntity> findByStatus(RoomStatus status);

    long countByStatus(RoomStatus status);
}
