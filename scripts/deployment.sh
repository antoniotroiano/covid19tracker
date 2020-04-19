#ssh -i "~/.ssh/coronaKey.pem" ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com
#cd app
#pkill -f 'java -jar'
#rm statistics-corona-1.0-SNAPSHOT.jar
mvn package
scp -i ~/.ssh/coronaKey.pem target/statistics-corona-1.0-SNAPSHOT.jar ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com:app/
#java -jar statistics-corona-1.0-SNAPSHOT.jar > dump 2 >&1 &