package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.EmpleadoDTO;
import com.hexa_system.aggregates.dto.EmpleadoResponseDTO;
import com.hexa_system.ports.in.EmpleadoServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmpleadoController {
    private final EmpleadoServiceIn empleadoServiceIn;

    @PostMapping("/crear")
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleados(@RequestBody EmpleadoDTO empleado){
        return ResponseEntity.ok(empleadoServiceIn.crearEmpleadoIn(empleado));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleados(@PathVariable Long id,
                                                                   @RequestBody EmpleadoDTO empleado){
        return ResponseEntity.ok(empleadoServiceIn.actualizarEmpleadoIn(id,empleado));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EmpleadoResponseDTO>> obtenerEmpleados(){
        return ResponseEntity.ok(empleadoServiceIn.listarEmpleadoIn());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id){
        empleadoServiceIn.eliminarEmpleadoIn(id);
        return ResponseEntity.noContent().build();
    }
}
