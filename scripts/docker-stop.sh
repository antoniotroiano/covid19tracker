#!/usr/bin/env bash

docker stop statistics
docker rm statistics
docker image rm statistics
rm -rf app/statistics-corona-*.jar