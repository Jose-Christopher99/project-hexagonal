package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.RolesDTO;
import com.hexa_system.ports.in.RolServiceIn;
import com.hexa_system.ports.out.RolServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolServiceIn {
    private final RolServiceOut rolServiceOut;

    @Override
    public RolesDTO crearRolIn(RolesDTO rolesDTO) {
        RolesDTO rol= rolServiceOut.crearRolOut(rolesDTO);
        return rol;
    }

    @Override
    public RolesDTO obtenerRolIn(Long id) {
        RolesDTO rol= rolServiceOut.obtenerRolOut(id);
        return rol;
    }

    @Override
    public List<RolesDTO> listarRolesIn() {
        List<RolesDTO> roles= rolServiceOut.listarRolesOut();
        return roles;
    }

    @Override
    public RolesDTO actualizarRolIn(Long id, RolesDTO rolesDTO) {
        RolesDTO rol= rolServiceOut.actualizarRolOut(id, rolesDTO);
        return rol;
    }

    @Override
    public void eliminarRolIn(Long id) {
        rolServiceOut.eliminarRolOut(id);
    }
}
