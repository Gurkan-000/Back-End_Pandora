package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseProducto {

    private UUID idProducto;

    private UUID idMarca;

    private UUID idSubCategoria;

    private String nombreProducto;

    private String descripcion;

    private BigDecimal precio;

    private String urlImagen;

    private String nombreMarca;

    private String nombreSubcategoria;

    private Integer cantidadVariantes;

}
