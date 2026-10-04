#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

docker compose up -d

echo "Waiting for Postgres to be ready..."
until docker compose exec postgres pg_isready -U tastytome >/dev/null 2>&1; do
    sleep 1
done

# Load environment variables from .env file if it exists
if [ -f ".env" ]; then
    set -a  # automatically export all variables
    source .env
    set +a  # unset auto-export
fi

# Get database credentials from .env
DB_URL="${DB_DATASOURCE:-jdbc:postgresql://localhost:5432/tastytome}"
DB_USER="${DB_USER:-tastytome}"
DB_PASS="${DB_PASS:-yourSecurePassword123!}"

echo "============================================"
echo "Running Liquibase Migrations..."
echo "Database: ${DB_URL}"
echo "User: ${DB_USER}"
echo "============================================"

# Run Liquibase with explicit database properties passed as Maven args
./mvnw liquibase:update \
    -DDB_DATASOURCE="${DB_URL}" \
    -DDB_USER="${DB_USER}" \
    -DDB_PASS="${DB_PASS}"

echo ""
echo "============================================"
echo "Generating JOOQ files..."
echo "============================================"

# Generate JOOQ with explicit JDBC connection
./mvnw jooq-codegen:generate \
    -DDB_DATASOURCE="${DB_URL}" \
    -DDB_USER="${DB_USER}" \
    -DDB_PASS="${DB_PASS}"

echo ""
echo "============================================"
echo "Migration complete!"
echo "============================================"