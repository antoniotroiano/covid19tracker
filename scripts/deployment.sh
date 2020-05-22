#!/bin/bash
cd ..
echo "$(tput setaf 7)------------------Statistic COVID-19 deployment started------------------"
if [[ $(git status --porcelain) ]]; then
  echo 'Please commit your changes before deployment'
  :
else
  oldVersion="$(egrep -o "\d[.]\d[.]\d[-](SNAPSHOT)" pom.xml)"
  echo "Old version of statistics application is: $(tput setaf 1) $oldVersion $(tput setaf 7)"
  echo "Please enter the new version number (e.g. 1.1.0-SNAPSHOT)"
  read newVersion
  sed -i.old -e 's/\<revision\>'$oldVersion'\<\/revision\>/\<revision\>'$newVersion'\<\/revision\>/g' pom.xml
  if grep -q $oldVersion "pom.xml"; then
    echo "Replacing version number failed. (Old version: $(tput setaf 1)$oldVersion$(tput setaf 7), new version: $(tput setaf 1)$newVersion$(tput setaf 7).) Aborting script..."
    git checkout .
    :
  else
    echo "Replaced $(tput setaf 1)$oldVersion$(tput setaf 7) with $(tput setaf 1) $newVersion$(tput setaf 7)."
    read -p "Next step: merging files to deploy branch and deploying to server. Do you want continue? (y or n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
      git add .
      git commit -m "Deployment: Changed version to $newVersion"
      git push
      git checkout deploy/master
      git merge master
      git push
      echo "Starting building .jar package..."
      cd v2
      mvn clean
      mvn package
      if [ ! -f target/v2-$newVersion.jar ]; then
        echo "Something went wrong. Built .jar file not found!"
        git checkout master
        :
      else
        echo "Copying .jar file to server..."
        scp -i ~/.ssh/coronaKey.pem target/v2-$newVersion.jar ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com:app/
        echo "Copying docker file to server..."
        cd ..
        scp -i ~/.ssh/coronaKey.pem Dockerfile ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com:app/
        echo "Copied .jar file and dockerfile to server. Connecting to server..."
        ssh -i "~/.ssh/coronaKey.pem" ec2-user@ec2-3-122-233-6.eu-central-1.compute.amazonaws.com /bin/bash <<EOF
cd ~/app/
echo "Stopping statistics application and deleting old image..."
docker stop statistics
docker rm statistics
echo "Stopped and deleted old statistics-corona application. Starting new version..."
docker build -f Dockerfile -t statistics .
docker run -d -p 8080:8080 statistics
echo "New version of statistics application is starting. Please wait some seconds..."
exit
EOF
        echo "Leaved server. Undo local changes..."
        git checkout master
        echo "-------------Statistic COVID-19 deployment finished-------------"
      fi
    else
      git checkout .
      echo "Aborted deployment!"
      :
    fi
  fi
fi
