#!/usr/bin/env bash
mvn clean package
echo 'Copy files...'
scp target/demo-0.0.1-SNAPSHOT.jar ubuntu@18.191.156.108:/home/ubuntu/
echo 'Restart server...'
ssh ubuntu@18.191.156.108 << EOF
pgrep java | xargs kill -9
nohup java -jar demo-0.0.1-SNAPSHOT.jar > log.txt &
echo Ready
EOF
echo 'Bye'