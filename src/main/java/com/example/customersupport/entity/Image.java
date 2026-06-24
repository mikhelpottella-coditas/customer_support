package com.example.customersupport.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "complaint_id",nullable = false)
    private Complaint complaint;


    @Lob
    @Column(columnDefinition = "IMAGEBLOB", nullable = false)
    private String imageHolder;


    @Column(name = "reference_text")
    private String referenceText;

}
