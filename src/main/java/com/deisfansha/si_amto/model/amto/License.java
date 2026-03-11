package com.deisfansha.si_amto.model.amto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_license")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licenseCode;
    private String groupMajor;
    private Boolean active;
    private String name;

}
