package com.deisfansha.si_amto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "t_majors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private int majorCode;
    private String name;
    private String eduLevel;
    private String groupMajor; // IERA, AP, Non Amto
    private Boolean active;

}
