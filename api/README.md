# TastyTome API Scripts Guide

## Available Scripts

### `migrate.sh`
Runs Liquibase migrations and generates JOOQ files.

**Usage:**
```bash
# Run migrations and generate JOOQ
./migrate.sh

# If using .env file with database credentials:
source .env  # loads DB credentials from .env
./migrate.sh
```

### `start-dev.sh`
Starts the Spring Boot development server.

**Usage:**
```bash
# Start the server (assumes migrations have already run)
./start-dev.sh
```

## Setup Steps

1. **Create database**: Ensure your PostgreSQL database exists with proper schema:
   ```sql
   CREATE DATABASE tastytome;
   CREATE USER tastytome WITH PASSWORD 'yourSecurePassword123!';
   GRANT ALL PRIVILEGES ON DATABASE tastytome TO tastytome;
   ```

2. **Create .env file** (optional but recommended):
   ```bash
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. **Run migrations**:
   ```bash
   ./migrate.sh
   ```

4. **Start the server**:
   ```bash
   ./start-dev.sh
   ```

## Environment Variables

The `.env` file should contain (these are used by both the application and migration scripts):

- `DB_DATASOURCE=jdbc:postgresql://localhost:5432/tastytome`
- `DB_USER=tastytome`
- `DB_PASS=yourSecurePassword123!`

The `.env.example` file shows additional optional properties.

## Notes

- The existing Liquibase migration (`20260820.01-InitUsersTable.sql`) is a test migration
- Add more SQL files to `db/changelog/migrations/` for additional migrations
- Liquibase will automatically detect and run all pending migrations
- JOOQ code generation creates Java DTOs from your database schema
