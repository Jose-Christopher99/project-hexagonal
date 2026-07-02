package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.EmpleadoDTO;
import com.hexa_system.aggregates.dto.EmpleadoResponseDTO;

import java.util.List;

public interface EmpleadoServiceIn {
    EmpleadoResponseDTO crearEmpleadoIn(EmpleadoDTO empleadoDTO);
    List<EmpleadoResponseDTO> listarEmpleadoIn();
    EmpleadoResponseDTO actualizarEmpleadoIn(Long id,EmpleadoDTO empleadoDTO);
    void eliminarEmpleadoIn(Long id);
}
