#!/bin/bash
# devs will generally start this config file, so this is a fairly safe place to do this.

mvn exec:java -Dskip-tests -Dexec.args="client harold 0x2329edfb5a326f1f6daf6cec37e50e7cc9f9b151 5001 root@127.0.0.1:5000" &

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
