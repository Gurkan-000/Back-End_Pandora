package com.example.tiendaPandora.services;

import com.example.tiendaPandora.entities.Pedido;
import com.example.tiendaPandora.entities.Venta;
import com.example.tiendaPandora.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;

    public void crear(Pedido pedido) {
        Venta venta = Venta.builder()
                .fechaVenta(LocalDateTime.now())
                .total(pedido.getPagoTotal())
                .pedido(pedido)
                .build();

        venta = ventaRepository.save(venta);

        pedido.setVenta(venta);
    }


}
