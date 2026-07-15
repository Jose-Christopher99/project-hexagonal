# ElectroSystem — Módulo de Ventas

Sistema de gestión de ventas de electrodomésticos desarrollado con **Java Spring Boot** bajo **Arquitectura Hexagonal (Ports & Adapters)**,
base de datos **PostgreSQL en Neon**, pasarela de pagos **Stripe** y autenticación **JWT con doble factor por email**.

---

## Tecnologías utilizadas

| Tecnología | Versión | Uso |

-> Java | 21 | Lenguaje principal utilizado |
-> Spring Boot | 4.0.7 | Framework backend |
-> Spring Security | 7.x | Autenticación y autorización |
-> JWT (jjwt) | 0.11.5 | Tokens de sesión |
-> PostgreSQL (Neon) | — | Base de datos en la nube |
-> Stripe | 26.3.0 | Pasarela de pagos |
-> OpenFeign | — | Consumo API Decoleta (RENIEC) |
-> Lombok | 1.18.32 | Reducción de boilerplate | 
-> Spring Mail | — | Envío de códigos 2FA |

## Arquitectura

El proyecto sigue la **Arquitectura Hexagonal** organizada en módulos Maven independientes:

```
electrosystem/
├── domain/               # Entidades de dominio, puertos y casos de uso
│       ├── aggregates/DTO/       # DTOs que viajan entre capas
│       ├── ports/
│       │   ├── in/               # Puertos de entrada (interfaces)
│       │   └── out/              # Puertos de salida (interfaces)
│       └── usecase/              # Implementación de casos de uso
│
├── application/          # Adaptadores de entrada
│       └── controller/           # Controllers REST
│
└── infrastructure/       # Adaptadores de salida y configuración
        ├── adapters/             # Implementación de puertos de salida
        ├── clients/              # Clientes Feign (APIs externas)
        ├── config/               # JWT, Security, Mail, CORS
        ├── entity/               # Entidades JPA
        ├── repository/           # Spring Data JPA
        └── response/             # DTOs de respuesta de APIs externas
```

## Patrones de diseño implementados

| Patrón | Tipo | Dónde se aplica |
|---|---|---|
| **Builder** | Creacional | `Empleado`, `Cliente`, `Venta`, `DetalleVenta`, `Comprobante` |
| **Adapter** | Estructural | `ClienteAdapter`, `EmpleadoAdapter`, `VentaAdapter`, `ProductoAdapter` |
| **Ports & Adapters** | Arquitectural | Toda la arquitectura hexagonal |

---

## Endpoints disponibles

### Autenticación — `/api/auth`
| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/auth/login` | Login con email y password | Público |
| POST | `/api/auth/verify` | Verificar código 2FA del email | Público |
| POST | `/api/auth/refresh` | Renovar access token | Público |

### Empleados — `/api/empleados`
| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/empleados` | Crear empleado | ADMIN |
| GET | `/api/empleados` | Listar empleados | ADMIN |
| PUT | `/api/empleados/{id}` | Actualizar empleado | ADMIN |
| DELETE | `/api/empleados/{id}` | Eliminar empleado | ADMIN |

### Productos — `/api/productos`
| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/productos` | Crear producto | ADMIN |
| GET | `/api/productos` | Listar productos | ADMIN, VENDEDOR |
| GET | `/api/productos/{id}` | Obtener producto | ADMIN, VENDEDOR |
| PUT | `/api/productos/{id}` | Actualizar producto | ADMIN |
| DELETE | `/api/productos/{id}` | Eliminar producto | ADMIN |

### Ventas — `/api/ventas`
| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| POST | `/api/ventas/procesar` | Procesar venta completa | VENDEDOR |

### Clientes — `/api/clientes`
| Método | Endpoint | Descripción | Acceso |
|---|---|---|---|
| GET | `/api/clientes/buscar?dni=` | Consultar datos por DNI | VENDEDOR |
| POST | `/api/clientes` | Guardar cliente | VENDEDOR |

---

## Flujo de autenticación doble factor

```
1. POST /api/auth/login
   → Valida credenciales (email + password)
   → Genera código de 6 dígitos
   → Envía código al email del empleado
   → Responde: { "accessToken": "CODIGO_ENVIADO" }

2. POST /api/auth/verify
   → Recibe email + código
   → Valida código (expira en 5 minutos)
   → Genera accessToken + refreshToken JWT
   → Responde: { "accessToken": "eyJ...", "refreshToken": "eyJ..." }

3. Usar el accessToken en el header de cada petición:
   Authorization: Bearer eyJ...
```

---

## Flujo de una venta

```
1. Vendedor consulta DNI del cliente → GET /api/clientes/buscar?dni=12345678
2. Decoleta devuelve nombres y apellidos automáticamente
3. Vendedor completa datos faltantes (teléfono, dirección)
4. Vendedor selecciona productos y cantidades
5. POST /api/ventas/procesar
   → Se guarda o reutiliza el cliente en BD
   → Se crea la orden de venta
   → Se calculan subtotales y total
   → Se descuenta el stock de cada producto
   → Se procesa el pago con Stripe
   → Se genera el comprobante (BOLETA o FACTURA)
6. Responde con el comprobante generado
```

---

## Historias de usuario

---

### HU-001 — Login con doble autenticación

**ID:** HU-001
**Módulo:** Autenticación
**Responsable:** Christopher Vasquez

**Descripción:**
Como empleado del sistema, quiero iniciar sesión con mi email y contraseña y recibir un código de verificación 
en mi correo para confirmar mi identidad antes de acceder al sistema.

**Criterios de aceptación:**

1. El sistema debe validar las credenciales del empleado (email y password). Si son incorrectas,
   debe retornar un error `401 Unauthorized` con el mensaje "Credenciales inválidas".

2. Si las credenciales son correctas, el sistema debe generar un código numérico de 6 dígitos,
   guardarlo en la base de datos con una expiración de 5 minutos y enviarlo al email registrado del empleado.

3. Al ingresar el código correcto y vigente en `/api/auth/verify`, el sistema debe retornar un `accessToken` JWT
   y un `refreshToken` válidos para autenticar las siguientes peticiones.

**Subtareas:**
- Implementar endpoint `POST /api/auth/login` que valide credenciales con BCrypt
- Implementar generación y persistencia del código de 6 dígitos con expiración
- Implementar envío de email con Spring Mail y cuenta SMTP dedicada
- Implementar endpoint `POST /api/auth/verify` que valide el código y genere los tokens JWT
- Implementar endpoint `POST /api/auth/refresh` para renovar el access token

---

### HU-002 — Gestión de empleados

**ID:** HU-002
**Módulo:** Administración
**Responsable:** Christopher Vasquez

**Descripción:**
Como administrador del sistema, quiero poder registrar, listar, actualizar y eliminar empleados con sus respectivos roles
para gestionar quién tiene acceso al sistema y con qué permisos.

**Criterios de aceptación:**

1. Solo los usuarios con rol `ADMINISTRADOR` pueden acceder
   a los endpoints de empleados. Cualquier intento de acceso sin ese rol debe retornar `403 Forbidden`.

2. Al crear un empleado, el sistema debe encriptar automáticamente la contraseña con BCrypt antes de persistirla en la base de datos.
   Nunca debe almacenarse en texto plano.

3. Al listar empleados, el sistema no debe exponer la contraseña en la respuesta — el `EmpleadoResponseDTO` solo incluye datos personales,
   email y nombre del rol.

**Subtareas:**
- Implementar `POST /api/empleados` con encriptación BCrypt de contraseña
- Implementar `GET /api/empleados` retornando `EmpleadoResponseDTO` sin password
- Implementar `PUT /api/empleados/{id}` para actualizar datos del empleado
- Implementar `DELETE /api/empleados/{id}` para eliminar empleado
- Configurar Spring Security para proteger endpoints con rol `ADMINISTRADOR`

---

### HU-003 — Gestión de productos

**ID:** HU-003
**Módulo:** Inventario
**Responsable:** Christopher Vasquez

**Descripción:**
Como administrador, quiero poder registrar y gestionar el catálogo de electrodomésticos con su precio y stock disponible, 
para que los vendedores puedan consultarlos al momento de realizar una venta.

**Criterios de aceptación:**

1. Al crear un producto, los campos `nombre` y `precio` son obligatorios. Si alguno falta, el sistema debe retornar `400 Bad Request`.

2. El stock de un producto debe decrementarse automáticamente cada vez que se procesa una venta que lo incluya.
   Si el stock es insuficiente para la cantidad solicitada, la venta debe rechazarse con el mensaje "Stock insuficiente para: {nombre del producto}".

3. Los vendedores solo pueden consultar (`GET`) el catálogo de productos pero no pueden crear, actualizar ni eliminar productos.

**Subtareas:**
- Implementar CRUD completo de productos en `ProductoAdapter`
- Implementar validación de stock en `VentaAdapter` antes de procesar la venta
- Configurar permisos diferenciados: `ADMIN` para escritura, `ADMIN + VENDEDOR` para lectura
- Implementar decremento automático de stock al procesar una venta exitosa

---

### HU-004 — Procesamiento de venta con pasarela de pagos

**ID:** HU-004
**Módulo:** Ventas
**Responsable:** Christopher Vasquez

**Descripción:**
Como vendedor, quiero registrar una venta seleccionando los productos, ingresando los datos del cliente y procesando 
el pago con Stripe para generar automáticamente un comprobante de pago (boleta o factura).

**Criterios de aceptación:**

1. Al procesar una venta, el sistema debe verificar si el cliente ya existe en la base de datos por su número de documento.
   Si existe, lo reutiliza; si no existe, lo registra automáticamente sin duplicar datos.

2. El sistema debe integrar Stripe para procesar el pago en soles peruanos (PEN). Si el pago falla, la venta debe marcarse como `CANCELADO`
   y no debe generarse ningún comprobante, revirtiéndose la transacción completa con `@Transactional`.

3. Si el pago es exitoso, el sistema debe generar automáticamente un comprobante con número correlativo en formato `B001-00001` para boletas
   o `F001-00001` para facturas, y retornar todos los datos de la venta en el `VentaDTO`.

**Subtareas:**
- Implementar `POST /api/ventas/procesar` con flujo completo de venta
- Integrar Stripe con `PaymentIntent` en modo test (PEN)
- Implementar generación automática de número correlativo de comprobante
- Implementar búsqueda o creación automática de cliente por número de documento
- Implementar rollback completo con `@Transactional` ante fallos de pago

---

### HU-005 — Consulta de cliente por DNI

**ID:** HU-005
**Módulo:** Ventas
**Responsable:** Christopher Vasquez

**Descripción:**
Como vendedor, quiero ingresar el número de DNI del cliente en el formulario de venta y que el sistema consulte automáticamente la API de Decoleta 
para autocompletar sus nombres y apellidos, evitando errores de digitación.

**Criterios de aceptación:**

1. Al consultar un DNI válido de 8 dígitos, el sistema debe retornar los datos del cliente (nombres, apellido paterno, apellido materno y número de documento) obtenidos desde la API de Decoleta en menos de 3 segundos.

2. Si el DNI no existe en la API de Decoleta o el servicio no está disponible, el sistema debe retornar un error descriptivo que permita al vendedor ingresar los datos manualmente.

3. Los datos retornados por Decoleta (`first_name`, `first_last_name`, `second_last_name`, `document_number`) deben mapearse correctamente al `ClienteResponseDTO` del sistema antes de enviarse al frontend.

**Subtareas:**
- Configurar `DecolectaReniecFeignClient` con URL base y token de autorización
- Implementar `GET /api/clientes/buscar?dni=` que consuma la API de Decoleta
- Implementar mapeo de `ResponseReniec` → `ClienteResponseDTO`
- Manejar errores de conexión con Feign (timeout, servicio no disponible)

---

### HU-006 — Consulta del catálogo de productos disponibles

**ID:** HU-006
**Módulo:** Ventas
**Responsable:** Christopher Vasquez

**Descripción:**
Como vendedor, quiero consultar el catálogo de productos con stock disponible para poder seleccionarlos al momento de registrar una venta, sin ver productos agotados que no puedo ofrecer al cliente.

**Criterios de aceptación:**

1. Al consultar el catálogo, el sistema debe retornar únicamente los productos cuyo stock sea mayor a cero.
   Los productos sin stock no deben aparecer en la lista del vendedor.

2. Cada producto en la respuesta debe mostrar su `id`, `nombre`, `precio` y `stock` disponible para que el
   vendedor pueda informar al cliente el precio y la cantidad máxima que puede adquirir.

3. Solo los usuarios autenticados con rol `VENDEDOR` o `ADMINISTRADOR` pueden acceder al catálogo. Un intento sin token válido debe retornar `401 Unauthorized`.

**Subtareas:**
- Implementar `GET /api/productos` que retorne solo productos con stock > 0
- Agregar filtro de stock en `ProductoAdapter` o en el `ProductoRepository`
- Verificar que el endpoint esté protegido con roles `VENDEDOR` y `ADMINISTRADOR`
- Retornar `ProductoDTO` con id, nombre, precio y stock

---

### HU-007 — Listar ventas realizadas con su estado de pago

**ID:** HU-007
**Módulo:** Ventas
**Responsable:** Christopher Vasquez

**Descripción:**
Como vendedor, quiero consultar el listado de ventas realizadas con su estado de pago para poder hacer seguimiento de las transacciones del día y 
verificar cuáles fueron exitosas o canceladas.

**Criterios de aceptación:**

1. El sistema debe retornar el listado de todas las ventas registradas con su `id`, nombre del cliente, total, estado de pago (`PENDIENTE`, `PAGADO`, `CANCELADO`), fecha y hora de la venta.

2. Las ventas deben mostrarse ordenadas de la más reciente a la más antigua para facilitar el seguimiento del vendedor.

3. Solo los usuarios autenticados con rol `VENDEDOR` o `ADMINISTRADOR` pueden acceder al listado de ventas.

**Subtareas:**
- Crear `VentaListadoDTO` con los campos necesarios para el listado
- Implementar `GET /api/ventas` en `VentaController`
- Implementar `listarVentasIn` en `VentaServiceIn` y `VentaServiceOut`
- Implementar `listarVentasOut` en `VentaAdapter` con ordenamiento por fecha descendente

---

### HU-008 — Consulta del historial de compras de un cliente por DNI

**ID:** HU-008
**Módulo:** Ventas
**Responsable:** Christopher Vasquez

**Descripción:**
Como vendedor, quiero consultar el historial de compras de un cliente ingresando su DNI para saber si ya ha comprado antes en la tienda y cuántas veces ha sido atendido.

**Criterios de aceptación:**

1. Al ingresar un DNI válido, el sistema debe buscar al cliente en la base de datos y retornar sus datos personales junto con el listado de sus ventas anteriores ordenadas por fecha descendente.

2. Si el cliente no existe en la base de datos (nunca ha comprado), el sistema debe retornar un mensaje claro indicando que no hay registros para ese DNI, sin lanzar un error `500`.

3. La respuesta debe incluir por cada venta: número de comprobante, tipo (boleta/factura), total, fecha y estado de pago.

**Subtareas:**
- Implementar `GET /api/clientes/{dni}/historial` en `ClienteController`
- Agregar `findByNumDoc` en `ClienteRepository` si no existe
- Crear `HistorialClienteDTO` con datos del cliente y lista de ventas
- Manejar el caso donde el cliente no existe retornando mensaje descriptivo

---

### HU-009 — Cierre de sesión del empleado

**ID:** HU-009
**Módulo:** Autenticación
**Responsable:** Christopher Vasquez

**Descripción:**
Como empleado, quiero cerrar sesión en el sistema para que mi token JWT quede invalidado y ninguna otra persona pueda usar mi sesión activa desde otro dispositivo.

**Criterios de aceptación:**

1. Al hacer logout, el sistema debe registrar el token del empleado en una lista negra (`blacklist`) para que no pueda ser reutilizado aunque no haya expirado.

2. Cualquier petición posterior que use un token en la lista negra debe retornar `401 Unauthorized` con el mensaje "Token inválido o sesión cerrada".

3. El endpoint de logout debe requerir un token válido en el header `Authorization`. Sin token no debe procesar el cierre de sesión.

**Subtareas:**
- Crear tabla `token_blacklist` en BD con campos `token` y `fecha_expiracion`
- Implementar `POST /api/auth/logout` que registre el token en la blacklist
- Modificar `JWTAuthenticationFilter` para verificar si el token está en la blacklist
- Limpiar periódicamente tokens expirados de la blacklist

---

### HU-010 — Renovación del access token con refresh token

**ID:** HU-010
**Módulo:** Autenticación
**Responsable:** Christopher Vasquez

**Descripción:**
Como empleado autenticado, quiero renovar mi access token usando el refresh token cuando el primero expire, para no tener que volver a hacer login completo con doble autenticación cada vez que el token caduca.

**Criterios de aceptación:**

1. Al enviar un refresh token válido al endpoint `/api/auth/refresh`, el sistema debe generar un nuevo access token con una nueva fecha de expiración sin requerir email ni contraseña.

2. Si el refresh token enviado es inválido, está expirado o no es de tipo `REFRESH`, el sistema debe retornar `401 Unauthorized` con un mensaje descriptivo del error.

3. El nuevo access token generado debe mantener los mismos datos del empleado (email, rol) que tenía el token anterior, sin necesidad de consultar nuevamente la base de datos de credenciales.

**Subtareas:**
- Implementar `POST /api/auth/refresh` que reciba el refresh token en el header
- Validar que el token sea de tipo `REFRESH` usando el claim `type`
- Validar que el token no esté expirado antes de generar el nuevo access token
- Retornar `SignInResponse` con el nuevo access token y el mismo refresh token
