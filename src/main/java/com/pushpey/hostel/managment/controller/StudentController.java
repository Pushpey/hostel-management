package com.pushpey.hostel.managment.controller;

import com.pushpey.hostel.managment.entity.Room;
import com.pushpey.hostel.managment.repository.RoomRepository;
import com.pushpey.hostel.managment.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private RoomRepository roomRepo;

    @GetMapping("/rooms")
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    @PostMapping("/request")
    public String request(@RequestParam Long roomId) {
        return service.requestRoom(roomId);
    }

    @PostMapping("/vacate-request")
    public String vacateRequest(@RequestParam Long studentId) {
        return service.requestVacate(studentId);
    }
}
