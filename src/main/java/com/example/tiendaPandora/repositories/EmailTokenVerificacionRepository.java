package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.EmailTokenVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailTokenVerificacionRepository extends JpaRepository<EmailTokenVerificacion, UUID> {

    Optional<EmailTokenVerificacion> findByToken(String token);

}
