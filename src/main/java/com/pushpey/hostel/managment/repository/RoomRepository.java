package com.pushpey.hostel.managment.repository;

import com.pushpey.hostel.managment.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

   // List<Room> findByIsFullFalse() ;// available rooms
    List<Room> findByFullFalse();

    @Query("SELECT COUNT(u) FROM User u WHERE u.room.id = :roomId")
    int countStudentsInRoom(@Param("roomId") Long roomId);
}