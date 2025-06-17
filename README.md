# Inventarisierungsmodul

Inventarisierungsmodul zur Bestellsoftware für die Hochschule Esslingen in Kooperation mit KEIM.


## Übersicht

Dieses Projekt besteht aus einem modularen Backend (Spring Boot, Java) und einem modernen Frontend (Angular), um die Inventarisierung und Verwaltung von Beständen effizient zu unterstützen. Es bietet Funktionen wie das Anlegen, Bearbeiten, Kommentieren, Taggen und Erweitern von Inventargegenständen sowie die Integration mit Bestell- und Benutzerverwaltung.

## Projektstruktur

```
Inventarisierungsmodul-Frontend/
│
├── backend/         # Spring Boot Backend (Java)
├── frontend/        # Angular Frontend (TypeScript)
├── Documentation/   # Architektur, API und Datenmodell
├── keycloak/        # Keycloak-Konfiguration für Authentifizierung
└── README.md        # Diese Datei
```

## Features

- **Inventarisierung**: Anlegen und Bearbeiten von Inventargegenständen
- **Erweiterungen**: Verwaltung von Erweiterungen zu Inventargegenständen
- **Kommentare & Tags**: Kommentieren und Taggen von Inventargegenständen
- **Bestellintegration**: Verknüpfung von Inventargegenständen mit Bestellungen
- **Benutzerverwaltung**: Integration mit Keycloak für Authentifizierung und Autorisierung
- **REST API**: Dokumentiert in `Documentation/API-Documentation.yaml`

## Voraussetzungen

- **Backend**: Java 17+, Maven, Docker (optional)
- **Frontend**: Node.js (empfohlen v18+), npm, Angular CLI
- **Keycloak**: Für Authentifizierung (siehe `keycloak/`)

## Schnellstart

### Backend

1. In das Backend-Verzeichnis wechseln:
   ```sh
   cd backend
   ```
2. Mit Maven bauen und starten:
   ```sh
   ./mvnw spring-boot:run
   ```
   Alternativ mit Docker:
   ```sh
   docker compose -f dev.docker-compose.yml up
   ```

### Frontend

1. In das Frontend-Verzeichnis wechseln:
   ```sh
   cd frontend
   ```
2. Abhängigkeiten installieren:
   ```sh
   npm install
   ```
3. Entwicklungsserver starten:
   ```sh
   ng serve
   ```
   Die Anwendung ist dann unter [http://localhost:4200](http://localhost:4200) erreichbar.

### Keycloak

- Die Keycloak-Konfiguration befindet sich im Verzeichnis `keycloak/`.
- Hinweise zur Einrichtung siehe `Documentation/Keycloak.pdf`.

## Tests

### Backend

```sh
cd backend
./mvnw test
```

### Frontend

```sh
cd frontend
ng test
```

## Dokumentation

- **API-Dokumentation**: [Documentation/API-Documentation.yaml](Documentation/API-Documentation.yaml)
- **Datenmodell**: [Documentation/Datamodel_v2.svg](Documentation/Datamodel_v2.svg)
- **Architektur & Meilensteine**: [Documentation/Doku_Inventarisierungsmodul_Meilenstein2.pdf](Documentation/Doku_Inventarisierungsmodul_Meilenstein2.pdf)

## Mitwirken

Pull Requests und Issues sind willkommen! Bitte beachten Sie die jeweiligen Contributing Guidelines.

## Lizenz

Siehe [LICENSE](LICENSE).

---

**Kontakt:** Hochschule Esslingen, Fakultät Informatik, in Kooperation mit KEIM.
