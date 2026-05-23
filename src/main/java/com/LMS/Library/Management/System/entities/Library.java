package com.LMS.Library.Management.System.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libraries")
@Setter
@Getter
public class Library  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;
    private String website;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer bookIssueDays;
    private Integer lateFine;
    private Integer depositAmount;

    @OneToMany(mappedBy = "library")
    private List<Membership> memberships = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
