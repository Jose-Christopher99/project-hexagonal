package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.RolesDTO;
import com.hexa_system.entity.Rol;
import com.hexa_system.ports.out.RolServiceOut;
import com.hexa_system.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolAdapter implements RolServiceOut {
    private final RolRepository rolRepository;

    @Override
    public RolesDTO crearRolOut(RolesDTO rolesDTO) {
        Rol rol = new Rol(
                null,
                rolesDTO.nombre()
        );
        Rol rolCreado= rolRepository.save(rol);
        return new RolesDTO(
                rolCreado.getIdRol(),
                rolCreado.getNombreRol()
        );
    }

    @Override
    public RolesDTO obtenerRolOut(Long id) {
        Rol rol = rolRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("El rol no se encuentra registrado en la base de datos"));
        return new RolesDTO(
                rol.getIdRol(),
                rol.getNombreRol()
        );
    }

    @Override
    public List<RolesDTO> listarRolesOut() {
        return rolRepository.findAll()
                .stream()
                .map(r -> new RolesDTO(
                        r.getIdRol(),
                        r.getNombreRol()
                ))
                .toList();
    }

    @Override
    public RolesDTO actualizarRolOut(Long id, RolesDTO rolesDTO) {
        Rol rol = rolRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("El rol no se encuentra en la base de datos"));
        rol.setNombreRol(rolesDTO.nombre());
        rolRepository.save(rol);
        return new RolesDTO(
                rol.getIdRol(),
                rol.getNombreRol()
        );
    }

    @Override
    public void eliminarRolOut(Long id) {
        rolRepository.deleteById(id);
    }
}
