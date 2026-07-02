package com.hexa_system.usecase;

import com.hexa_system.aggregates.dto.ProcesarVentaDTO;
import com.hexa_system.aggregates.dto.VentaDTO;
import com.hexa_system.ports.in.VentaServiceIn;
import com.hexa_system.ports.out.VentaServiceOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaServiceIn {
    private final VentaServiceOut ventaServiceOut;

    @Override
    public VentaDTO procesarVentaIn(ProcesarVentaDTO dto) {
        VentaDTO venta= ventaServiceOut.procesarVentaOut(dto);
        return venta;
    }
}
