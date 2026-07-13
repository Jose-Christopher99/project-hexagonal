package com.hexa_system.adapters;

import com.hexa_system.aggregates.dto.ClienteDTO;
import com.hexa_system.aggregates.dto.ClienteResponseDTO;
import com.hexa_system.clients.DecolectaReniecFeignClient;
import com.hexa_system.entity.Cliente;
import com.hexa_system.ports.out.ClienteServiceOut;
import com.hexa_system.repository.ClienteRepository;
import com.hexa_system.response.ResponseReniec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteAdapter implements ClienteServiceOut {
    private final ClienteRepository clienteRepository;
    private final DecolectaReniecFeignClient  feignClient;
    @Value("${decoleta.token}")
    private String token;


    @Override
    public ClienteResponseDTO consultarDniOut(String dni) {
        String auth= "Bearer "+token;
        ResponseReniec response= feignClient.consultarDni(dni, auth);
        return new ClienteResponseDTO(
                response.getFirst_name(),
                response.getFirst_last_name(),
                response.getSecond_last_name(),
                response.getDocument_number()
        );
    }

    @Override
    public ClienteDTO guardarClienteOut(ClienteDTO dto) {
        return clienteRepository.findByNumDoc(dto.numDoc())
                .map(this::toDTO)//HACE REFERENCIA AL METODO PRIVADO
                .orElseGet(()->{
                    Cliente cliente = Cliente.builder()
                            .tipoDoc(dto.tipoDoc())
                            .numDoc(dto.numDoc())
                            .nombre(dto.nombre())
                            .apellidoP(dto.apellidoP())
                            .apellidoM(dto.apellidoM())
                            .telefono(dto.telefono())
                            .build();
                    Cliente guardado= clienteRepository.save(cliente);
                    return toDTO(guardado);
                });
    }

    private ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getTipoDoc(),
                cliente.getNumDoc(),
                cliente.getNombre(),
                cliente.getApellidoP(),
                cliente.getApellidoM(),
                cliente.getTelefono()
        );
    }
}
