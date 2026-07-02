package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.ProductoDTO;
import com.hexa_system.ports.in.ProductoServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductoController {
    private final ProductoServiceIn productoIn;

    @PostMapping("/crear")
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoIn.crearProductoIn(productoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerProducto(@PathVariable Long id) {
        return ResponseEntity.ok(productoIn.obtenerProductoIn(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(productoIn.listarProductosIn());
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoIn.actualizarProductoIn(id,productoDTO));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoIn.eliminarProductoIn(id);
        return ResponseEntity.noContent().build();
    }
}
