#!/bin/bash
# devs will generally run this config file, so this is a fairly safe place to do this.

mvn exec:java -Dskip-tests -Dexec.args="client root 0xc0a71f7eb1a04a867a65022021f962b3a65a40a5 5001 8001 john@127.0.0.1:5000" &

while true
do
  sleep 10
done

if [ "$(uname)" == "Darwin" ]; then
  exit 0
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
  exit 0
fi

wmic process Where "CommandLine Like '%CHATTER%'" call terminate

exit 0