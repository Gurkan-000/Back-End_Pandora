package com.example.tiendaPandora.dtos.response;


import lombok.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDashboardAlertasStock {

    private Integer stockMinimoGlobal;

    private Integer cantidadVariantesStockBajo;

    private Integer cantidadVariantesAgotadas;

    private List<ResponseDashboardVarianteStock> variantes;

}