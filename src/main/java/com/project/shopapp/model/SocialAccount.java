package com.project.shopapp.model;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column (name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "name", nullable = false, length = 50)
    private String name;
}
