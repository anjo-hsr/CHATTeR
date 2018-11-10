#!/bin/bash
# devs will generally start this config file, so this is a fairly safe place to do this.

mvn exec:java -Dskip-tests -Dexec.args="client john 0x3079c583432ff5eb6a6a338d94f868c81db53f7c 5001 8001 root@127.0.0.1:5000" &

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