#!/bin/bash
cd ..
if [[ $(git status --porcelain) ]]; then
  echo 'Please commit your changes before deployment'
  :
else
  echo -e "-------------Statistic COVID-19 deployment started...-------------"
  echo "Please enter the old version number (e.g. 1.0.0-SNAPSHOT)"
  read oldVersion
  echo "Please enter the new version number (e.g. 1.1.0-SNAPSHOT)"
  read newVersion
  sed -i.old -e 's/\<version\>'$oldVersion'\<\/version\>/\<version\>'$newVersion'\<\/version\>/g' pom.xml
  git add .
  git commit -m "Deployment: Changed version to  $newVersion"
  git push
  git checkout deploy/master
  git merge master
  git push
  echo "Starting building .jar package..."
  mvn package
  if [ ! -f target/statistics-corona-$newVersion.jar ]; then
    echo "Something went wrong. Built .jar file not found!"
    :
  else
    echo "Copying .jar file to server..."
    scp -i ~/.ssh/coronaKey.pem target/statistics-corona-$newVersion.jar ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com:app/
    echo "Copied .jar file to server. Connecting to server..."
    ssh -i "~/.ssh/coronaKey.pem" ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com /bin/bash <<EOF
cd ~/app/
echo "Stopping statistics-corona application and deleting old .jar..."
pkill -f 'java -jar'
rm statistics-corona-$oldVersion.jar
echo "Stopped and deleted old statistics-corona application. Starting new version..."
java -jar statistics-corona-$newVersion.jar >dump 2>&1 &
echo "New version of statistics-corona application is starting. Please wait some seconds..."
exit
EOF
    echo "Leaved server. Undo local changes..."
    git checkout .
    echo "-------------Statistic COVID-19 deployment finished-------------"
  fi
fi
