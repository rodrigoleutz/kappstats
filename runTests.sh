#!/bin/bash
set -e

echo -e "\n\n#### Start pre-commit tests"
./gradlew clean
./gradlew :server:test \
          :composeApp:jvmTest \
          --parallel \
          --no-configuration-cache
echo -e "\n\n#### End pre-commit tests"