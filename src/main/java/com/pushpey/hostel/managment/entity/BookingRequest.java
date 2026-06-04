package com.pushpey.hostel.managment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class BookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  Student (User with STUDENT role)
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    //  Room
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    //  Status (ENUM)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    // Request time
    private LocalDateTime createdAt;

    //  Approval time
    private LocalDateTime updatedAt;

    //  Auto set time before insert
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }

    //  Auto update time before update
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}