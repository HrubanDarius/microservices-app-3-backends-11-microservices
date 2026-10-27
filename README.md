# ⚡ Energy Management System

Sistem distribuit de management al energiei bazat pe arhitectura **microservicii**, cu autentificare JWT, control al accesului pe roluri (RBAC) și interfață React.

---

## 📐 Arhitectura sistemului

```
┌──────────────┐        ┌────────────────────────────────────────────────┐
│   Browser    │◄──────►│              Traefik API Gateway               │
│  React SPA   │        │  (load balancer, routing, SSL termination)     │
└──────────────┘        └──┬──────────────┬─────────────────┬────────────┘
                           │              │                 │
                  /auth-service   /user-service      /device-service
                           │              │                 │
              ┌────────────▼─┐  ┌─────────▼──────┐  ┌──────▼──────────┐
              │    Auth      │  │     User        │  │    Device       │
              │ Microservice │  │  Microservice   │  │  Microservice   │
              │  :8084       │  │   :8080         │  │   :8081         │
              └──────┬───────┘  └────────┬────────┘  └───────┬─────────┘
                     │                   │                    │
              ┌──────▼──────┐   ┌────────▼───────┐  ┌────────▼────────┐
              │ credential_db│  │    user_db      │  │   device_db     │
              │ MySQL :3308  │  │  MySQL :3306    │  │  MySQL :3307    │
              └─────────────┘  └─────────────────┘  └─────────────────┘
```

---

## 🛠️ Tehnologii utilizate

| Layer | Tehnologie |
|---|---|
| Frontend | React 18, Bootstrap 5, Vite |
| Backend | Spring Boot 4.1.1, Java 25 |
| Autentificare | JJWT 0.12.5, BCrypt |
| Baze de date | MySQL 8 |
| API Gateway | Traefik v3 |
| Containerizare | Docker, Docker Compose |
| Documentație API | SpringDoc OpenAPI 2.8.5 (Swagger UI) |

---

## 📦 Structura proiectului

```
PROIECT/
├── frontend/                    # Aplicatie React (Vite + Bootstrap 5)
│   └── src/
│       ├── pages/               # LoginPage, AdminPage, ClientDevicesPage
│       ├── services/            # authService.js, api.js
│       └── components/          # Navbar, tabele, formulare
├── auth-microservice/           # Autentificare JWT (port 8084)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # AuthController
│       ├── services/            # AuthService
│       ├── security/            # JwtTokenProvider
│       └── entities/            # UserCredential, Role
├── user-microservice/           # Gestionare utilizatori (port 8080)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # PersonController
│       ├── services/            # PersonService
│       └── entities/            # Person
├── device-microservice/         # Gestionare dispozitive (port 8081)
│   └── src/main/java/ro/tuc/ds2020/
│       ├── controllers/         # DeviceController
│       ├── services/            # DeviceService
│       └── entities/            # Device
├── docker-compose.yml           # Orchestrare containere
└── .env                         # Variabile de mediu (nu se comite!)
```

---

## 🚀 Pornire rapidă

### Cerinte prealabile

- Docker Desktop instalat și pornit
- Git

### Clonare și pornire

```bash
git clone <url-repo>
cd PROIECT
docker compose up -d --build
```

Aplicatia va fi disponibila la **http://localhost** după ce toate containerele sunt sanatoase.

### Oprire

```bash
docker compose down
```

### Rebuild complet (cu stergerea volumelor)

```bash
docker compose down -v
docker compose up -d --build
```

---

## 🌐 Porturi si URL-uri

| Serviciu | URL direct | URL prin gateway |
|---|---|---|
| Frontend React | http://localhost | — |
| Auth Microservice | http://localhost:8084 | http://localhost/auth-service |
| User Microservice | http://localhost:8080 | http://localhost/user-service |
| Device Microservice | http://localhost:8081 | http://localhost/device-service |
| Traefik Dashboard | http://localhost:8090 | — |
| Auth DB (MySQL) | localhost:3308 | — |
| User DB (MySQL) | localhost:3306 | — |
| Device DB (MySQL) | localhost:3307 | — |

---

## 📖 Documentatie Swagger / OpenAPI

Fiecare microserviciu expune o interfata Swagger UI interactiva. Apasa butonul **Authorize** (🔒) si introdu token-ul JWT obtinut la login pentru a testa rutele protejate.

| Microserviciu | Swagger UI |
|---|---|
| Auth Service | [http://localhost/auth-service/swagger-ui/index.html](http://localhost/auth-service/swagger-ui/index.html) |
| User Service | [http://localhost/user-service/swagger-ui/index.html](http://localhost/user-service/swagger-ui/index.html) |
| Device Service | [http://localhost/device-service/swagger-ui/index.html](http://localhost/device-service/swagger-ui/index.html) |

### Endpoint-uri principale

#### 🔐 Auth Service (`/auth-service/auth`)

| Metoda | Ruta | Descriere |
|---|---|---|
| `POST` | `/auth/register` | Inregistreaza un cont nou; returneaza JWT |
| `POST` | `/auth/login` | Autentificare cu username/parola; returneaza JWT |
| `GET` | `/auth/validate` | Valideaza un token JWT |

#### 👤 User Service (`/user-service/person`)

| Metoda | Ruta | Descriere | Rol necesar |
|---|---|---|---|
| `GET` | `/person` | Lista tuturor utilizatorilor | ADMIN |
| `POST` | `/person` | Creeaza utilizator nou | ADMIN |
| `GET` | `/person/{id}` | Detalii utilizator dupa UUID | ADMIN / CLIENT (propriu) |
| `PUT` | `/person/{id}` | Actualizeaza utilizator | ADMIN |
| `DELETE` | `/person/{id}` | Sterge utilizator | ADMIN |

#### ⚡ Device Service (`/device-service/device`)

| Metoda | Ruta | Descriere | Rol necesar |
|---|---|---|---|
| `GET` | `/device` | Lista tuturor dispozitivelor | ADMIN |
| `POST` | `/device` | Adauga dispozitiv nou | ADMIN |
| `GET` | `/device/{id}` | Detalii dispozitiv dupa UUID | ADMIN |
| `PUT` | `/device/{id}` | Actualizeaza dispozitiv | ADMIN |
| `DELETE` | `/device/{id}` | Sterge dispozitiv | ADMIN |
| `GET` | `/device/user/{userId}` | Dispozitivele unui client | ADMIN / CLIENT (propriu) |
| `DELETE` | `/device/user/{userId}` | Sterge dispozitivele unui client | ADMIN |

---

## 👥 Conturi demo

> [!IMPORTANT]
> Conturile demo sunt create automat la prima pornire prin `DatabaseSeeder`. Nu este nevoie de configurare manuala.

| Rol | Username | Parola | Permisiuni |
|---|---|---|---|
| **ADMIN** | `admin` | `admin123` | CRUD complet: utilizatori, dispozitive, alocare |
| **CLIENT** | `client` | `client123` | Vizualizare exclusiva a propriilor dispozitive si profil |

---

## 🔒 Flux de autentificare (JWT)

```
1. Utilizatorul trimite POST /auth-service/auth/login { username, password }
2. Auth Service verifica parola cu BCrypt
3. Se genereaza un JWT semnat (HS256) cu: userId, username, role, expirare 24h
4. Frontend salveaza token-ul in localStorage
5. Toate cererile ulterioare includ: Authorization: Bearer <token>
6. Microserviciile valideaza token-ul la nevoie prin /auth-service/auth/validate
```

### Payload JWT (claims)

```json
{
  "sub": "username",
  "userId": "uuid-al-utilizatorului",
  "role": "ADMIN | CLIENT",
  "iat": 1234567890,
  "exp": 1234567890
}
```

---

## 🎭 Roluri și permisiuni (RBAC)

| Actiune | ADMIN | CLIENT |
|---|:---:|:---:|
| Vizualizare toti utilizatorii | ✅ | ❌ |
| Creare / Editare / Stergere utilizatori | ✅ | ❌ |
| Vizualizare toate dispozitivele | ✅ | ❌ |
| Creare / Editare / Stergere dispozitive | ✅ | ❌ |
| Alocare dispozitiv catre un client | ✅ | ❌ |
| Vizualizare propriile dispozitive | ✅ | ✅ |
| Vizualizare profil propriu | ✅ | ✅ |

---

## 🗄️ Baze de date

### `credential_db` (Auth Service)

| Coloana | Tip | Descriere |
|---|---|---|
| `id` | `VARCHAR(36)` | UUID primar |
| `username` | `VARCHAR(255)` | Unic |
| `password` | `VARCHAR(255)` | Hash BCrypt |
| `role` | `ENUM('ADMIN','CLIENT')` | Rolul utilizatorului |

### `user_db` (User Service)

| Coloana | Tip | Descriere |
|---|---|---|
| `id` | `VARCHAR(36)` | UUID — **identic** cu cel din `credential_db` |
| `name` | `VARCHAR(255)` | Nume complet |
| `address` | `VARCHAR(255)` | Adresa |
| `age` | `INT` | Varsta |

### `device_db` (Device Service)

| Coloana | Tip | Descriere |
|---|---|---|
| `id` | `VARCHAR(36)` | UUID primar |
| `description` | `VARCHAR(255)` | Descriere dispozitiv |
| `address` | `VARCHAR(255)` | Adresa fizica |
| `max_hourly_energy_consumption` | `DOUBLE` | Consum maxim orar (kWh) |
| `user_id` | `VARCHAR(36)` | FK catre ID-ul clientului din `user_db` |

> [!NOTE]
> UUID-ul unui utilizator este **sincronizat** intre `credential_db` si `user_db`. La inregistrare, auth-service poate primi un UUID predefinit prin campul optional `id` din `RegisterRequestDTO`.

---

## ⚙️ Variabile de mediu (`.env`)

Creeaza un fisier `.env` in radacina proiectului:

```env
# Auth DB
AUTH_DB_PORT=3308
AUTH_DB_NAME=credential_db
AUTH_DB_USER=root
AUTH_DB_PASSWORD=root
AUTH_SERVICE_PORT=8084

# User DB
USER_DB_PORT=3306
USER_DB_NAME=user_db
USER_DB_USER=root
USER_DB_PASSWORD=root
USER_SERVICE_PORT=8080

# Device DB
DEVICE_DB_PORT=3307
DEVICE_DB_NAME=device_db
DEVICE_DB_USER=root
DEVICE_DB_PASSWORD=root
DEVICE_SERVICE_PORT=8081
```

---

## 🧪 Testare

Fiecare microserviciu include teste unitare si de integrare (JUnit 5 + Mockito):

```bash
# Ruleaza testele pentru un microserviciu
cd user-microservice
mvn test

# Ruleaza testele pentru toate microserviciile
cd PROIECT
for dir in auth-microservice user-microservice device-microservice; do
  (cd $dir && mvn test)
done
```

Rapoartele JaCoCo sunt generate in `target/site/jacoco/index.html`.

---

## 📋 Checkstyle

Proiectul foloseste **Checkstyle 13.9.0** (compatibil Java 25) cu reguli stricte:

- Parametri `final` obligatorii
- Javadoc obligatoriu pe metode publice
- Fara importuri neutilizate
- `EqualsHashCode` — implementare obligatorie a ambelor metode

```bash
mvn checkstyle:check
```

---

*Proiect realizat pentru cursul de Sisteme Distribuite*
