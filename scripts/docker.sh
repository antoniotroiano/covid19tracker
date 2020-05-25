#!/usr/bin/env bash

cd ..
mvn clean
mvn package
docker stop statistics
docker rm statistics
docker build -f DockerfileLocal -t statistics .
docker run --name statistics -d -p 8080:8080 statistics