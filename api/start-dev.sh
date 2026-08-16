#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

export MAVEN_OPTS="${MAVEN_OPTS} -Dspring.devtools.restart.enabled=true -Dspring.devtools.restart.poll-interval=1000"

./mvnw spring-boot:run