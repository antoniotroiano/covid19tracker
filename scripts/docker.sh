#!/usr/bin/env bash

docker stop statistics
docker rm statistics
docker build -f Dockerfile -t statistics .
docker run --name statistics -d -p 8080:8080 statistics