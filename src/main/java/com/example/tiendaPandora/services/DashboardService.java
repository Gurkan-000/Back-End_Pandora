package com.example.tiendaPandora.services;


import com.example.tiendaPandora.dtos.response.*;
import com.example.tiendaPandora.entities.VarianteProducto;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.repositories.PedidoRepository;
import com.example.tiendaPandora.repositories.VarianteProductoRepository;
import com.example.tiendaPandora.repositories.VentaRepository;
import com.example.tiendaPandora.repositories.projections.ProyeccionResumenVentas;
import com.example.tiendaPandora.repositories.projections.ProyeccionVentasPorFecha;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VentaRepository ventaRepository;
    private final PedidoRepository pedidoRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    @Transactional(readOnly = true)
    public ResponseDashboardResumen obtenerResumen(LocalDate fecha) {

        LocalDateTime fechaActual = LocalDateTime.now();

        // Evitar seleccionar semanas futuras.
        if (fecha.isAfter(fechaActual.toLocalDate())) {
            throw new IllegalArgumentException(
                    "No se puede consultar una fecha futura."
            );
        }

        // Determinar el lunes de la semana seleccionada.
        LocalDate lunesActual = fecha.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );

        // Periodo actual: lunes a lunes (límite exclusivo).
        LocalDateTime inicioPeriodoActual = lunesActual.atStartOfDay();

        LocalDateTime finPeriodoActual = lunesActual
                .plusWeeks(1)
                .atStartOfDay();

        // Si es la semana actual, consultar solamente hasta ahora.
        if (fechaActual.isBefore(finPeriodoActual)) {
            finPeriodoActual = fechaActual;
        }

        // Periodo anterior: misma duración que el periodo actual.
        LocalDateTime inicioPeriodoAnterior =
                inicioPeriodoActual.minusWeeks(1);

        LocalDateTime finPeriodoAnterior =
                finPeriodoActual.minusWeeks(1);

        // Resumen del periodo seleccionado.
        ProyeccionResumenVentas resumen =
                ventaRepository.obtenerResumenVentasPorPeriodo(
                        inicioPeriodoActual,
                        finPeriodoActual
                );

        Long unidadesVendidas =
                ventaRepository.obtenerUnidadesVendidasPorPeriodo(
                        inicioPeriodoActual,
                        finPeriodoActual
                );

        // Resumen del periodo anterior.
        ProyeccionResumenVentas periodoAnterior =
                ventaRepository.obtenerResumenVentasPorPeriodo(
                        inicioPeriodoAnterior,
                        finPeriodoAnterior
                );

        Long unidadesPeriodoAnterior =
                ventaRepository.obtenerUnidadesVendidasPorPeriodo(
                        inicioPeriodoAnterior,
                        finPeriodoAnterior
                );

        return ResponseDashboardResumen.builder()

                .ventasTotales(
                        redondear(resumen.getVentasTotales())
                )

                .cantidadPedidos(
                        resumen.getCantidadPedidos()
                )

                .unidadesVendidas(
                        unidadesVendidas
                )

                .ticketPromedio(
                        redondear(resumen.getTicketPromedio())
                )

                .variacionVentas(
                        calcularVariacion(
                                resumen.getVentasTotales(),
                                periodoAnterior.getVentasTotales()
                        )
                )

                .variacionPedidos(
                        calcularVariacion(
                                BigDecimal.valueOf(resumen.getCantidadPedidos()),
                                BigDecimal.valueOf(periodoAnterior.getCantidadPedidos())
                        )
                )

                .variacionUnidades(
                        calcularVariacion(
                                BigDecimal.valueOf(unidadesVendidas),
                                BigDecimal.valueOf(unidadesPeriodoAnterior)
                        )
                )

                .variacionTicketPromedio(
                        calcularVariacion(
                                resumen.getTicketPromedio(),
                                periodoAnterior.getTicketPromedio()
                        )
                )

                .build();
    }

    private BigDecimal calcularVariacion(BigDecimal actual, BigDecimal anterior) {

        if (anterior.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return actual.subtract(anterior)
                .multiply(BigDecimal.valueOf(100))
                .divide(anterior, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal redondear(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP);
    }


    @Transactional(readOnly = true)
    public List<ResponseDashboardVentas> obtenerVentas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ReglaDeNegocioException(
                    "La fecha de inicio no puede ser posterior a la fecha de fin"
            );
        }

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();

        Map<LocalDate, ProyeccionVentasPorFecha> ventasPorFecha =
                ventaRepository.obtenerVentasPorFecha(inicio, fin)
                        .stream()
                        .collect(Collectors.toMap(
                                ProyeccionVentasPorFecha::getFecha,
                                Function.identity()
                        ));

        List<ResponseDashboardVentas> resultado = new ArrayList<>();

        for (LocalDate fecha = fechaInicio;
             !fecha.isAfter(fechaFin);
             fecha = fecha.plusDays(1)) {

            ProyeccionVentasPorFecha venta = ventasPorFecha.get(fecha);

            resultado.add(ResponseDashboardVentas.builder()
                    .fecha(fecha)
                    .ventas(venta != null
                            ? venta.getVentas().setScale(2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO.setScale(2))
                    .cantidadPedidos(venta != null
                            ? venta.getCantidadPedidos()
                            : 0L)
                    .build());
        }

        return resultado;
    }


    @Transactional(readOnly = true)
    public List<ResponseDashboardProductosMasVendidos> obtenerProductosMasVendidos(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ReglaDeNegocioException(
                    "La fecha de inicio no puede ser posterior a la fecha de fin"
            );
        }

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();

        return ventaRepository.obtenerProductosMasVendidos(inicio, fin)
                .stream()
                .map(producto -> ResponseDashboardProductosMasVendidos.builder()
                        .idProducto(producto.getIdProducto())
                        .nombreProducto(producto.getNombreProducto())
                        .urlImagen(producto.getUrlImagen())
                        .unidadesVendidas(producto.getUnidadesVendidas())
                        .importeTotal(producto.getImporteTotal()
                                .setScale(2, RoundingMode.HALF_UP))
                        .build()
                )
                .toList();
    }


    @Transactional(readOnly = true)
    public List<ResponseDashboardVentasCategoria> obtenerVentasCategoria(
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ReglaDeNegocioException(
                    "La fecha de inicio no puede ser posterior a la fecha de fin"
            );
        }

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay();

        return ventaRepository.obtenerVentasPorCategoria(inicio, fin)
                .stream()
                .map(categoria -> ResponseDashboardVentasCategoria.builder()
                        .idCategoria(categoria.getIdCategoria())
                        .nombreCategoria(categoria.getNombreCategoria())
                        .ventasTotales(categoria.getVentasTotales()
                                .setScale(2, RoundingMode.HALF_UP))
                        .unidadesVendidas(categoria.getUnidadesVendidas())
                        .build()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseDashboardPedidosRecientes> obtenerPedidosRecientes() {

        return pedidoRepository.obtenerPedidosRecientes(PageRequest.of(0, 10))
                .stream()
                .map(pedido -> ResponseDashboardPedidosRecientes.builder()
                        .idPedido(pedido.getIdPedido())
                        .correo(pedido.getCorreo())
                        .estadoPedido(pedido.getEstadoPedido())
                        .fechaPedido(pedido.getFechaPedido())
                        .pagoTotal(pedido.getPagoTotal()
                                .setScale(2, RoundingMode.HALF_UP))
                        .build()
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseDashboardAlertasStock obtenerAlertasStock() {

        final int STOCK_MINIMO_GLOBAL = 5;

        List<VarianteProducto> variantes =
                varianteProductoRepository.obtenerVariantesConStockBajo(
                        STOCK_MINIMO_GLOBAL
                );

        List<ResponseDashboardVarianteStock> alertas = variantes.stream()
                .map(variante -> {

                    String atributos = variante.getVariantesAtributos()
                            .stream()
                            .map(va -> va.getValor().getAtributo().getNombreAtributo()
                                    + ": "
                                    + va.getValor().getValor())
                            .sorted()
                            .collect(Collectors.joining(", "));

                    String estado = variante.getStock() == 0
                            ? "AGOTADO"
                            : "STOCK_BAJO";

                    return ResponseDashboardVarianteStock.builder()
                            .idVarianteProducto(variante.getIdVarianteProducto())
                            .idProducto(variante.getProducto().getIdProducto())
                            .nombreProducto(variante.getProducto().getNombreProducto())
                            .atributos(atributos)
                            .stock(variante.getStock())
                            .estado(estado)
                            .build();
                })
                .toList();

        int cantidadAgotadas = (int) alertas.stream()
                .filter(v -> v.getEstado().equals("AGOTADO"))
                .count();

        int cantidadStockBajo = (int) alertas.stream()
                .filter(v -> v.getEstado().equals("STOCK_BAJO"))
                .count();

        return ResponseDashboardAlertasStock.builder()
                .stockMinimoGlobal(STOCK_MINIMO_GLOBAL)
                .cantidadVariantesStockBajo(cantidadStockBajo)
                .cantidadVariantesAgotadas(cantidadAgotadas)
                .variantes(alertas)
                .build();
    }

}

