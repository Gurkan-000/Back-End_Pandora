package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseVarianteProducto {

    private UUID idVarianteProducto;

    private Integer stock;

    private String urlImagen;

    private Boolean esPrincipal;

    private List<ResponseValorAtributo> atributos;

}
