#!/bin/bash

APP=callback-consumer
NET=jarand-net

docker build -t $APP:latest .

docker rm -f $APP || true

docker network create $NET || true

docker run -d --name $APP --network $NET -p 8082:8080 \
-e SPRING_DATASOURCE_URL=jdbc:postgresql://twitch-collector-db:5432/twitch-collector-db \
$APP:latest
