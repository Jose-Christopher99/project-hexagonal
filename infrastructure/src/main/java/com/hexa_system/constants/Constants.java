package com.hexa_system.constants;

public class Constants {
    public static final String[] PERMIT_ENDPOINTS = {
            "/api/auth/**"
    };

    // ENDPOINTS SOLO PARA ADMINISTRADOR
    public static final String[] ENDPOINTS_ADMIN = {
            "/api/empleados/**",
            "/api/productos/**",
            "/api/roles/**"
    };

    // ENDPOINTS PARA VENDEDOR
    public static final String[] ENDPOINTS_VENDEDOR = {
            "/api/venta/**",
            "/api/cliente/**",
            "/api/productos/listar"
    };

}
