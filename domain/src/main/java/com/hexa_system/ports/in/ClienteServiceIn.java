package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.ClienteDTO;
import com.hexa_system.aggregates.dto.ClienteResponseDTO;

public interface ClienteServiceIn {
    ClienteResponseDTO consultarDniIn(String dni);
    ClienteDTO guardarClienteIn(ClienteDTO dto);
}
