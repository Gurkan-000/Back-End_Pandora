package com.example.tiendaPandora.entities;

import com.example.tiendaPandora.entities.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Usuarios")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements UserDetails {

    @Id
    @UuidGenerator
    private UUID idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String correo;

    private String contrasena;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(unique = true)
    private String googleId;

    private boolean correoVerificado;

    @Override
    public @NullMarked Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public @NullMarked String getUsername() {
        return correo;
    }

}
