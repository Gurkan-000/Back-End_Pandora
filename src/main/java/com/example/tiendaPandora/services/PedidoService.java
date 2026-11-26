package com.example.tiendaPandora.services;

import com.example.tiendaPandora.dtos.request.RequestPedido;
import com.example.tiendaPandora.dtos.response.ResponseDetallePedido;
import com.example.tiendaPandora.dtos.response.ResponseInfoPedido;
import com.example.tiendaPandora.dtos.response.ResponsePedido;
import com.example.tiendaPandora.entities.*;
import com.example.tiendaPandora.entities.enums.EstadoPedido;
import com.example.tiendaPandora.entities.enums.EstadoTransaccion;
import com.example.tiendaPandora.entities.enums.Rol;
import com.example.tiendaPandora.exceptions.EntidadNoEncontradaException;
import com.example.tiendaPandora.exceptions.ReglaDeNegocioException;
import com.example.tiendaPandora.mappers.MapperDetallePedido;
import com.example.tiendaPandora.mappers.MapperPedido;
import com.example.tiendaPandora.repositories.DetallePedidoRepository;
import com.example.tiendaPandora.repositories.PedidoRepository;
import com.example.tiendaPandora.repositories.TransaccionRepository;
import com.example.tiendaPandora.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import com.example.tiendaPandora.entities.enums.EstadoTransaccion;
import com.example.tiendaPandora.repositories.TransaccionRepository;


@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final CarritoService carritoService;
    private final VentaService ventaService;
    private final UsuarioRepository usuarioRepository;
    private final TransaccionRepository transaccionRepository;

    public Pedido buscarPedido(UUID idPedido) {
        return pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new EntidadNoEncontradaException("Pedido no encontrado"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<ResponsePedido> listar() {

        return pedidoRepository.findAll()
                .stream()
                .map(MapperPedido::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponsePedido> listarPorUsuario(String correo) {
        return pedidoRepository.findByUsuario_CorreoOrderByFechaPedidoDesc(correo)
                .stream()
                .map(MapperPedido::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ResponseInfoPedido listarDetallePorPedido(UUID idPedido, UserDetails usuario) {

        Usuario usuarioAutenticado = (Usuario) usuario;
        Pedido pedido = buscarPedido(idPedido);
        Usuario usuarioPedido = pedido.getUsuario();

        if(!(usuarioAutenticado.getUsername().equals(usuarioPedido.getUsername()) || usuarioAutenticado.getRol() == Rol.ADMIN)) {
            throw new AccessDeniedException("Usuario no permitido");
        }

        List<ResponseDetallePedido> detalles = pedido.getDetalles().stream()
                .map(MapperDetallePedido::toResponse)
                .toList();

        return MapperPedido.toResponse(pedido, detalles);
    }

    @Transactional
    public ResponsePedido crear(String correo, RequestPedido request) {

        Carrito carrito = carritoService.buscarCarritoPorUsuario(correo);

        if (carrito.getDetalles().isEmpty()) {
            throw new ReglaDeNegocioException("El carrito está vacío");
        }

        Pedido pedido = MapperPedido.toEntity(request);

        pedido.addUsuario(carrito.getUsuario());

        for (DetalleCarrito detalleCarrito : carrito.getDetalles()) {
            DetallePedido detallePedido = DetallePedido.builder()
                    .nombreProducto(detalleCarrito.getVarianteProducto().getProducto().getNombreProducto())
                    .precioUnitario(detalleCarrito.getPrecioUnitario())
                    .cantidad(detalleCarrito.getCantidad())
                    .varianteProducto(detalleCarrito.getVarianteProducto())
                    .build();

            detallePedido.calcularSubtotal();

            pedido.addDetalle(detallePedido);
        }

        pedido.calcularPagoTotal();

        pedido = pedidoRepository.save(pedido);

        ventaService.crear(pedido);
        carritoService.vaciarCarrito(carrito);

        return MapperPedido.toResponse(pedido);
    }

    @Transactional
    public void enviar(UUID idPedido) {

        Pedido pedido = buscarPedido(idPedido);

        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE) {
            throw new ReglaDeNegocioException(
                    "Solo se pueden enviar pedidos que estén pendientes"
            );
        }

        pedido.setEstadoPedido(EstadoPedido.ENVIADO);
    }

    @Transactional
    public void entregar(UUID idPedido) {

        Pedido pedido = buscarPedido(idPedido);

        if (pedido.getEstadoPedido() != EstadoPedido.ENVIADO) {
            throw new ReglaDeNegocioException(
                    "Solo se pueden entregar pedidos que estén enviados"
            );
        }

        pedido.setEstadoPedido(EstadoPedido.ENTREGADO);
    }

    @Transactional
    public Pedido crearParaPago(String correo, RequestPedido request) {

        Carrito carrito = carritoService.buscarCarritoPorUsuario(correo);

        if (carrito.getDetalles().isEmpty()) {
            throw new ReglaDeNegocioException("El carrito está vacío");
        }

        Pedido pedido = MapperPedido.toEntity(request);
        pedido.addUsuario(carrito.getUsuario());

        for (DetalleCarrito detalleCarrito : carrito.getDetalles()) {

            DetallePedido detallePedido = DetallePedido.builder()
                    .nombreProducto(
                        detalleCarrito.getVarianteProducto()
                            .getProducto().getNombreProducto()
                    )
                    .precioUnitario(detalleCarrito.getPrecioUnitario())
                    .cantidad(detalleCarrito.getCantidad())
                    .varianteProducto(detalleCarrito.getVarianteProducto())
                    .build();

            detallePedido.calcularSubtotal();
            pedido.addDetalle(detallePedido);
        }

        pedido.calcularPagoTotal();

        pedido = pedidoRepository.save(pedido);

        Transaccion transaccion = Transaccion.builder()
                .estadoTransaccion(EstadoTransaccion.PENDIENTE)
                .monto(pedido.getPagoTotal())
                .fechaTransaccion(LocalDateTime.now())
                .pedido(pedido)
                .build();

        transaccionRepository.save(transaccion);

        return pedido;
    }

    @Transactional(readOnly = true)
    public Transaccion obtenerTransaccionParaPago(
            UUID idPedido,
            String correo
    ) {

        Pedido pedido = buscarPedido(idPedido);

        if (!pedido.getUsuario().getCorreo().equals(correo)) {
            throw new AccessDeniedException("Pedido no permitido");
        }

        return transaccionRepository.findByPedido_IdPedido(idPedido)
                .orElseThrow(() ->
                    new EntidadNoEncontradaException(
                        "Transacción no encontrada"
                    )
                );
    }
    @Transactional
    public void registrarOrdenPayPal(
            UUID idPedido,
            String correo,
            String idOrdenPayPal
    ) {

        Transaccion transaccion =
                obtenerTransaccionParaPago(idPedido, correo);

        transaccion.setIdOrdenPayPal(idOrdenPayPal);
    }

    @Transactional
    public void confirmarPago(
            UUID idPedido,
            String correo,
            String idCapturaPayPal
    ) {

        Transaccion transaccion =
                obtenerTransaccionParaPago(idPedido, correo);

        if (transaccion.getEstadoTransaccion()
                == EstadoTransaccion.PROCEDE) {
            return;
        }

        if (transaccion.getEstadoTransaccion()
                != EstadoTransaccion.PENDIENTE) {
            throw new ReglaDeNegocioException(
                    "La transacción no está pendiente"
            );
        }

        Pedido pedido = transaccion.getPedido();

        transaccion.setEstadoTransaccion(EstadoTransaccion.PROCEDE);
        transaccion.setIdCapturaPayPal(idCapturaPayPal);
        transaccion.setReferenciaPago(idCapturaPayPal);

        ventaService.crear(pedido);

        Carrito carrito = carritoService.buscarCarritoPorUsuario(correo);
        carritoService.vaciarCarrito(carrito);
    }

    @Transactional
    public void marcarPagoFallido(UUID idPedido, String correo) {

        Transaccion transaccion =
                obtenerTransaccionParaPago(idPedido, correo);

        if (transaccion.getEstadoTransaccion()
                == EstadoTransaccion.PENDIENTE) {

            transaccion.setEstadoTransaccion(
                    EstadoTransaccion.NO_PROCEDE
            );
        }
    }
}
