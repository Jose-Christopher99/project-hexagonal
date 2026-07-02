package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.EmpleadoDTO;
import com.hexa_system.aggregates.dto.EmpleadoResponseDTO;
import com.hexa_system.entity.Empleado;
import com.hexa_system.entity.Rol;
import com.hexa_system.ports.out.EmpleadoServiceOut;
import com.hexa_system.repository.EmpleadoRepository;
import com.hexa_system.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoAdapter implements EmpleadoServiceOut {
    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;

    @Override
    public EmpleadoResponseDTO crearEmpleadoOut(EmpleadoDTO empleadoDTO) {
        Rol rol = rolRepository.findByNombreRol(empleadoDTO.rol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado en la base de datos"));
        Empleado empleado = new Empleado(
                null,
                empleadoDTO.email(),
                new BCryptPasswordEncoder().encode(empleadoDTO.password()),
                rol,
                empleadoDTO.tipoDoc(),
                empleadoDTO.numDoc(),
                empleadoDTO.nombre(),
                empleadoDTO.apellidoP(),
                empleadoDTO.apellidoM(),
                empleadoDTO.telefono()
        );
        Empleado empleadoSave= empleadoRepository.save(empleado);
        return new EmpleadoResponseDTO(
                empleadoSave.getId(),
                empleadoSave.getTipoDoc(),
                empleadoSave.getNumDoc(),
                empleadoSave.getNombre(),
                empleadoSave.getApellidoP(),
                empleadoSave.getApellidoM(),
                empleadoSave.getTelefono(),
                empleadoSave.getRol().getNombreRol(),
                empleadoSave.getEmail()
        );
    }

    @Override
    public List<EmpleadoResponseDTO> listarEmpleadoOut() {
        return empleadoRepository.findAll()
                .stream()
                .map(e -> new EmpleadoResponseDTO(
                        e.getId(),
                        e.getTipoDoc(),
                        e.getNumDoc(),
                        e.getNombre(),
                        e.getApellidoP(),
                        e.getApellidoM(),
                        e.getTelefono(),
                        e.getEmail(),
                        e.getRol().getNombreRol()
                ))
                .toList();
    }

    @Override
    public EmpleadoResponseDTO actualizarEmpleadoOut(Long id, EmpleadoDTO empleadoDTO) {
        Rol rol= rolRepository.findByNombreRol(empleadoDTO.rol()).orElseThrow(
                ()-> new RuntimeException("Rol no encontrado en la base de datos"));
        Empleado empleado= empleadoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Empleado no encontrado en la base de datos"));
        empleado.setNombre(empleadoDTO.nombre());
        empleado.setApellidoP(empleadoDTO.apellidoP());
        empleado.setApellidoM(empleadoDTO.apellidoM());
        empleado.setEmail(empleadoDTO.email());
        empleado.setPassword(new BCryptPasswordEncoder().encode(empleadoDTO.password()));
        empleado.setTelefono(empleadoDTO.telefono());
        empleado.setRol(rol);
        Empleado actualizarEmpleado= empleadoRepository.save(empleado);
        return new EmpleadoResponseDTO(
                actualizarEmpleado.getId(),
                actualizarEmpleado.getTipoDoc(),
                actualizarEmpleado.getNumDoc(),
                actualizarEmpleado.getNombre(),
                actualizarEmpleado.getApellidoP(),
                actualizarEmpleado.getApellidoM(),
                actualizarEmpleado.getTelefono(),
                actualizarEmpleado.getRol().getNombreRol(),
                actualizarEmpleado.getEmail()
        );
    }

    @Override
    public void eliminarEmpleadoOut(Long id) {
        empleadoRepository.deleteById(id);
    }
}
