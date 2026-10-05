package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseAtributo {

    private UUID idAtributo;

    private String nombreAtributo;

}
