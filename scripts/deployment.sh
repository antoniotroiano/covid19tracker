#!/bin/bash
cd ..
echo -e "-------------Teamleadwahl deployment started...-------------"
if [[ $(git status --porcelain) ]]; then
  echo 'Please commit your changes before deployment'
  :
else
  echo "Please enter the old version number (e.g. 0.8.11-SNAPSHOT)"
  read oldVersion
  echo "Please enter the new version number (e.g. 0.8.12-SNAPSHOT)"
  read newVersion
  sed -i.old -e 's/\<version\>'$oldVersion'\<\/version\>/\<version\>'$newVersion'\<\/version\>/g' pom.xml
  git add .
  git commit -m "Deployment: Changed version to  $newVersion"
  git push
  git checkout master-deploy
  git merge master
  git push
  cd src/main/resources/
  echo "Please enter the password for the database"
  read dbPassword
  sed -i.old -e 's/=password/='$dbPassword'/g' application.properties
  cd ../../..
  echo "Starting building .jar package..."
  mvn package
  if [ ! -f target/statistics-corona-$newVersion.jar ]; then
    echo "Something went wrong. Built .jar file not found!"
    :
  else
    echo "Copying .jar file to server..."
    scp -i ~/.ssh/coronaKey.pem target/statistics-corona-$newVersion.jar ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com:app/
    echo "Copied .jar file to server. Connecting to server..."
    ssh -i "~/.ssh/coronaKey.pem" ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com
    cd ~/app/
    echo "Stopping teamleadwahl application and deleting old .jar..."
    pkill -f 'java -jar'
    rm statistics-corona-1.0-SNAPSHOT.jar
    echo "Stopped and deleted old teamleadwahl application. Starting new version..."
    java -jar statistics-corona-x.x-SNAPSHOT.jar 2 >dump >&1 &
    echo "New version of teamleadwahl application is starting. Please wait some seconds..."
    exit
    echo "Leaved server. Undo local changes..."
    git checkout .
    echo "-------------Teamleadwahl deployment finished-------------"
  fi
fi
