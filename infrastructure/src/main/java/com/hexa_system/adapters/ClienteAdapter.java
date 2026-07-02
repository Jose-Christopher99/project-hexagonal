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
                .map(existente -> new ClienteDTO(
                        existente.getId(),
                        existente.getTipoDoc(),
                        existente.getNumDoc(),
                        existente.getNombre(),
                        existente.getApellidoP(),
                        existente.getApellidoM(),
                        existente.getTelefono()
                ))
                .orElseGet(()->{
                    Cliente cliente = new Cliente();
                    cliente.setTipoDoc(dto.tipoDoc());
                    cliente.setNumDoc(dto.numDoc());
                    cliente.setNombre(dto.nombre());
                    cliente.setApellidoP(dto.apellidoP());
                    cliente.setApellidoM(dto.apellidoM());
                    cliente.setTelefono(dto.telefono());
                    Cliente guardado= clienteRepository.save(cliente);
                    return new ClienteDTO(
                            guardado.getId(),
                            guardado.getTipoDoc(),
                            guardado.getNumDoc(),
                            guardado.getNombre(),
                            guardado.getApellidoP(),
                            guardado.getApellidoM(),
                            guardado.getTelefono()
                    );
                });
    }
}
