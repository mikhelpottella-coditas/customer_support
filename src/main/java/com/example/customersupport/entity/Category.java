package com.example.customersupport.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issue",nullable = false)
    private String issue;

    @Column(name = "description")
    private String description;


    // mapping

    @OneToMany(mappedBy = "category")
    private List<Complaint> complaintList;

}
