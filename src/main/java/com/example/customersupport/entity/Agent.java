package com.example.customersupport.entity;

import com.example.customersupport.enums.SupportType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "experience",nullable = false)
    private Double experience;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "support_type")
    @Enumerated(EnumType.STRING)
    private SupportType supportType;


    //mapping
    @OneToMany(mappedBy = "agent")
    private List<Complaint> complaintList;

}
