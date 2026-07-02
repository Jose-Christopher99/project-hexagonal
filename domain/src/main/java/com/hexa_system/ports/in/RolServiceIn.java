package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.RolesDTO;
import java.util.List;

public interface RolServiceIn {
    RolesDTO crearRolIn(RolesDTO rolesDTO);
    RolesDTO obtenerRolIn(Long id);
    List<RolesDTO> listarRolesIn();
    RolesDTO actualizarRolIn(Long id, RolesDTO rolesDTO);
    void eliminarRolIn(Long id);
}
