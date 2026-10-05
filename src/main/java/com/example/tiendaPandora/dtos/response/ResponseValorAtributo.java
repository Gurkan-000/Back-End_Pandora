package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseValorAtributo {

    private UUID idValorAtributo;

    private UUID idAtributo;

    private String valor;

    private String nombreAtributo;

}
