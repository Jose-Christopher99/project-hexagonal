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
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmpleadoResponseDTO crearEmpleadoOut(EmpleadoDTO empleadoDTO) {
        Rol rol = rolRepository.findByNombreRol(empleadoDTO.rol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado en la base de datos"));
        Empleado empleado = Empleado.builder()
                .tipoDoc(empleadoDTO.tipoDoc())
                .numDoc(empleadoDTO.numDoc())
                .nombre(empleadoDTO.nombre())
                .apellidoP(empleadoDTO.apellidoP())
                .apellidoM(empleadoDTO.apellidoM())
                .telefono(empleadoDTO.telefono())
                .email(empleadoDTO.email())
                .password(passwordEncoder.encode(empleadoDTO.password()))
                .rol(rol)
                .build();
        Empleado empleadoSave= empleadoRepository.save(empleado);
        return toDTO(empleadoSave);
    }

    @Override
    public List<EmpleadoResponseDTO> listarEmpleadoOut() {
        return empleadoRepository.findAll()
                .stream()
                .map(this::toDTO)
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
        return toDTO(actualizarEmpleado);
    }

    @Override
    public void eliminarEmpleadoOut(Long id) {
        empleadoRepository.deleteById(id);
    }

    private EmpleadoResponseDTO toDTO(Empleado empleado){
        return new EmpleadoResponseDTO(
                empleado.getId(),
                empleado.getTipoDoc(),
                empleado.getNumDoc(),
                empleado.getNombre(),
                empleado.getApellidoP(),
                empleado.getApellidoM(),
                empleado.getTelefono(),
                empleado.getRol().getNombreRol(),
                empleado.getEmail()
        );
    }
}
