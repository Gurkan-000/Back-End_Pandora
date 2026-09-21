package com.example.tiendaPandora.entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "TokensBlackList")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenBlackList {

    @Id
    @UuidGenerator
    private UUID idBlackToken;

    @Column(length = 500, unique = true)
    private String token;

    @Column(name = "expiracion")
    private Date expiracion;

}