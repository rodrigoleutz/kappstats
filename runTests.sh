#!/bin/bash
set -e

echo -e "\n\n#### Start pre-commit tests"
echo -e "\n\n#### clean"
./gradlew clean
echo -e "\n\n#### server tests"
./gradlew :server:test
echo -e "\n\n#### composeApp tests"
./gradlew :composeApp:jvmTest
echo -e "\n\n#### End pre-commit tests"