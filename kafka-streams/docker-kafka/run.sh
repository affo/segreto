#!/bin/bash
kafka-topics --create --topic InTopic \
        --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1
kafka-topics --create --topic OutTopic \
        --zookeeper zookeeper:2181 --partitions 1 --replication-factor 1

kafka-docker.sh "$@"
