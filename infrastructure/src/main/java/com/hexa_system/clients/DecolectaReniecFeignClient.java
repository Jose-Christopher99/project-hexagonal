package com.hexa_system.clients;

import com.hexa_system.response.ResponseReniec;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reniec-client", url="${decoleta.base-url}")
public interface DecolectaReniecFeignClient {
    @GetMapping("/v1/reniec/dni")
    ResponseReniec consultarDni(@RequestParam("numero") String numero,
                                @RequestHeader("Authorization") String authorization);
}
