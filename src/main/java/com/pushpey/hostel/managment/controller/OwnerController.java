package com.pushpey.hostel.managment.controller;

import com.pushpey.hostel.managment.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService service;

    @PostMapping("/approve")
    public String approve(@RequestParam Long requestId) {
        return service.approve(requestId);
    }

    @PostMapping("/approve-vacate")
    public String approveVacate(@RequestParam Long requestId) {
        return service.approveVacate(requestId);
    }

    @PostMapping("/force-vacate/{studentId}")
    public String forceVacate(@PathVariable Long studentId) {
        return service.forceVacate(studentId);
    }

    @PostMapping("/reject/{requestId}")
    public String reject(@PathVariable Long requestId) {
        return service.reject(requestId);
    }

    @PostMapping("/reject-vacate/{requestId}")
    public String rejectVacate(@PathVariable Long requestId) {
        return service.rejectVacate(requestId);
    }
}
