package com.user.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class UserAuthority {
    @Id
    private UUID forumId;
    @MapsId
    @ManyToOne
    private User user;
    @Column(name = "authority_type")
    private String authorityType;
}
