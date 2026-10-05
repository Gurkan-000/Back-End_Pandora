package com.example.tiendaPandora.entities;

import com.example.tiendaPandora.entities.enums.TipoToken;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "RefreshTokens")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @UuidGenerator
    private UUID idToken;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, unique = true)
    private String jti;

    @Enumerated(EnumType.STRING)
    private TipoToken tipo;

    @Column(nullable = false)
    private LocalDateTime expiracion;

    @Column(nullable = false)
    private Boolean revocado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
