Pruebas paso a paso (Lógica de Negocio) — Sistema de Gestión de Microcréditos
Este documento explica cómo probar, de forma manual y paso a paso (para principiantes), toda la lógica de negocio implementada en el servicio de Solicitudes de Crédito.

1. Requisitos antes de empezar
1.1 Tener la API corriendo
El proyecto está configurado en src/main/resources/application.properties para conectarse a PostgreSQL usando variables de entorno:

DB_URL
DB_USERNAME
DB_PASSWORD

Ejemplo de valores típicos:

DB_URL=jdbc:postgresql://localhost:5432/microcredito_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

Luego inicia la aplicación (desde la raíz del proyecto):
bash.\mvnw.cmd spring-boot:run
Cuando esté arriba, abre Swagger:

http://localhost:8080/swagger-ui.html
http://localhost:8080/api-docs

1.2 Endpoints que vas a usar
Clientes:

POST /api/clientes
GET /api/clientes/{id}

Productos de Crédito:

POST /api/productos
GET /api/productos/{id}
PUT /api/productos/{id}

Solicitudes de Crédito:

POST /api/solicitudes
GET /api/solicitudes/{id}
PATCH /api/solicitudes/{id}/estado/{estado}

Notas importantes:

Esta API usa entidades directamente (sin DTOs).
Para crear una solicitud, lo más simple es enviar los IDs dentro de producto y cliente (no necesitas enviar el objeto completo):

json  {
    "producto": { "id": 1 },
    "cliente": { "id": 2 }
  }

En los errores, si ves respuesta 500, es porque no hay un manejador de errores global; aun así, el mensaje suele incluir el motivo (por ejemplo: "El producto de crédito debe estar ACTIVO…").


2. Datos base (crear clientes y productos de crédito)
La idea es crear algunos registros "de prueba" para poder comprobar todas las reglas.
2.1 Crear un cliente con buen historial crediticio SIN garantía
Endpoint:

POST /api/clientes

Body:
json{
  "nombre": "Ana",
  "identificacion": "CC-1001",
  "edad": 25,
  "ingresoMensual": 1500000,
  "tieneGarantia": false,
  "historialCrediticio": "BUENO"
}
Guarda el id que devuelve la respuesta. En este documento lo llamaremos:

CLIENTE_SIN_GARANTIA_ID

2.2 Crear un cliente con buen historial crediticio CON garantía
Endpoint:

POST /api/clientes

Body:
json{
  "nombre": "Carlos",
  "identificacion": "CC-1002",
  "edad": 30,
  "ingresoMensual": 3000000,
  "tieneGarantia": true,
  "historialCrediticio": "BUENO"
}
Guarda el id:

CLIENTE_CON_GARANTIA_ID

2.3 Crear un cliente menor de 18 años (para forzar error)
Endpoint:

POST /api/clientes

Body (ejemplo con 17 años):
json{
  "nombre": "Luis",
  "identificacion": "CC-1003",
  "edad": 17,
  "ingresoMensual": 0,
  "tieneGarantia": false,
  "historialCrediticio": "SIN_HISTORIAL"
}
Guarda el id:

CLIENTE_MENOR_ID

2.4 Crear 3 productos de crédito ACTIVOS (2 de monto bajo/medio y 1 de monto alto)
Producto 1 (monto bajo, activo):

POST /api/productos

Body:
json{
  "nombre": "MicroCredi-Básico",
  "tipo": "CONSUMO",
  "montoMaximo": 1000000,
  "plazoMaximoMeses": 12,
  "tasaInteres": 2.5,
  "estado": "ACTIVO"
}
Guarda el id:

PRODUCTO_1_ID

Producto 2 (monto medio, activo):

POST /api/productos

Body:
json{
  "nombre": "MicroCredi-Emprendedor",
  "tipo": "PRODUCTIVO",
  "montoMaximo": 5000000,
  "plazoMaximoMeses": 24,
  "tasaInteres": 2.0,
  "estado": "ACTIVO"
}
Guarda el id:

PRODUCTO_2_ID

Producto 3 (monto alto, activo):

POST /api/productos

Body:
json{
  "nombre": "MicroCredi-Plus",
  "tipo": "PRODUCTIVO",
  "montoMaximo": 20000000,
  "plazoMaximoMeses": 48,
  "tasaInteres": 1.8,
  "estado": "ACTIVO"
}
Guarda el id:

PRODUCTO_ALTO_MONTO_ID


3. Regla 1 — Validar que el producto de crédito esté "ACTIVO" antes de crear la solicitud
Regla: Si el producto de crédito NO está ACTIVO, no se puede crear la solicitud.
3.1 Caso OK (producto ACTIVO)
Endpoint:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_1_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 800000
}
Qué verificar:

La respuesta devuelve una solicitud con estado en PENDIENTE.
El id de la solicitud existe (guárdalo como SOLICITUD_OK_1_ID).

3.2 Caso ERROR (producto NO ACTIVO)
Primero fuerza a que un producto quede SUSPENDIDO para simular que ya no está disponible.
Paso A: cambiar estado del Producto 2 a SUSPENDIDO:

PUT /api/productos/{id}

Usa {id} = PRODUCTO_2_ID y body:
json{
  "nombre": "MicroCredi-Emprendedor",
  "tipo": "PRODUCTIVO",
  "montoMaximo": 5000000,
  "plazoMaximoMeses": 24,
  "tasaInteres": 2.0,
  "estado": "SUSPENDIDO"
}
Paso B: intentar crear solicitud con ese producto:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_2_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 3000000
}
Resultado esperado:

Debe fallar.
Mensaje esperado (o similar): El producto de crédito debe estar ACTIVO para crear la solicitud.


4. Regla 2 — Verificar que el cliente sea mayor de 18 años
Regla: El cliente debe tener edad > 18.
4.1 Caso ERROR (edad menor de 18)
Endpoint:

POST /api/solicitudes

Body (usa un producto ACTIVO):
json{
  "producto": { "id": PRODUCTO_1_ID },
  "cliente": { "id": CLIENTE_MENOR_ID },
  "montoSolicitado": 500000
}
Resultado esperado:

Debe fallar.
Mensaje esperado: El cliente debe ser mayor de 18 años para solicitar un microcrédito.


5. Regla 3 — Impedir que un cliente tenga más de 2 solicitudes activas simultáneas
Definición práctica en esta API:

"Solicitudes activas" = solicitudes con estado = PENDIENTE para el mismo cliente.
Máximo permitido: 2.

5.1 Crear 2 solicitudes PENDIENTES (debe permitirlo)
Solicitud #1:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_1_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 800000
}
Guarda SOLICITUD_1_ID.
Solicitud #2:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_ALTO_MONTO_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 15000000
}
Guarda SOLICITUD_2_ID.

Si la segunda falla porque el producto de alto monto exige garantía, asegúrate de estar usando CLIENTE_CON_GARANTIA_ID (con garantía = true).

5.2 Intentar crear la solicitud #3 (debe fallar)
Primero crea un tercer producto ACTIVO para esta prueba (Producto 4).

POST /api/productos

Body:
json{
  "nombre": "MicroCredi-Rápido",
  "tipo": "CONSUMO",
  "montoMaximo": 2000000,
  "plazoMaximoMeses": 6,
  "tasaInteres": 3.0,
  "estado": "ACTIVO"
}
Guarda PRODUCTO_4_ID.
Ahora intenta crear la tercera solicitud:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_4_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 1500000
}
Resultado esperado:

Debe fallar.
Mensaje esperado: El cliente ya tiene 2 solicitudes activas. Debe esperar a que sean resueltas.

5.3 "Liberar cupo" cambiando el estado de una solicitud
Si cambias una solicitud de PENDIENTE a RECHAZADA, ya no cuenta como activa.
Paso A: rechazar SOLICITUD_1_ID:

PATCH /api/solicitudes/{id}/estado/{estado}

Usa {id} = SOLICITUD_1_ID y {estado} = RECHAZADA.
Paso B: intenta de nuevo crear la solicitud #3 (con PRODUCTO_4_ID).
Resultado esperado:

Ahora sí debe permitirla.


6. Regla 4 — Cambiar automáticamente el estado del producto a "EN_REVISION" al recibir una solicitud
Regla: Cuando se crea una solicitud, el producto pasa automáticamente de ACTIVO a EN_REVISION para ese cliente, indicando que está siendo evaluado.
6.1 Probar el cambio automático
Paso A: toma un producto que esté ACTIVO (por ejemplo PRODUCTO_4_ID recién creado).
Paso B: crea una solicitud:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_4_ID },
  "cliente": { "id": CLIENTE_SIN_GARANTIA_ID },
  "montoSolicitado": 1000000
}
Paso C: consulta el producto:

GET /api/productos/{id}

Usa {id} = PRODUCTO_4_ID.
Resultado esperado:

Debe aparecer estado: "EN_REVISION".


7. Regla 5 — Si el monto solicitado es "ALTO", validar que el cliente tenga garantía
Regla: Si montoSolicitado >= umbral_alto (por ejemplo, >= $10.000.000), entonces cliente.tieneGarantia debe ser true.
7.1 Caso ERROR (monto alto + cliente sin garantía)
Asegúrate de que PRODUCTO_ALTO_MONTO_ID esté ACTIVO. Si no lo está, créa otro producto de alto monto disponible o actualiza su estado.
Endpoint:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_ALTO_MONTO_ID },
  "cliente": { "id": CLIENTE_SIN_GARANTIA_ID },
  "montoSolicitado": 15000000
}
Resultado esperado:

Debe fallar.
Mensaje esperado: Para montos superiores a $10.000.000 el cliente debe presentar una garantía.

7.2 Caso OK (monto alto + cliente con garantía)
Endpoint:

POST /api/solicitudes

Body:
json{
  "producto": { "id": PRODUCTO_ALTO_MONTO_ID },
  "cliente": { "id": CLIENTE_CON_GARANTIA_ID },
  "montoSolicitado": 15000000
}
Resultado esperado:

Debe crear la solicitud.
El producto debe quedar en EN_REVISION automáticamente (verifícalo con GET /api/productos/{id}).


8. Resumen rápido de "qué debe pasar"
SituaciónResultadoProducto NO ACTIVONo crea la solicitudCliente menor de 18 añosNo crea la solicitudCliente con 2 solicitudes PENDIENTESNo permite una terceraSolicitud creada exitosamenteProducto cambia a EN_REVISIONMonto alto + cliente sin garantíaNo crea la solicitudSolicitud RECHAZADALibera el cupo del cliente
