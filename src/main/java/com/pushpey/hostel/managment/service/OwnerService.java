package com.pushpey.hostel.managment.service;

import com.pushpey.hostel.managment.entity.BookingRequest;
import com.pushpey.hostel.managment.entity.RequestStatus;
import com.pushpey.hostel.managment.entity.Room;
import com.pushpey.hostel.managment.entity.User;
import com.pushpey.hostel.managment.repository.BookingRequestRepository;
import com.pushpey.hostel.managment.repository.RoomRepository;
import com.pushpey.hostel.managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    @Autowired
    private BookingRequestRepository requestRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private UserRepository userRepo;

    public String approve(Long requestId) {

        BookingRequest req = requestRepo.findById(requestId).orElseThrow();
        Room room = req.getRoom();

        if (room.isFull()) {
            return "Room full!";
        }

        User student = req.getStudent();
        student.setRoom(room);   // ✅ now works

        userRepo.save(student);

        req.setStatus(RequestStatus.APPROVED); // ✅ enum use

        requestRepo.save(req);

        room.setOccupiedBeds(room.getOccupiedBeds() + 1);

        if(room.getOccupiedBeds() >= room.getCapacity()) {
            room.setFull(true);
        }
        roomRepo.save(room);


        return "Approved!";
    }

    public String approveVacate(Long requestId) {

        BookingRequest req =
                requestRepo.findById(requestId).orElseThrow();

        User student = req.getStudent();
        Room room = req.getRoom();

        // remove room
        student.setRoom(null);

        // decrease occupancy
        room.setOccupiedBeds(room.getOccupiedBeds() - 1);

        // room available again
        room.setFull(false);

        req.setStatus(RequestStatus.VACATE_APPROVED);

        userRepo.save(student);
        roomRepo.save(room);
        requestRepo.save(req);

        return "Vacate approved";
    }

    public String forceVacate(Long studentId) {

        User student = userRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Room room = student.getRoom();

        if(room == null) {
            return "Student already has no room";
        }

        // room empty count decrease
        room.setOccupiedBeds(room.getOccupiedBeds() - 1);

        if(room.getOccupiedBeds() < 0) {
            room.setOccupiedBeds(0);
        }


        // room full false
        room.setFull(false);

        roomRepo.save(room);

        // student room remove
        student.setRoom(null);

        userRepo.save(student);

        return "Student forcefully vacated";
    }

    public String rejectVacate(Long requestId) {

        BookingRequest req =
                requestRepo.findById(requestId).orElseThrow();

        req.setStatus(RequestStatus.VACATE_REJECTED);

        requestRepo.save(req);

        return "Vacate request rejected";
    }



    public String reject(Long requestId) {

        BookingRequest req =
                requestRepo.findById(requestId).orElseThrow();

        // already approved check
        if(req.getStatus() == RequestStatus.APPROVED) {
            return "Approved request cannot be rejected";
        }

        req.setStatus(RequestStatus.REJECTED);

        requestRepo.save(req);

        return "Request rejected";
    }
}