package com.example.tiendaPandora.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTokenVerificacion {

    @Id
    @UuidGenerator
    private UUID idEmailToken;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
