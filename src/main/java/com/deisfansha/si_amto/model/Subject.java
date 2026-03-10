package com.deisfansha.si_amto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "t_subject")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String subjectNumber;
    private String nameSubject;
    private int credit;
    private String majorSubject;
    private Boolean active;

}
