# ⚡ Energy Management System

Sistem distribuit de management al energiei bazat pe arhitectura **microservicii**, cu autentificare JWT securizată la nivel de **API Gateway (Traefik ForwardAuth)**, control al accesului pe roluri (RBAC) și interfață React.

---

## 📐 Arhitectura sistemului și Securitatea la nivel de Gateway

```
┌──────────────┐
│   Browser    │
│  React SPA   │
└──────┬───────┘
       │ HTTP / HTTPS (Port 80)
       ▼
┌────────────────────────────────────────────────────────────────────────┐
│                      Traefik v3 API Gateway                            │
│                                                                        │
│   [Router Public]           [ForwardAuth Middleware: jwt-auth]         │
│   • / (Frontend)                 │ Interceptează cererea               │
│   • /auth-service/*              │ Forwardează 'Authorization: Bearer' │
│   • /*/swagger-ui/*              ▼                                     │
│   • /*/v3/api-docs/*      ┌──────────────┐                             │
│                           │ auth-service │  Token Valid (HTTP 200)     │
│   [Router Protejat]       │  /validate   ├─────────────────────┐       │
│   • /user-service/* ◄─────┴──────────────┘                     │       │
│   • /device-service/*     Token Invalid / Lipsă (HTTP 401)     │       │
│                           Blochează cererea la Gateway!        ▼       │
└────────────────────────────────────────┬───────────────────────┬───────┘
                                         │                       │
                                /user-service             /device-service
                                         │                       │
                            ┌────────────▼──────┐   ┌────────────▼──────┐
                            │  User Service     │   │  Device Service   │
                            │   (2 replici)     │   │   (2 replici)     │
                            │      :8080        │   │      :8081        │
                            └────────┬──────────┘   └────────┬──────────┘
                                     │                       │
                            ┌────────▼─────────┐    ┌────────▼─────────┐
                            │    user_db       │    │   device_db      │
                            │  MySQL :3306     │    │  MySQL :3307     │
                            └──────────────────┘    └──────────────────┘
```

> [!IMPORTANT]
> **Cheia secretă JWT (`jwt.secret`) se află EXCLUSIV într-un singur loc:** în `auth-microservice`.
> Traefik folosește mecanismul nativ și gratuit **ForwardAuth** pentru a valida fiecare cerere înainte de a o lăsa să ajungă la `user-service` sau `device-service`. Astfel:
> 1. Nu este nevoie de licență plătită Traefik Enterprise (care are modulul proprietar de JWT).
> 2. Nu este nevoie de plugin-uri externe instabile de pe GitHub.
> 3. Microserviciile din spate nu cunosc și nu dețin cheia secretă — protecția se face direct în gateway!

---

## 🛠️ Tehnologii utilizate

| Layer | Tehnologie |
|---|---|
| Frontend | React 18, Bootstrap 5, Vite |
| Backend | Spring Boot 4.1.1, Java 25 |
| Securitate Gateway | Traefik ForwardAuth + JJWT 0.12.5 + BCrypt |
| Baze de date | MySQL 8.4 (containere izolate pentru fiecare microserviciu) |
| API Gateway | Traefik v3.2 |
| Containerizare | Docker, Docker Compose (cu replicare și load balancing) |
| Documentație API | SpringDoc OpenAPI 2.8.5 (Swagger UI cu suport Bearer JWT) |

---

## 📦 Structura proiectului

```
PROIECT/
├── frontend/                    # Aplicatie React (Vite + Bootstrap 5)
│   └── src/
│       ├── pages/               # LoginPage, UsersPage, DevicesPage, ClientDevicesPage
│       ├── services/            # authService.js, userService.js, deviceService.js
│       └── components/          # Navbar, tabele, formulare
├── auth-microservice/           # Autentificare JWT & Validare Gateway (port 8082)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # AuthController (POST /login, POST /register, ANY /validate)
│       ├── services/            # AuthService
│       ├── security/            # JwtTokenProvider (deține jwt.secret)
│       └── entities/            # UserCredential, Role
├── user-microservice/           # Gestionare utilizatori (port 8080, 2 replici)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # PersonController
│       ├── services/            # PersonService, DeviceServiceClient
│       └── entities/            # Person
├── device-microservice/         # Gestionare dispozitive (port 8081, 2 replici)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # DeviceController, UserReferenceController
│       ├── services/            # DeviceService, UserReferenceService
│       └── entities/            # Device, UserReference
├── docker-compose.yml           # Orchestrare Traefik, baze de date, replici microservicii
└── .env                         # Variabile de mediu
```

---

## 🚀 Pornire rapidă

### Cerinte prealabile

- Docker Desktop instalat și pornit
- Git

### Pornire / Rebuild

```bash
docker compose down -v
docker compose up -d --build
```

Aplicația va fi disponibilă la **http://localhost** după inițializarea tuturor containerelor.

### Oprire

```bash
docker compose down
```

---

## 🌐 Porturi și URL-uri

| Serviciu | URL direct | URL prin Gateway (Port 80) |
|---|---|---|
| Frontend React | http://localhost | http://localhost/ |
| Auth Service | http://localhost:8082 | http://localhost/auth-service |
| User Service | http://localhost:8080 | http://localhost/user-service |
| Device Service | http://localhost:8081 | http://localhost/device-service |
| Traefik Dashboard | http://localhost:8082 | — |
| Auth DB (MySQL) | localhost:3308 | — |
| User DB (MySQL) | localhost:3306 | — |
| Device DB (MySQL) | localhost:3307 | — |

---

## 📖 Documentatie Swagger / OpenAPI

Fiecare microserviciu expune propria interfață Swagger UI. Rutele de documentație (`/swagger-ui/*` și `/v3/api-docs/*`) sunt configurate ca publice în Traefik, permițând deschiderea paginii fără token. Endpoint-urile de date sunt însă **protejate de gateway**.

| Microserviciu | Link Swagger UI |
|---|---|
| **Auth Service** | [http://localhost/auth-service/swagger-ui/index.html](http://localhost/auth-service/swagger-ui/index.html) |
| **User Service** | [http://localhost/user-service/swagger-ui/index.html](http://localhost/user-service/swagger-ui/index.html) |
| **Device Service** | [http://localhost/device-service/swagger-ui/index.html](http://localhost/device-service/swagger-ui/index.html) |

---

## 🧪 Cum se testează tot proiectul în Swagger (Ghid pas cu pas)

Pentru a testa fluxul complet de securitate și funcționalitate folosind Swagger UI:

### Pasul 1: Obținerea token-ului JWT din Swagger Auth

1. Deschide **Auth Swagger**: [http://localhost/auth-service/swagger-ui/index.html](http://localhost/auth-service/swagger-ui/index.html).
2. Expandează endpoint-ul `POST /auth/login`.
3. Apasă **Try it out**.
4. În Request Body introdu credențialele contului de Admin:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
5. Apasă **Execute**.
6. În răspuns (HTTP 200), copiază valoarea câmpului `"token"` (șirul lung care începe cu `eyJ...`).

---

### Pasul 2: Testarea securității Gateway în Swagger User / Device

#### A. Testare FĂRĂ Token (Demonstrarea blocării la Gateway):
1. Deschide **User Swagger**: [http://localhost/user-service/swagger-ui/index.html](http://localhost/user-service/swagger-ui/index.html).
2. Expandează `GET /person` și apasă **Try it out** -> **Execute** (fără să te autentifici).
3. **Rezultat:** Vei primi **HTTP 401 Unauthorized**. Cererea a fost oprită direct de Traefik!

#### B. Testare CU Token (Demonstrarea autorizării la Gateway):
1. În colțul din dreapta-sus al paginii User Swagger, apasă pe butonul verde **Authorize** (cu lăcățel).
2. În fereastra apărută, la câmpul **Value**, lipește token-ul copiat la Pasul 1:
   ```
   eyJhbGciOiJIUzI1NiJ9...
   ```
3. Apasă butonul **Authorize**, apoi **Close**.
4. Acum reapelează `GET /person` -> **Execute**.
5. **Rezultat:** Vei primi **HTTP 200 OK** cu lista tuturor utilizatorilor din baza de date!

---

### Pasul 3: Testarea operațiilor în Swagger Device

1. Deschide **Device Swagger**: [http://localhost/device-service/swagger-ui/index.html](http://localhost/device-service/swagger-ui/index.html).
2. Apasă butonul verde **Authorize** și lipește același token JWT.
3. Testează `GET /device` -> **HTTP 200 OK** cu lista dispozitivelor.
4. Testează `POST /device` pentru a adăuga un dispozitiv nou legat de ID-ul unui client.
5. Testează `GET /device/user/{userId}` pentru a vedea dispozitivele acelui utilizator.

---

## 🔍 Cum se testează validarea Gateway din Terminal (PowerShell / curl)

> [!NOTE]
> În Windows PowerShell, comanda `curl` este un alias pentru `Invoke-WebRequest` (unde `-i` înseamnă `-InFile`). Pentru a folosi utilitarul nativ curl, scrie întotdeauna **`curl.exe`**!

### 1. Cerere fără token (Trebuie să returneze HTTP 401 Unauthorized direct din Traefik):
```powershell
curl.exe -i http://localhost/user-service/person
```
*Răspuns primit:* `HTTP/1.1 401 Unauthorized`

### 2. Cerere cu token invalid (Trebuie să returneze HTTP 401 Unauthorized):
```powershell
curl.exe -i -H "Authorization: Bearer token_invalid_123" http://localhost/user-service/person
```
*Răspuns primit:* `HTTP/1.1 401 Unauthorized`

### 3. Cerere cu token valid de la login:
```powershell
# Obtine token-ul
$loginRes = Invoke-RestMethod -Method Post -Uri "http://localhost/auth-service/auth/login" -ContentType "application/json" -Body '{"username":"admin","password":"admin123"}'
$token = $loginRes.token

# Apeleaza API-ul protejat cu token (cu curl.exe)
curl.exe -i -H "Authorization: Bearer $token" http://localhost/user-service/person

# Sau folosind comanda nativa PowerShell:
Invoke-RestMethod -Uri "http://localhost/user-service/person" -Headers @{ Authorization = "Bearer $token" }
```
*Răspuns primit:* `HTTP/1.1 200 OK` cu lista de utilizatori!

---

## 👥 Conturi demo predefinite

Conturile demo sunt create automat la prima pornire prin `DatabaseSeeder`:

| Rol | Username | Parola | Permisiuni |
|---|---|---|---|
| **ADMIN** | `admin` | `admin123` | CRUD complet: utilizatori, dispozitive, alocare |
| **CLIENT** | `client` | `client123` | Vizualizare exclusivă a propriilor dispozitive și profil |

---

## 🎭 Roluri și permisiuni (RBAC)

| Acțiune | ADMIN | CLIENT |
|---|:---:|:---:|
| Vizualizare toți utilizatorii | ✅ | ❌ |
| Creare / Editare / Ștergere utilizatori | ✅ | ❌ |
| Vizualizare toate dispozitivele | ✅ | ❌ |
| Creare / Editare / Ștergere dispozitive | ✅ | ❌ |
| Alocare dispozitiv către un client | ✅ | ❌ |
| Vizualizare propriile dispozitive | ✅ | ✅ |
| Vizualizare profil propriu | ✅ | ✅ |

---

## 🗄️ Baze de date

### `credential_db` (Auth Service - Port 3308)
- `id`: UUID primar (sincronizat cu cel din `user_db`)
- `username`: Unic
- `password`: Hash securizat cu algoritm BCrypt
- `role`: `ADMIN` sau `CLIENT`

### `user_db` (User Service - Port 3306)
- `id`: UUID primar (același ID ca în `credential_db`)
- `name`: Numele utilizatorului
- `address`: Adresa fizică
- `age`: Vârsta

### `device_db` (Device Service - Port 3307)
- `id`: UUID primar
- `description`: Descriere dispozitiv
- `address`: Adresa instalării
- `max_hourly_energy_consumption`: Consum maxim orar (kWh)
- `user_id`: UUID-ul clientului proprietar (referință sincronizată)

---

## 📋 Checkstyle & Calitatea Codului

Proiectul folosește **Checkstyle 13.9.0** (compatibil Java 25) cu reguli stricte:
- Toți parametrii metodelor sunt marcați `final`.
- Metodele publice au documentație Javadoc completă.
- Fără importuri nefolosite.
- `EqualsHashCode` implementat pe entități.

---

*Proiect realizat pentru cursul de Sisteme Distribuite*
