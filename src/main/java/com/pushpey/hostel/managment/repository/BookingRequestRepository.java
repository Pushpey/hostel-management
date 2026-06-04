package com.pushpey.hostel.managment.repository;


import com.pushpey.hostel.managment.entity.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, Long> {

    List<BookingRequest> findByStatus(String status);

    List<BookingRequest> findByStudentId(Long studentId);

    Optional<BookingRequest> findByStudentIdAndRoomId(Long studentId, Long roomId);
}