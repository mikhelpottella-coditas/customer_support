package com.example.customersupport.entity;


import com.example.customersupport.enums.InviteStatus;
import com.example.customersupport.enums.SupportType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invite")
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sentBy;

    private String invitationTo;

    private UUID inviteToken;

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    @Enumerated(EnumType.STRING)
    private SupportType supportType;

    private LocalDateTime creationDate;

    private LocalDateTime expirationDate;


}
