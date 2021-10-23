#!/usr/bin/env bash

docker build -f app/Dockerfile -t covid19tracker .
docker run --name covid19tracker -d -p 8080:8080 covid19tracker