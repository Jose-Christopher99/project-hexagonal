package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.ProductoDTO;

import java.util.List;

public interface ProductoServiceOut {
    ProductoDTO crearProductoOut(ProductoDTO productoDTO);
    ProductoDTO obtenerProductoOut(Long id);
    List<ProductoDTO> listarProductosOut();
    ProductoDTO actualizarProductoOut(Long id, ProductoDTO productoDTO);
    void eliminarProductoOut(Long id);
}
