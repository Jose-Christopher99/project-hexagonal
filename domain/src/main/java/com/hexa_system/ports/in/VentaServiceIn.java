package com.hexa_system.ports.in;

import com.hexa_system.aggregates.dto.ProcesarVentaDTO;
import com.hexa_system.aggregates.dto.VentaDTO;

public interface VentaServiceIn {
    VentaDTO procesarVentaIn(ProcesarVentaDTO dto);
}
