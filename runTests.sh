#!/bin/bash
set -e

echo -e "\n\n#### Start pre-commit tests"
./gradlew :server:test
./gradlew :composeApp:cleanJvmTest :composeApp:jvmTest
echo -e "\n\n#### End pre-commit tests"