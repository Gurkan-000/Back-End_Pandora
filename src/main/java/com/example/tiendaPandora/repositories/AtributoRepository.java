package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Atributo;
import com.example.tiendaPandora.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AtributoRepository extends JpaRepository<Atributo, UUID> {

}
