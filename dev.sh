#!/bin/bash
# devs will generally run this config file, so this is a fairly safe place to do this.

#Builds backend parallel to the frontend
./mvnw -f backend/pom.xml \
  "-DskipTests=true" \
  "-Ddeactivate-dev-tools=false" \
  clean install exec:java \
  "-Dexec.args=master root 0xc0a71f7eb1a04a867a65022021f962b3a65a40a5 5000" \
  &


sleep 20

cd frontend
npm run start:root
cd ..


if [ "$(uname)" == "Darwin" ]; then
  exit 0
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  exit 0
fi

wmic process Where "CommandLine Like '%CHATTER%'" call terminate

exit 0
