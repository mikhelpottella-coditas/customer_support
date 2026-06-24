package com.example.customersupport.entity;

import com.example.customersupport.enums.SupportType;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "experience",nullable = false)
    private Double experience;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "support_type")
    @Enumerated(EnumType.STRING)
    private SupportType supportType;

    @ManyToOne
    @JoinColumn(name = "manger_id",nullable = false)
    private Manager manager;

}
