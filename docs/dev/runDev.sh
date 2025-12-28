#!/usr/bin/bash
set -e
echo "### Create mongo directory in /srv"
mkdir -p /srv/mongo
echo "### Start docker compose"
docker compose --env-file .env up -d