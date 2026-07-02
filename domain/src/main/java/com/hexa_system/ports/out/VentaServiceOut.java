package com.hexa_system.ports.out;

import com.hexa_system.aggregates.dto.ProcesarVentaDTO;
import com.hexa_system.aggregates.dto.VentaDTO;

public interface VentaServiceOut {
    VentaDTO procesarVentaOut(ProcesarVentaDTO dto);
}
