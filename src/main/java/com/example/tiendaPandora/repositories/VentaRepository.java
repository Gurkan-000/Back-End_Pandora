package com.example.tiendaPandora.repositories;

import com.example.tiendaPandora.entities.Venta;
import com.example.tiendaPandora.repositories.projections.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface VentaRepository extends JpaRepository<Venta, UUID> {


    @Query("""
        SELECT
            COALESCE(SUM(v.total), 0) AS ventasTotales,
            COUNT(v) AS cantidadPedidos,
            COALESCE(AVG(v.total), 0) AS ticketPromedio
        FROM Venta v
        WHERE v.fechaVenta >= :fechaInicio
        AND v.fechaVenta < :fechaFin
    """)
    ProyeccionResumenVentas obtenerResumenVentasPorPeriodo(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    @Query("""
        SELECT COALESCE(SUM(d.cantidad), 0)
        FROM Venta v
        JOIN v.pedido p
        JOIN p.detalles d
        WHERE v.fechaVenta >= :fechaInicio
        AND v.fechaVenta < :fechaFin
    """)
    Long obtenerUnidadesVendidasPorPeriodo(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT
            FUNCTION('DATE', v.fechaVenta) AS fecha,
            SUM(v.total) AS ventas,
            COUNT(v) AS cantidadPedidos
        FROM Venta v
        WHERE v.fechaVenta >= :fechaInicio
        AND v.fechaVenta < :fechaFin
        GROUP BY FUNCTION('DATE', v.fechaVenta)
        ORDER BY FUNCTION('DATE', v.fechaVenta)
    """)
    List<ProyeccionVentasPorFecha> obtenerVentasPorFecha(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT
            prod.idProducto AS idProducto,
            prod.nombreProducto AS nombreProducto,
            prod.urlImagen AS urlImagen,
            SUM(d.cantidad) AS unidadesVendidas,
            SUM(d.pagoSubtotal) AS importeTotal
        FROM Venta v
        JOIN v.pedido p
        JOIN p.detalles d
        JOIN d.varianteProducto vp
        JOIN vp.producto prod
        WHERE v.fechaVenta >= :fechaInicio
        AND v.fechaVenta < :fechaFin
        GROUP BY
            prod.idProducto,
            prod.nombreProducto,
            prod.urlImagen
        ORDER BY SUM(d.cantidad) DESC
    """)
    List<ProyeccionProductosMasVendidos> obtenerProductosMasVendidos(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );


    @Query("""
        SELECT
            cat.idCategoria AS idCategoria,
            cat.nombreCategoria AS nombreCategoria,
            SUM(d.pagoSubtotal) AS ventasTotales,
            SUM(d.cantidad) AS unidadesVendidas
        FROM Venta v
        JOIN v.pedido p
        JOIN p.detalles d
        JOIN d.varianteProducto vp
        JOIN vp.producto prod
        JOIN prod.subCategoria sub
        JOIN sub.categoria cat
        WHERE v.fechaVenta >= :fechaInicio
        AND v.fechaVenta < :fechaFin
        GROUP BY
            cat.idCategoria,
            cat.nombreCategoria
        ORDER BY SUM(d.pagoSubtotal) DESC
    """)
    List<ProyeccionVentasPorCategoria> obtenerVentasPorCategoria(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

}

