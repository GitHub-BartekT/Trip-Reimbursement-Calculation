#!/bin/bash

function stop_spring_boot() {
    echo "Stopping Spring Boot..."
    mvn spring-boot:stop
}

function start_spring_boot() {
    echo "Starting Spring Boot..."
    mvn spring-boot:start
}

if [ "$1" == "stop" ]; then
    stop_spring_boot
elif [ "$1" == "start" ]; then
    start_spring_boot
else
    echo "Usage: $0 [stop|start]"
    exit 1
fi
