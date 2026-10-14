# users-menagement-desktop-hezagonal-ddd
Ejemplo de una aplicación Desktop que hace CRUDL y control de acceso de Usuarios, escrita en Java,  arquitectura Hexagonal y enfoque DDD, usando buenas practicas y Clean code

---

## Parcial: microservicio **users** (Integrante 1)

Este servicio hace los **pasos 1, 2 y 5** del diagrama:

1. **Paso 1** – Crea / actualiza usuarios en TiDB (`POST /api/users`, `PUT /api/users/{id}`).
2. **Paso 2** – En vez de enviar el correo por SMTP, deja el mensaje en el tópico Kafka `notificaciones`
   (`KafkaEmailSenderAdapter`) con el formato acordado: `{ "email", "nombre", "asunto", "mensaje" }`.
3. **Paso 5** – Si el usuario es nuevo, llama (sync) a `POST {ACCOUNTS_URL}/api/accounts` con
   `{ "userId", "email", "nombre" }` (`AccountsRestClientAdapter`). Si accounts falla, el usuario
   igual queda creado y se registra un warning en el log.

Swagger: `/swagger-ui.html`

### Variables de entorno (Render)

| Variable | Ejemplo |
|---|---|
| `DB_HOST` | `gateway01.us-east-1.prod.aws.tidbcloud.com` |
| `DB_PORT` | `4000` |
| `DB_NAME` | `crud_usuarios` |
| `DB_USERNAME` | `xxxxxxxx.root` |
| `DB_PASSWORD` | *(TiDB)* |
| `DB_SSL_MODE` | `REQUIRED` (TiDB Cloud exige TLS) |
| `KAFKA_BOOTSTRAP_SERVERS` | `kafka-xxxx.aivencloud.com:12345` *(puerto SASL)* |
| `KAFKA_USERNAME` | `avnadmin` |
| `KAFKA_PASSWORD` | *(Aiven)* |
| `KAFKA_CA_CERT` | contenido completo de `ca.pem` |
| `ACCOUNTS_URL` | `https://accounts-xxxx.onrender.com` (sin `/` al final) |

### Despliegue en Render

New → Web Service → conectar este repositorio → **Runtime: Docker** (usa el `Dockerfile`) → variables de arriba → Deploy.
