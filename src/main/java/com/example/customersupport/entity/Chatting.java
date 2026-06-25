package com.example.customersupport.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chatting")
public class Chatting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complaint",nullable = false)
    private Complaint complaint;

    @Column(name = "sender_id",nullable = false)
    private Long senderId;

    @Column(name = "sender_name",nullable = false)
    private String SenderName;

    @Column(name = "message",nullable = false)
    private String message;

    @Column(name = "created_at",nullable = false)
    private LocalDateTime createdAt;


}
