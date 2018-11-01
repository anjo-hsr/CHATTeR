#!/bin/bash
# devs will generally run this config file, so this is a fairly safe place to do this.

#Builds backend parallel to the frontend
./mvnw -f backend/pom.xml \
  "-DskipTests=true" \
  "-Ddeactivate-dev-tools=false" \
  clean install exec:java \
  &


sleep 20

cd frontend
npm run start
cd ..


if [ "$(uname)" == "Darwin" ]; then
  exit 0
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  exit 0
fi

wmic process Where "CommandLine Like '%CHATTER%'" call terminate

exit 0