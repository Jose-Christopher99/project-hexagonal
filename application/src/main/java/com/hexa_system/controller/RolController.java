package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.RolesDTO;
import com.hexa_system.ports.in.RolServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RolController {
    private final RolServiceIn rolServiceIn;

    @PostMapping("/crear")
    public ResponseEntity<RolesDTO> crearRol(@RequestBody RolesDTO rolesDTO) {
        return ResponseEntity.ok(rolServiceIn.crearRolIn(rolesDTO));
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<RolesDTO> actualizarRol(@PathVariable Long id, @RequestBody RolesDTO rolesDTO) {
        return ResponseEntity.ok(rolServiceIn.actualizarRolIn(id, rolesDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesDTO> obtenerRol(@PathVariable Long id) {
        return ResponseEntity.ok(rolServiceIn.obtenerRolIn(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RolesDTO>> ListarRoles() {
        return ResponseEntity.ok(rolServiceIn.listarRolesIn());
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable Long id){
        rolServiceIn.eliminarRolIn(id);
        return ResponseEntity.noContent().build();
    }
}
