package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCategoria {

    private UUID idCategoria;

    private String nombreCategoria;

}
