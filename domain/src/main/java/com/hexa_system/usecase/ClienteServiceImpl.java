package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.ClienteDTO;
import com.hexa_system.aggregates.dto.ClienteResponseDTO;
import com.hexa_system.ports.in.ClienteServiceIn;
import com.hexa_system.ports.out.ClienteServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteServiceIn {
    private final ClienteServiceOut clienteServiceOut;

    @Override
    public ClienteResponseDTO consultarDniIn(String dni) {
        ClienteResponseDTO resultado= clienteServiceOut.consultarDniOut(dni);
        return resultado;
    }

    @Override
    public ClienteDTO guardarClienteIn(ClienteDTO dto) {
        ClienteDTO resultado= clienteServiceOut.guardarClienteOut(dto);
        return resultado;
    }
}
