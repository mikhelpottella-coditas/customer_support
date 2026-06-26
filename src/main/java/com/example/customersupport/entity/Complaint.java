package com.example.customersupport.entity;


import com.example.customersupport.enums.ComplaintStatus;
import com.example.customersupport.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @Column(name="priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;


    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;


    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "rating")
    private Short rating;

    //mapping
    @OneToMany(mappedBy = "complaint",cascade = CascadeType.ALL)
    private List<Attachment> attachmentList;

    @OneToMany(mappedBy = "complaint")
    private List<Chatting> chattingList;


    public void addAttachment(Attachment attachment){
        if(attachmentList==null) attachmentList = new ArrayList<>();
        attachment.setComplaint(this);
        attachmentList.add(attachment);
    }

    public void addChatting(Chatting chatting){
        if(chattingList==null) chattingList = new ArrayList<>();
        chatting.setComplaint(this);
        chattingList.add(chatting);
    }

}
