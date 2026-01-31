#!/bin/bash
set -e

echo -e "\n\n#### server tests"
./gradlew :server:test --no-configuration-cache