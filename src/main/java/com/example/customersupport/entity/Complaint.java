package com.example.customersupport.entity;


import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complaint_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private ComplaintStatus complaintStatus;


    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @Column(name="priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;


    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;


    //mapping
    @OneToMany(mappedBy = "complaint")
    private List<Attachment> attachmentList;

    @OneToMany(mappedBy = "complaint")
    private List<Chatting> chattingList;
}
