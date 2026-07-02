package com.hexa_system.controller;

import com.hexa_system.aggregates.dto.ClienteResponseDTO;
import com.hexa_system.ports.in.ClienteServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cliente")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ClienteController {
    private final ClienteServiceIn clienteServiceIn;

    @GetMapping("/buscar")
    public ResponseEntity<ClienteResponseDTO> buscarCliente(@RequestParam("dni") String dni){
        return ResponseEntity.ok(clienteServiceIn.consultarDniIn(dni));
    }
}
