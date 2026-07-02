package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.ProductoDTO;
import com.hexa_system.entity.Producto;
import com.hexa_system.ports.out.ProductoServiceOut;
import com.hexa_system.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoAdapter implements ProductoServiceOut {
    private final ProductoRepository productoRepository;

    @Override
    public ProductoDTO crearProductoOut(ProductoDTO productoDTO) {
        Producto producto = new  Producto(
                null, productoDTO.nombre(),productoDTO.precio(),productoDTO.stock(),productoDTO.categoria());
        Producto creado= productoRepository.save(producto);
        return new ProductoDTO(
                creado.getId(),
                creado.getNombre(),
                creado.getPrecio(),
                creado.getStock(),
                creado.getCategoria()
        );
    }

    @Override
    public ProductoDTO obtenerProductoOut(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("El producto con id "+ id +" no se encuentra registrado en la base de datos"));
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria()
        );
    }

    @Override
    public List<ProductoDTO> listarProductosOut() {
        return productoRepository.findAll()
                .stream()
                .map(p -> new ProductoDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio(),
                        p.getStock(),
                        p.getCategoria()
                ))
                .toList();
    }

    @Override
    public ProductoDTO actualizarProductoOut(Long id, ProductoDTO productoDTO) {
        Producto producto = productoRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("El producto no se encuentra registrado en la base de datos"));
        producto.setNombre(productoDTO.nombre());
        producto.setPrecio(productoDTO.precio());
        producto.setCategoria(productoDTO.categoria());
        producto.setStock(productoDTO.stock());
        productoRepository.save(producto);
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getCategoria()
        );
    }

    @Override
    public void eliminarProductoOut(Long id) {
        productoRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("El producto no se encuentra registrado en la base de datos"));
        productoRepository.deleteById(id);
    }
}
