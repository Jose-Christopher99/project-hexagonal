package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.ClienteDTO;
import com.hexa_system.aggregates.dto.DetalleVentaDTO;
import com.hexa_system.aggregates.dto.ProcesarVentaDTO;
import com.hexa_system.aggregates.dto.VentaDTO;
import com.hexa_system.entity.*;
import com.hexa_system.ports.out.VentaServiceOut;
import com.hexa_system.repository.*;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.Stripe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaAdapter implements VentaServiceOut {
    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ClienteAdapter clienteAdapter;
    private final ClienteRepository clienteRepository;
    @Value("${stripe.secret-key}")
    private String secretKey;

    @Override
    @Transactional
    public VentaDTO procesarVentaOut(ProcesarVentaDTO dto) {
        //Buscamos y/o guardamos al cliente
        ClienteDTO clienteDTO = clienteAdapter.guardarClienteOut(dto.cliente());
        Cliente cliente = clienteRepository.findByNumDoc(clienteDTO.numDoc()).
                orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
        //Buscamos al empleado que inicio sesion
        Empleado empleado = empleadoRepository.findById(dto.empleadoId())
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado: " + dto.empleadoId()));

        //Creamos la venta
        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setEmpleado(empleado);
        venta.setFecha_venta(LocalDate.now());
        venta.setHora_venta(LocalTime.now());
        venta.setEstadoPago("PENDIENTE");
        venta.setTotal(BigDecimal.ZERO);
        ventaRepository.save(venta);

        //Procesamos el detalle de la venta y el total
        BigDecimal total = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (DetalleVentaDTO item : dto.productos()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.productoId()));

            if (producto.getStock() < item.cantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            BigDecimal subTotal = producto.getPrecio()
                    .multiply(BigDecimal.valueOf(item.cantidad()));

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductos(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubTotal(subTotal);
            detalles.add(detalle);

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);
            total = total.add(subTotal);
        }

        detalleVentaRepository.saveAll(detalles);
        venta.setTotal(total);
        ventaRepository.save(venta);

        //Procesamos el Pago con Stripe
        try {
            Stripe.apiKey = secretKey;
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(total.multiply(BigDecimal.valueOf(100)).longValue())
                    .setCurrency("pen")
                    .setDescription("Venta #" + venta.getId())
                    .build();
            PaymentIntent.create(params);

            //Actualizamos el estado de pago
            venta.setEstadoPago("PAGADO");
            ventaRepository.save(venta);

            //Generamos el comprobante
            Comprobante comprobante = new Comprobante();
            comprobante.setVenta(venta);
            comprobante.setTipo(dto.tipoComprobante());
            comprobante.setNumeroComprobante(generarNumeroComprobante(dto.tipoComprobante()));
            comprobante.setFechaEmision(LocalDate.now());
            comprobante.setHoraEmision(LocalTime.now());
            comprobanteRepository.save(comprobante);

            //Retornamos la venta realizada
            return new VentaDTO(
                    venta.getId(),
                    dto.cliente().nombre() + " " + dto.cliente().apellidoP()+ " "+ dto.cliente().apellidoM(),
                    total,
                    "PAGADO",
                    comprobante.getNumeroComprobante(),
                    dto.tipoComprobante(),
                    comprobante.getFechaEmision(),
                    comprobante.getHoraEmision()
            );

        } catch (Exception e) {
            venta.setEstadoPago("CANCELADO");
            ventaRepository.save(venta);
            throw new RuntimeException("Error al procesar el pago: " + e.getMessage());
        }
    }

    private String generarNumeroComprobante(String tipo) {
        String prefijo = tipo.equals("BOLETA") ? "B001" : "F001";
        long conteo = comprobanteRepository.count() + 1;
        return String.format("%s-%05d", prefijo, conteo);
    }

}
