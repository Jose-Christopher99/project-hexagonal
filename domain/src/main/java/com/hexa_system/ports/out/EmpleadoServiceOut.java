package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.EmpleadoDTO;
import com.hexa_system.aggregates.dto.EmpleadoResponseDTO;

import java.util.List;

public interface EmpleadoServiceOut {

    EmpleadoResponseDTO crearEmpleadoOut(EmpleadoDTO empleadoDTO);
    List<EmpleadoResponseDTO> listarEmpleadoOut();
    EmpleadoResponseDTO actualizarEmpleadoOut(Long id,EmpleadoDTO empleadoDTO);
    void eliminarEmpleadoOut(Long id);
}
