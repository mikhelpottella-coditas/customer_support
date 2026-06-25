package com.example.customersupport.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "complaint_id",nullable = false)
    private Complaint complaint;



    @Column(name="file", nullable = false)
    private String file;


    @Column(name = "reference_text")
    private String referenceText;

}
