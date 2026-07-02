package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.ProcesarVentaDTO;
import com.hexa_system.aggregates.dto.VentaDTO;
import com.hexa_system.ports.in.VentaServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/venta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class VentaController {
    private final VentaServiceIn ventaServiceIn;

    @PostMapping("/procesar")
    public ResponseEntity<VentaDTO> procesarVenta (@RequestBody ProcesarVentaDTO dto){
        return ResponseEntity.ok(ventaServiceIn.procesarVentaIn(dto));
    }
}
