#!/usr/bin/bash
set -e

echo -n "#### Start pre-push build test"

echo "#### Killing Daemons for a fresh start"
./gradlew --stop

echo "#### Clean build"
./gradlew clean

echo "#### Starting fresh build (Clean + Parallel)"
./gradlew :server:build \
          :composeApp:packageAppImage \
          :androidApp:assembleDebug \
          --parallel \
          --no-configuration-cache

echo -n "\n\n#### End pre-push build test"