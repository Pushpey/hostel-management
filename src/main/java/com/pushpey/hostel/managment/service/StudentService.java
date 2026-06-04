package com.pushpey.hostel.managment.service;

import com.pushpey.hostel.managment.entity.BookingRequest;
import com.pushpey.hostel.managment.entity.RequestStatus;
import com.pushpey.hostel.managment.entity.Room;
import com.pushpey.hostel.managment.entity.User;
import com.pushpey.hostel.managment.repository.BookingRequestRepository;
import com.pushpey.hostel.managment.repository.RoomRepository;
import com.pushpey.hostel.managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private BookingRequestRepository requestRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private UserRepository userRepo;

    public String requestRoom(Long roomId) {

        // 🔐 Logged-in user extract (JWT se)
        User student = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        // 🏠 Room fetch
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ❌ Already has room check
        if (student.getRoom() != null) {
            throw new RuntimeException("You already have a room!");
        }

        // ❌ Duplicate request check
        if (requestRepo
                .findByStudentIdAndRoomId(student.getId(), roomId)
                .isPresent()) {

            throw new RuntimeException("Request already sent!");
        }

        // ❌ Room full check (DB count se)
        int count = roomRepo.countStudentsInRoom(roomId);

        if (count >= room.getCapacity()) {
            throw new RuntimeException("Room already full!");
        }

        // 📩 Create request
        BookingRequest req = new BookingRequest();
        req.setStudent(student);
        req.setRoom(room);
        req.setStatus(RequestStatus.PENDING); // ✅ enum use

        requestRepo.save(req);

        return "Room request sent successfully!";
    }


    public String requestVacate(Long studentId) {

        User student = userRepo.findById(studentId)
                .orElseThrow();

        Room room = student.getRoom();

        if(room == null) {
            return "No room allocated";
        }

        BookingRequest request = new BookingRequest();

        request.setStudent(student);
        request.setRoom(room);
        request.setStatus(RequestStatus.VACATE_PENDING);

        requestRepo.save(request);

        return "Vacate request sent";
    }
}