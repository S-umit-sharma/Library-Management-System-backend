package com.LMS.Library.Management.System.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "states")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String name;

}
