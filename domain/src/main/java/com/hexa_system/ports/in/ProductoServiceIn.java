package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.ProductoDTO;

import java.util.List;

public interface ProductoServiceIn {
    ProductoDTO crearProductoIn(ProductoDTO productoDTO);
    ProductoDTO obtenerProductoIn(Long id);
    List<ProductoDTO> listarProductosIn();
    ProductoDTO actualizarProductoIn(Long id, ProductoDTO productoDTO);
    void eliminarProductoIn(Long id);
}
