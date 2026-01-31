#!/bin/bash
set -e

echo -e "\n\n#### Start pre-commit tests"
./runClean.sh
./runServerTest.sh
./runComposeAppTest.sh
echo -e "\n\n#### End pre-commit tests"