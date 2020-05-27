#!/usr/bin/env bash

docker build -f Dockerfile -t statistics .
docker run --name statistics -d -p 8080:8080 statistics