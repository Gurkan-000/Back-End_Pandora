package com.example.tiendaPandora.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @UuidGenerator
    private UUID idUsuario;

}
