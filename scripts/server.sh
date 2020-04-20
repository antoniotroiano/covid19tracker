pkill -f 'java -jar'
rm statistics-corona-1.0-SNAPSHOT.jar
java -jar statistics-corona-1.0-SNAPSHOT.jar > dump 2 >&1 &