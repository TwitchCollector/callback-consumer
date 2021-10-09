#!/bin/bash

DB=twitch-collector-db
NET=jarand-net

docker rm -f $DB || true

docker network create $NET || true

docker run -d --name $DB --network $NET -p 5432:5432 \
  -e POSTGRES_DB=twitch-collector-db \
  -e POSTGRES_USER=twitch-collector-dbo \
  -e POSTGRES_PASSWORD=postgres \
  postgres:latest
