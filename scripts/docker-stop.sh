#!/usr/bin/env bash

docker stop covid19tracker
docker rm covid19tracker
docker image rm covid19tracker
rm -rf app/covid19tracker-*.jar