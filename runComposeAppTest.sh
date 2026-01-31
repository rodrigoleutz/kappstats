#!/bin/bash
set -e

echo -e "\n\n#### composeApp tests"
./gradlew :composeApp:jvmTest -Djava.awt.headless=true --no-configuration-cache