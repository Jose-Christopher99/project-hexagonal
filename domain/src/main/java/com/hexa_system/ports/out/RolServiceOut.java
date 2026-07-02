package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.RolesDTO;
import java.util.List;

public interface RolServiceOut {
    RolesDTO crearRolOut(RolesDTO rolesDTO);
    RolesDTO obtenerRolOut(Long id);
    List<RolesDTO> listarRolesOut();
    RolesDTO actualizarRolOut(Long id, RolesDTO rolesDTO);
    void eliminarRolOut(Long id);
}
