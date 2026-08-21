#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

# Load environment variables from .env file if it exists
if [ -f ".env" ]; then
    set -a  # automatically export all variables
    source .env
    set +a  # unset auto-export
fi

export MAVEN_OPTS="${MAVEN_OPTS} -Dspring.devtools.restart.enabled=true -Dspring.devtools.restart.poll-interval=1000"

./mvnw spring-boot:run