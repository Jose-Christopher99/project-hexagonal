package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.ClienteDTO;
import com.hexa_system.aggregates.dto.ClienteResponseDTO;

public interface ClienteServiceOut {
    ClienteResponseDTO consultarDniOut(String dni);
    ClienteDTO guardarClienteOut(ClienteDTO dto);
}
