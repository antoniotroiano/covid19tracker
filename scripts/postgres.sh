docker stop postgres-coronaStatistic
docker rm postgres-coronaStatistic
docker run --name postgres-coronaStatistic -e POSTGRES_DB=coronaStatistic -e POSTGRES_PASSWORD=password -p 32768:5432 -d postgres