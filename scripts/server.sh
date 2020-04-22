pkill -f 'java -jar'
rm statistics-corona-1.0-SNAPSHOT.jar
java -jar statistics-corona-1.1.0-SNAPSHOT.jar 2 >dump >&1 &
java -jar statistics-corona-1.1.0-SNAPSHOT.jar >dump 2>&1 &
