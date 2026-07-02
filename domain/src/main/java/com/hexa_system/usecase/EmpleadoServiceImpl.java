package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.EmpleadoDTO;
import com.hexa_system.aggregates.dto.EmpleadoResponseDTO;
import com.hexa_system.ports.in.EmpleadoServiceIn;
import com.hexa_system.ports.out.EmpleadoServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoServiceIn {
    private final EmpleadoServiceOut empleadoServiceOut;

    @Override
    public EmpleadoResponseDTO crearEmpleadoIn(EmpleadoDTO empleadoDTO) {
        EmpleadoResponseDTO dto= empleadoServiceOut.crearEmpleadoOut(empleadoDTO);
        return dto;
    }

    @Override
    public List<EmpleadoResponseDTO> listarEmpleadoIn() {
        List<EmpleadoResponseDTO> list= empleadoServiceOut.listarEmpleadoOut();
        return list;
    }

    @Override
    public EmpleadoResponseDTO actualizarEmpleadoIn(Long id, EmpleadoDTO empleadoDTO) {
        EmpleadoResponseDTO dto= empleadoServiceOut.actualizarEmpleadoOut(id, empleadoDTO);
        return dto;
    }

    @Override
    public void eliminarEmpleadoIn(Long id) {
        empleadoServiceOut.eliminarEmpleadoOut(id);
    }
}
