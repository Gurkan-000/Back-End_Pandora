package com.example.tiendaPandora.controllers;


import com.example.tiendaPandora.dtos.response.*;
import com.example.tiendaPandora.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/resumen")
    public ResponseEntity<ResponseDashboardResumen> obtenerResumen(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha
    ) {

        return ResponseEntity.ok(
                dashboardService.obtenerResumen(fecha)
        );
    }

    @GetMapping("/ventas")
    public ResponseEntity<List<ResponseDashboardVentas>> obtenerResumen(@RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin) {

        return ResponseEntity.ok(dashboardService.obtenerVentas(fechaInicio, fechaFin));
    }


    @GetMapping("/productos-mas-vendidos")
    public ResponseEntity<List<ResponseDashboardProductosMasVendidos>> obtenerProductosMasVendidos(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin
    ) {
        return ResponseEntity.ok(
                dashboardService.obtenerProductosMasVendidos(fechaInicio, fechaFin)
        );
    }


    @GetMapping("/ventas-categoria")
    public ResponseEntity<List<ResponseDashboardVentasCategoria>> obtenerVentasCategoria(
            @RequestParam LocalDate fechaInicio,
            @RequestParam LocalDate fechaFin
    ) {
        return ResponseEntity.ok(
                dashboardService.obtenerVentasCategoria(fechaInicio, fechaFin)
        );
    }

    @GetMapping("/pedidos-recientes")
    public ResponseEntity<List<ResponseDashboardPedidosRecientes>> obtenerPedidosRecientes() {
        return ResponseEntity.ok(
                dashboardService.obtenerPedidosRecientes()
        );
    }

    @GetMapping("/alertas-stock")
    public ResponseEntity<ResponseDashboardAlertasStock> obtenerAlertasStock() {

        return ResponseEntity.ok(
                dashboardService.obtenerAlertasStock()
        );
    }

}

