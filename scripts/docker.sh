#!/usr/bin/env bash

cd ..
mvn clean
mvn package
docker stop statistics
docker rm statistics
docker build -f Dockerfile -t statistics .
docker run -d -p 8080:8080 statistics