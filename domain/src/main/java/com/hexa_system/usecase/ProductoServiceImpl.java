package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.ProductoDTO;
import com.hexa_system.ports.in.ProductoServiceIn;
import com.hexa_system.ports.out.ProductoServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoServiceIn {
    private final ProductoServiceOut productoServiceOut;

    @Override
    public ProductoDTO crearProductoIn(ProductoDTO productoDTO) {
        ProductoDTO producto = productoServiceOut.crearProductoOut(productoDTO);
        return producto;
    }

    @Override
    public ProductoDTO obtenerProductoIn(Long id) {
        ProductoDTO producto = productoServiceOut.obtenerProductoOut(id);
        return producto;
    }

    @Override
    public List<ProductoDTO> listarProductosIn() {
        List<ProductoDTO> productos = productoServiceOut.listarProductosOut();
        return productos;
    }

    @Override
    public ProductoDTO actualizarProductoIn(Long id, ProductoDTO productoDTO) {
        ProductoDTO producto = productoServiceOut.actualizarProductoOut(id, productoDTO);
        return producto;
    }

    @Override
    public void eliminarProductoIn(Long id) {
        productoServiceOut.eliminarProductoOut(id);
    }
}
