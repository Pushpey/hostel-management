package com.pushpey.hostel.managment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int roomNumber;

    private int capacity = 2;

    @Column(name = "is_full")
    private boolean full;

    @Column(name = "occupied_beds")
    private int occupiedBeds = 0;
}