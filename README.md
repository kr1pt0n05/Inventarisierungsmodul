# Inventarisierungsmodul (InSy)

Inventarisierungsmodul zur Bestellsoftware für die Hochschule Esslingen in Kooperation mit KEIM.

## Live-Demo

Eine Live-Demo ist verfügbar unter: https://insy.lind3.de

- **Benutzername:** `demo@hs-esslingen.de`
- **Passwort:** `4dx\("&}+H*em/p5[G0Yx.9fW$SiK+H]`

## Übersicht

Dieses Projekt besteht aus einem modularen Backend (Spring Boot, Java) und einem modernen Frontend (Angular), um die Inventarisierung und Verwaltung von Beständen effizient zu unterstützen. Es bietet Funktionen wie das Anlegen, Bearbeiten, Kommentieren, Taggen und Erweitern von Inventargegenständen sowie die Integration mit Bestell- und Benutzerverwaltung.

### Technologie-Stack

**Backend:**
- Java 17
- Spring Boot 3.4.6
- Spring Security mit OAuth2 (Keycloak)
- Spring Data JPA
- PostgreSQL
- Maven
- Docker

**Frontend:**
- Angular 21.1.1
- TypeScript
- Angular Material
- Tailwind CSS 4
- Chart.js
- OAuth2 OIDC Client
- Nginx (Production)

## Projektstruktur

```
InSy/
│
├── backend/         # Spring Boot Backend (Java 17, Spring Boot 3.4.6)
│   ├── src/         # Quellcode
│   ├── Dockerfile   # Multi-stage Docker Build
│   ├── docker-compose.yml          # Production Setup
│   ├── dev-db.docker-compose.yml   # Development Datenbank
│   └── README.md    # Backend-spezifische Dokumentation
│
├── frontend/        # Angular Frontend (Angular 21, TypeScript)
│   ├── src/         # Quellcode
│   ├── public/      # Statische Assets
│   ├── Dockerfile   # Multi-stage Docker Build
│   ├── docker-compose.yml  # Frontend Container Setup
│   └── README.md    # Frontend-spezifische Dokumentation
│
├── Documentation/   # Architektur, API und Datenmodell
│   ├── API-Documentation.yaml
│   └── Datamodel_v2.svg
│
├── keycloak/        # Keycloak-Konfiguration für Authentifizierung
│   ├── docker-compose.yml
│   └── Keycloak.md
│
└── README.md        # Diese Datei
```

## Features

- **Inventarisierung**: Anlegen, Bearbeiten und Verwalten von Inventargegenständen
- **Erweiterungen**: Verwaltung von Hardware-Erweiterungen zu Inventargegenständen
- **Kommentare & Tags**: Kommentieren und kategorisches Taggen von Inventargegenständen
- **Bestellintegration**: Verknüpfung von Inventargegenständen mit Bestellungen aus BeSy
- **Statistiken**: Auswertungen und Visualisierungen des Inventarbestands
- **Benutzerverwaltung**: OAuth2-basierte Authentifizierung und Autorisierung mit Keycloak
- **CSV-Import/Export**: Massenimport und Export von Inventardaten
- **Änderungshistorie**: Versionierung und Nachverfolgung von Änderungen (Javers)
- **REST API**: Vollständig dokumentiert in OpenAPI/Swagger Format

## Voraussetzungen

### Für lokale Entwicklung

**Backend:**
- Java JDK 17 oder höher
- Maven 3.9.x
- PostgreSQL 12+ (oder Docker)
- Git

**Frontend:**
- Node.js v18+ (empfohlen: v22)
- npm oder yarn
- Angular CLI (`npm install -g @angular/cli`)

**Optional:**
- Docker & Docker Compose (für containerisiertes Setup)
- Keycloak (für Authentifizierung)

### Für Production Deployment

- Docker & Docker Compose
- Traefik oder anderer Reverse Proxy (für HTTPS)
- PostgreSQL Datenbank
- Keycloak Server

## Inbetriebnahme

### 1. Repository klonen

```bash
git clone https://github.com/kr1pt0n05/Inventarisierungsmodul InSy
cd InSy
```

### 2. Entwicklungsumgebung einrichten

#### Backend Setup

1. **Backend konfigurieren**:
   - `.env.example` kopieren und anpasssen:
   ```bash
   cd backend
   cp .env.example .env
   ```
   - `.env` Datei im `backend/` Verzeichnis bearbeiten:
     ```env
     POSTGRES_USER=insy_user
     POSTGRES_PASSWORD=ihr_passwort
     POSTGRES_DB=insy
     POSTGRES_HOST=localhost
     POSTGRES_PORT=5432
     PORT=8080
     ISSUER_URI=https://auth.insy.hs-esslingen.com/realms/insy
     INSY_REQUIRED_ROLE=inventory-manager
     ALLOWED_ORIGIN=http://localhost:4200
     ```

2. **Datenbank starten** (mit Docker):
   ```bash
   docker compose -f dev-db.docker-compose.yml up -d
   ```
   Dies startet:
   - PostgreSQL auf Port 5432
   - PgAdmin auf Port 5050 (http://localhost:5050)

3. **Backend starten**:
   ```bash
   # Mit Maven
   ./mvnw spring-boot:run

   # Oder mit Maven Wrapper (Windows)
   mvnw.cmd spring-boot:run
   ```

   Backend läuft auf: http://localhost:8080

4. **API testen**:
   ```bash
   curl http://localhost:8080
   ```

#### Frontend Setup

1. **Dependencies installieren**:
   ```bash
   cd frontend
   npm install
   ```

2. **Environment konfigurieren**:
   - Umgebungsdatei `src/environments/environment.development.ts` prüfen
   - Backend-URL und Keycloak-Einstellungen anpassen

3. **Development Server starten**:
   ```bash
   npm start
   # Oder
   ng serve
   ```

   Frontend läuft auf: http://localhost:4200

#### Keycloak Setup (Optional für lokale Entwicklung)

Siehe Keycloak Dokumentation für detailliertere Einrichtung.

1. **Keycloak starten**:
   ```bash
   cd keycloak
   docker compose up -d
   ```

2. **Keycloak konfigurieren**:
   - Details siehe [keycloak/Keycloak.md](keycloak/Keycloak.md)
   - Realm "insy" erstellen
   - Client "insy" mit korrekten Redirect URIs konfigurieren
   - Rolle "inventory-manager" anlegen

### 3. Production Deployment mit Docker

#### Variante A: Komplettes Setup mit Docker Compose

1. **Environment-Variablen setzen**:
   ```bash
   # Backend .env
   cd backend
   cp .env.example .env
   # Anpassen für Production (externe URLs, sichere Passwörter)
   ```

2. **Images bauen**:
   ```bash
   # Backend
   cd backend
   docker build -t insy-backend:latest .

   # Frontend
   cd ../frontend
   docker build --build-arg PROFILE=production -t insy-frontend:latest .
   ```

3. **Services starten**:
   ```bash
   # Backend mit Datenbank
   cd backend
   docker compose up -d

   # Frontend
   cd ../frontend
   docker compose up -d
   ```

#### Variante B: Separates Deployment

**Backend:**
```bash
cd backend
# Mit externer Datenbank
docker run -d \
  --name insy-backend \
  -p 8080:8080 \
  --env-file .env \
  -e POSTGRES_HOST=db.example.com \
  insy-backend:latest
```

**Frontend:**
```bash
cd frontend
docker run -d \
  --name insy-frontend \
  -p 80:80 \
  insy-frontend:latest
```

#### Variante C: Mit Container Registry

1. **Images taggen und pushen**:
   ```bash
   # Backend
   docker tag insy-backend:latest ghcr.io/ihr-org/insy-backend:latest
   docker push ghcr.io/ihr-org/insy-backend:latest

   # Frontend
   docker tag insy-frontend:latest ghcr.io/ihr-org/insy-frontend:latest
   docker push ghcr.io/ihr-org/insy-frontend:latest
   ```

2. **Auf dem Server deployen**:
   ```bash
   docker pull ghcr.io/ihr-org/insy-backend:latest
   docker pull ghcr.io/ihr-org/insy-frontend:latest
   # Mit docker-compose.yml starten
   ```

### 4. Production Checkliste

- [ ] PostgreSQL Datenbank mit Backups eingerichtet
- [ ] Keycloak Server konfiguriert und erreichbar
- [ ] Environment-Variablen für Production gesetzt
- [ ] HTTPS mit gültigem SSL-Zertifikat (Traefik/Let's Encrypt)
- [ ] CORS-Einstellungen für Production-Domain konfiguriert
- [ ] Firewall-Regeln (nur Port 443 öffentlich)
- [ ] Monitoring und Logging eingerichtet
- [ ] Backup-Strategie implementiert
- [ ] Health Checks konfiguriert
- [ ] Database Migrations getestet

## Entwicklung

### Backend

Detaillierte Informationen siehe [backend/README.md](backend/README.md)

**Wichtige Befehle:**
```bash
# Bauen
./mvnw clean install

# Tests ausführen
./mvnw test

# Mit Development-Profil starten
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Package erstellen
./mvnw clean package
```

**Projektstruktur:**
- `controller/` - REST Endpoints
- `service/` - Business Logic
- `repository/` - Datenzugriff
- `model/` - JPA Entities
- `dto/` - Data Transfer Objects
- `security/` - Security Configuration
- `configuration/` - Spring Configuration

### Frontend

Detaillierte Informationen siehe [frontend/README.md](frontend/README.md)

**Wichtige Befehle:**
```bash
# Entwicklungsserver
npm start

# Production Build
npm run build

# Tests
npm test

# Component generieren
ng generate component components/mein-component
```

**Projektstruktur:**
- `components/` - Wiederverwendbare UI-Komponenten
- `pages/` - Seiten-Komponenten
- `services/` - API-Services und Business Logic
- `models/` - TypeScript Interfaces
- `resolver/` - Route Resolver

## Tests

### Backend Tests

```bash
cd backend
./mvnw test

# Mit Coverage Report
./mvnw test jacoco:report
```

### Frontend Tests

```bash
cd frontend
# Unit Tests
ng test

# CI Tests (Headless)
npm run test:ci

# E2E Tests
ng e2e
```

## API Dokumentation

Die REST API ist vollständig dokumentiert:

- **OpenAPI/Swagger Spec**: [Documentation/API-Documentation.yaml](Documentation/API-Documentation.yaml)
- **JSON Format**: [Documentation/swagger.json](Documentation/swagger.json)

**Swagger UI** (wenn aktiviert): http://localhost:8080/swagger-ui.html

### Beispiel API Endpunkte

```
GET    /api/inventories              - Liste aller Inventargegenstände
GET    /api/inventories/{id}         - Details eines Inventargegenstands
POST   /api/inventories              - Neuen Inventargegenstand anlegen
PUT    /api/inventories/{id}         - Inventargegenstand aktualisieren
DELETE /api/inventories/{id}         - Inventargegenstand löschen
GET    /api/inventories/{id}/history - Änderungshistorie
POST   /api/inventories/import       - CSV Import
GET    /api/statistics               - Statistiken
```

## Datenmodell

Das vollständige Datenmodell ist visualisiert in:
- [Documentation/Datamodel_v2.svg](Documentation/Datamodel_v2.svg)

**Hauptentitäten:**
- **Inventory** - Inventargegenstände
- **Article** - Artikel/Produkte
- **Extension** - Hardware-Erweiterungen
- **Comment** - Kommentare
- **Tag** - Kategorisierungs-Tags
- **Company** - Firmen/Hersteller
- **CostCenter** - Kostenstellen
- **Order** - Bestellungen

## Fehlerbehebung

### Backend startet nicht

```bash
# Datenbankverbindung prüfen
psql -h localhost -U insy_user -d insy

# Logs prüfen
./mvnw spring-boot:run --debug

# Port bereits belegt
netstat -ano | findstr :8080
```

### Frontend Build Fehler

```bash
# Node Modules neu installieren
rm -rf node_modules package-lock.json
npm install

# Cache löschen
npm cache clean --force
```

### Docker Container Probleme

```bash
# Logs anzeigen
docker compose logs -f

# Container neu starten
docker compose down
docker compose up -d

# Volumes löschen (Achtung: Datenverlust!)
docker compose down -v
```

### Keycloak Authentifizierung schlägt fehl

- ISSUER_URI in Backend-Konfiguration prüfen
- Keycloak Client Redirect URIs prüfen
- Token-Gültigkeit und Rollen prüfen
- Browser-Console auf CORS-Fehler prüfen

## Dokumentation

- **API-Dokumentation**: [Documentation/API-Documentation.yaml](Documentation/API-Documentation.yaml)
- **Datenmodell**: [Documentation/Datamodel_v2.svg](Documentation/Datamodel_v2.svg)
- **Architektur & Meilensteine**: [Documentation/Doku_Inventarisierungsmodul_Meilenstein2.pdf](Documentation/Doku_Inventarisierungsmodul_Meilenstein2.pdf)
- **Backend Details**: [backend/README.md](backend/README.md)
- **Frontend Details**: [frontend/README.md](frontend/README.md)
- **Keycloak Setup**: [keycloak/Keycloak.md](keycloak/Keycloak.md)

## Mitwirken

Pull Requests und Issues sind willkommen!

### Development Workflow

1. Branch erstellen: `git checkout -b feature/mein-feature`
2. Änderungen committen: `git commit -am 'Feature hinzugefügt'`
3. Tests ausführen: `./mvnw test && npm test`
4. Push: `git push origin feature/mein-feature`
5. Pull Request erstellen

### Code-Standards

- **Backend**: Java Code Conventions, Spring Best Practices
- **Frontend**: Angular Style Guide, TypeScript Strict Mode
- **Commits**: Conventional Commits Format

## Lizenz

Siehe [LICENSE](LICENSE).

## Support & Kontakt

**Hochschule Esslingen**
- Fakultät Informatik
- In Kooperation mit KEIM

Für Fragen und Support:
- Issues auf GitHub erstellen
- Dokumentation konsultieren
- Team kontaktieren

---

**Version**: 1.1.0
**Letztes Update**: Januar 2026
