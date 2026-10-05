package com.example.tiendaPandora.dtos.response;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseMarca {

    private UUID idMarca;

    private String nombreMarca;

}
