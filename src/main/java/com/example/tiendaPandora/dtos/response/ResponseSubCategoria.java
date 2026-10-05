package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseSubCategoria {

    private UUID idSubCategoria;

    private UUID idCategoria;

    private String nombreCategoria;

    private String nombreSubCategoria;

}
